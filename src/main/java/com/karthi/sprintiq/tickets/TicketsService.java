package com.karthi.sprintiq.tickets;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.fields.dto.FieldDTO;
import com.karthi.sprintiq.fields.entity.Field;
import com.karthi.sprintiq.projects.ProjectsService;
import com.karthi.sprintiq.projects.entity.Section;
import com.karthi.sprintiq.tickets.dto.*;
import com.karthi.sprintiq.tickets.entity.Ticket;
import com.karthi.sprintiq.tickets.entity.TicketComment;
import com.karthi.sprintiq.tickets.entity.TicketField;
import com.karthi.sprintiq.tickets.repository.TicketsRepository;
import com.karthi.sprintiq.tickets.specification.TicketSpecification;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketsService {

  private final TicketsRepository ticketsRepository;
  private final ProjectsService projectsService;

  public TicketDTO createTicket(TicketCreateRequestDTO request) {
    return toDTO(ticketsRepository.save(toTicket(request)));
  }

  public List<TicketListingDTO> getAllTickets(
    String search,
    String status,
    String priority,
    Long assigneeId,
    Long projectId
  ) {
    // Specification<Ticket> ticketSpecification = buildSpecification(
    //   search,
    //   status,
    //   priority,
    //   assigneeId,
    //   projectId
    // );
    // List<Ticket> tickets = ticketsRepository.findAll(ticketSpecification);
    List<Ticket> tickets = ticketsRepository.findAll();
    List<TicketListingDTO> ticketListings = tickets
      .stream()
      .map(this::toListingDTO)
      .toList();
    return ticketListings;
  }

  private Ticket getTicketById(Long id) {
    return ticketsRepository
      .findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
  }

  public TicketDTO getTicketByIdToDTO(Long id) {
    return toDTO(getTicketById(id));
  }

  public void updateTicket(Long id, TicketUpdateRequestDTO request) {
    Ticket ticket = getTicketById(id);
    ticket.setTitle(request.getTitle());
    ticket.setDescription(request.getDescription());
    ticket.setProject(projectsService.getProjectById(request.getProjectId()));
    ticket.setSection(projectsService.getSectionById(request.getSectionId()));
    ticket.setOrderIndex(request.getOrderIndex());
    ticketsRepository.save(ticket);
  }

  public void deleteTicket(Long id) {
    ticketsRepository.delete(getTicketById(id));
  }

  @Transactional
  public void reorderTickets(
    Long sectionId,
    List<TicketOrderDTO> ticketOrders
  ) {
    Map<Long, Integer> ticketOrderMap = ticketOrders
      .stream()
      .collect(
        Collectors.toMap(TicketOrderDTO::getId, TicketOrderDTO::getOrderIndex)
      );
    List<Ticket> tickets = ticketsRepository.findByIdIn(
      ticketOrderMap.keySet()
    );
    Section section = projectsService.getSectionById(sectionId);

    tickets.forEach(ticket -> {
      ticket.setSection(section);
      ticket.setOrderIndex(ticketOrderMap.get(ticket.getId()));
    });
    ticketsRepository.saveAll(tickets);
  }

  private Ticket toTicket(TicketCreateRequestDTO request) {
    return Ticket.builder()
      .title(request.getTitle())
      .description(request.getDescription())
      .project(projectsService.getProjectById(request.getProjectId()))
      .section(projectsService.getSectionById(request.getSectionId()))
      .orderIndex(request.getOrderIndex())
      .build();
  }

  private TicketDTO toDTO(Ticket ticket) {
    return TicketDTO.builder()
      .id(ticket.getId())
      .title(ticket.getTitle())
      .description(ticket.getDescription())
      .comments(toCommentDTO(ticket.getComments()))
      .ticketFields(toTicketFieldsDTO(ticket.getFields()))
      .project(projectsService.toProjectTitleDTO(ticket.getProject()))
      .sectionId(
        ticket.getSection() != null ? ticket.getSection().getId() : null
      )
      .parentId(ticket.getParent() != null ? ticket.getParent().getId() : null)
      .subTicketIds(
        ticket.getSubTickets() != null
          ? ticket.getSubTickets().stream().map(Ticket::getId).toList()
          : null
      )
      .orderIndex(ticket.getOrderIndex())
      .build();
  }

  private TicketListingDTO toListingDTO(Ticket ticket) {
    return TicketListingDTO.builder()
      .id(ticket.getId())
      .title(ticket.getTitle())
      .project(
        ticket.getSection() != null
          ? projectsService.toProjectTitleDTO(ticket.getSection().getProject())
          : null
      )
      .build();
  }

  private List<TicketCommentDTO> toCommentDTO(
    List<TicketComment> ticketComments
  ) {
    return ticketComments
      .stream()
      .map(ticketComment ->
        TicketCommentDTO.builder()
          .id(ticketComment.getId())
          .content(ticketComment.getContent())
          .createdAt(ticketComment.getCreatedAt())
          .createdById(ticketComment.getCreatedBy().getId())
          .build()
      )
      .toList();
  }

  private List<TicketFieldDTO> toTicketFieldsDTO(
    List<TicketField> ticketFields
  ) {
    return ticketFields.stream().map(this::toTicketFieldDTO).toList();
  }

  private TicketFieldDTO toTicketFieldDTO(TicketField ticketField) {
    return TicketFieldDTO.builder()
      .id(ticketField.getId())
      .field(toFieldDTO(ticketField.getField()))
      .value(ticketField.getValue())
      .build();
  }

  private FieldDTO toFieldDTO(Field field) {
    return FieldDTO.builder()
      .id(field.getId())
      .systemKey(field.getSystemKey())
      .name(field.getName())
      .description(field.getDescription())
      .fieldKind(field.getFieldKind())
      .fieldType(field.getFieldType())
      .system(field.isSystem())
      .locked(field.isLocked())
      .searchable(field.isSearchable())
      .active(field.isActive())
      .build();
  }


  private Specification<Ticket> buildSpecification(
    String search,
    String status,
    String priority,
    Long assigneeId,
    Long projectId
  ) {
    Specification<Ticket> ticketSpecification = (
      root,
      query,
      criteriaBuilder
    ) -> criteriaBuilder.conjunction();
    return ticketSpecification
      .and(TicketSpecification.withTitleContaining(search))
      .and(TicketSpecification.hasStatus(status))
      .and(TicketSpecification.hasPriority(priority))
      .and(TicketSpecification.hasAssigneeId(assigneeId))
      .and(TicketSpecification.hasProjectId(projectId))
      .and(TicketSpecification.orderByDueDateAsc());
  }
}
