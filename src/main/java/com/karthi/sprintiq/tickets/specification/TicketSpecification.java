package com.karthi.sprintiq.tickets.specification;

import com.karthi.sprintiq.tickets.entity.Ticket;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecification {

  public static Specification<Ticket> withTitleContaining(String search) {
    return (root, query, criteriaBuilder) -> {
      if (search == null || search.isEmpty() || search.isBlank()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(root.get("title"), "%" + search + "%");
    };
  }

  public static Specification<Ticket> hasStatus(String status) {
    return (root, query, criteriaBuilder) -> {
      if (status == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("status"), status);
    };
  }

  public static Specification<Ticket> hasPriority(String priority) {
    return (root, query, criteriaBuilder) -> {
      if (priority == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("priority"), priority);
    };
  }

  public static Specification<Ticket> hasAssigneeId(Long assigneeId) {
    return (root, query, criteriaBuilder) -> {
      if (assigneeId == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId);
    };
  }

  public static Specification<Ticket> hasProjectId(Long projectId) {
    return (root, query, criteriaBuilder) -> {
      if (projectId == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(
        root.join("section").get("project").get("id"),
        projectId
      );
    };
  }

  public static Specification<Ticket> orderByDueDateAsc() {
    return (root, query, criteriaBuilder) -> {
      query.orderBy(criteriaBuilder.asc(root.get("dueDate")));
      return criteriaBuilder.conjunction();
    };
  }
}
