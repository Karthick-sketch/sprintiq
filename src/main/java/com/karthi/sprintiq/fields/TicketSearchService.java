package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.fields.dto.TicketSearchRequest;
import com.karthi.sprintiq.fields.dto.TicketSearchRequest.FieldFilter;
import com.karthi.sprintiq.fields.dto.TicketSearchRequest.FilterOperator;
import com.karthi.sprintiq.fields.entity.FieldDefinition;
import com.karthi.sprintiq.fields.enums.WorkflowSemanticKey;
import com.karthi.sprintiq.fields.repository.FieldRepository;
import com.karthi.sprintiq.tickets.dto.TicketListingDTO;
import com.karthi.sprintiq.tickets.entity.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketSearchService {

    private final EntityManager em;
    private final FieldRepository fieldRepository;

    public Page<TicketListingDTO> search(TicketSearchRequest req) {
        int page = req.getPage() != null ? req.getPage() : 0;
        int size = req.getSize() != null ? req.getSize() : 50;

        CriteriaBuilder cb = em.getCriteriaBuilder();

        // ── Count query ──────────────────────────────────────────────────────
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Ticket> countRoot = countQuery.from(Ticket.class);
        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(buildPredicates(cb, countRoot, countQuery, req));
        long total = em.createQuery(countQuery).getSingleResult();

        // ── Data query ───────────────────────────────────────────────────────
        CriteriaQuery<Ticket> dataQuery = cb.createQuery(Ticket.class);
        Root<Ticket> root = dataQuery.from(Ticket.class);
        dataQuery.select(root).distinct(true);
        dataQuery.where(buildPredicates(cb, root, dataQuery, req));

        // Sort
        if (req.getSort() != null && !req.getSort().isEmpty()) {
            List<Order> orders = req.getSort().stream().map(s -> {
                try {
                    Path<Object> path = root.get(s.getField());
                    return "DESC".equalsIgnoreCase(s.getDirection())
                        ? cb.desc(path) : cb.asc(path);
                } catch (Exception e) {
                    return cb.desc(root.get("id"));
                }
            }).toList();
            dataQuery.orderBy(orders);
        } else {
            dataQuery.orderBy(cb.desc(root.get("id")));
        }

        TypedQuery<Ticket> typedQuery = em.createQuery(dataQuery)
            .setFirstResult(page * size)
            .setMaxResults(size);

        List<TicketListingDTO> results = typedQuery.getResultList()
            .stream().map(t -> TicketListingDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .build()
            ).toList();

        return new PageImpl<>(results, PageRequest.of(page, size), total);
    }

    @SuppressWarnings("unchecked")
    private Predicate buildPredicates(CriteriaBuilder cb, Root<Ticket> root,
                                       CriteriaQuery<?> query, TicketSearchRequest req) {
        List<Predicate> predicates = new ArrayList<>();

        // Project filter
        if (req.getProjectIds() != null && !req.getProjectIds().isEmpty()) {
            predicates.add(root.get("project").get("id").in(req.getProjectIds()));
        }

        // Workflow semantic filter using EXISTS subquery
        if (req.getWorkflowSemanticKeys() != null && !req.getWorkflowSemanticKeys().isEmpty()) {
            predicates.add(buildSemanticPredicate(cb, root, query, req.getWorkflowSemanticKeys()));
        }

        // Dynamic field filters — each as a separate EXISTS subquery
        if (req.getFieldFilters() != null) {
            for (FieldFilter filter : req.getFieldFilters()) {
                Predicate p = buildFieldFilterPredicate(cb, root, query, filter);
                if (p != null) predicates.add(p);
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate buildSemanticPredicate(CriteriaBuilder cb, Root<Ticket> root,
                                              CriteriaQuery<?> query,
                                              List<WorkflowSemanticKey> semanticKeys) {
        // EXISTS (SELECT 1 FROM ticket_field_values tfv
        //   JOIN project_fields pf ON pf.id = tfv.project_field_id
        //   JOIN fields f ON f.id = pf.field_id AND f.system_key = 'status'
        //   LEFT JOIN field_options fo ON fo.id = tfv.field_option_id
        //   LEFT JOIN project_field_options pfo ON pfo.id = tfv.project_field_option_id
        //   WHERE tfv.ticket_id = tk.id
        //   AND COALESCE(pfo.workflow_semantic_key, fo.workflow_semantic_key) IN (...))
        Subquery<Long> sub = query.subquery(Long.class);
        Root<com.karthi.sprintiq.fields.entity.TicketFieldValue> tfv =
            sub.from(com.karthi.sprintiq.fields.entity.TicketFieldValue.class);
        sub.select(cb.literal(1L));

        List<Predicate> subPreds = new ArrayList<>();
        subPreds.add(cb.equal(tfv.get("ticketId"), root.get("id")));
        subPreds.add(cb.equal(
            tfv.get("projectField").get("field").get("systemKey"),
            "status"
        ));

        // COALESCE(pfo.semantic, fo.semantic) IN (keys)
        List<String> semanticStrings = semanticKeys.stream()
            .map(WorkflowSemanticKey::name).toList();
        Predicate semanticMatch = cb.or(
            tfv.get("projectFieldOption").get("workflowSemanticKey").in(semanticStrings),
            cb.and(
                cb.isNull(tfv.get("projectFieldOption")),
                tfv.get("fieldOption").get("workflowSemanticKey").in(semanticStrings)
            )
        );
        subPreds.add(semanticMatch);
        sub.where(cb.and(subPreds.toArray(new Predicate[0])));

        return cb.exists(sub);
    }

    private Predicate buildFieldFilterPredicate(CriteriaBuilder cb, Root<Ticket> root,
                                                 CriteriaQuery<?> query, FieldFilter filter) {
        Subquery<Long> sub = query.subquery(Long.class);
        Root<com.karthi.sprintiq.fields.entity.TicketFieldValue> tfv =
            sub.from(com.karthi.sprintiq.fields.entity.TicketFieldValue.class);
        sub.select(cb.literal(1L));

        List<Predicate> subPreds = new ArrayList<>();
        subPreds.add(cb.equal(tfv.get("ticketId"), root.get("id")));

        // Identify field by systemKey or fieldId
        if (filter.getSystemKey() != null) {
            subPreds.add(cb.equal(
                tfv.get("projectField").get("field").get("systemKey"),
                filter.getSystemKey()
            ));
        } else if (filter.getFieldId() != null) {
            subPreds.add(cb.equal(
                tfv.get("projectField").get("field").get("id"),
                filter.getFieldId()
            ));
        } else {
            return null;
        }

        FilterOperator op = filter.getOperator();
        if (op == null) return null;

        switch (op) {
            case IN_OPTIONS -> {
                if (filter.getOptionValueKeys() != null && !filter.getOptionValueKeys().isEmpty()) {
                    subPreds.add(cb.or(
                        tfv.get("fieldOption").get("valueKey").in(filter.getOptionValueKeys()),
                        tfv.get("projectFieldOption").get("valueKey").in(filter.getOptionValueKeys())
                    ));
                }
            }
            case IN_USERS -> {
                if (filter.getUserIds() != null && !filter.getUserIds().isEmpty()) {
                    subPreds.add(tfv.get("user").get("id").in(filter.getUserIds()));
                }
            }
            case TEXT_CONTAINS -> {
                if (filter.getTextValue() != null) {
                    subPreds.add(cb.like(cb.lower(tfv.get("textValue")),
                        "%" + filter.getTextValue().toLowerCase() + "%"));
                }
            }
            case NUMBER_GTE -> {
                if (filter.getNumberValue() != null) {
                    subPreds.add(cb.ge(tfv.get("numberValue"), filter.getNumberValue()));
                }
            }
            case NUMBER_LTE -> {
                if (filter.getNumberValue() != null) {
                    subPreds.add(cb.le(tfv.get("numberValue"), filter.getNumberValue()));
                }
            }
            case IS_EMPTY -> { return cb.not(cb.exists(sub)); }
            case IS_NOT_EMPTY -> { /* sub predicate sufficient */ }
            default -> { return null; }
        }

        sub.where(cb.and(subPreds.toArray(new Predicate[0])));
        return cb.exists(sub);
    }
}
