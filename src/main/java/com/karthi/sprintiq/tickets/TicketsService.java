package com.karthi.sprintiq.tickets;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.projects.ProjectsService;
import com.karthi.sprintiq.projects.entity.Section;
import com.karthi.sprintiq.tickets.dto.TicketDTO;
import com.karthi.sprintiq.tickets.dto.TicketListingDTO;
import com.karthi.sprintiq.tickets.dto.TicketOrderDTO;
import com.karthi.sprintiq.tickets.dto.TicketRequestDTO;
import com.karthi.sprintiq.tickets.entity.Ticket;
import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
import com.karthi.sprintiq.tickets.repository.TicketsRepository;
import com.karthi.sprintiq.tickets.specification.TicketSpecification;
import com.karthi.sprintiq.user.UserService;
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
  private final UserService userService;

  public TicketDTO createTicket(TicketRequestDTO request) {
    return toDTO(ticketsRepository.save(toTicket(request)));
  }

  public List<TicketListingDTO> getAllTickets(
    String search,
    Status status,
    Priority priority,
    Long assigneeId,
    Long projectId
  ) {
    Specification<Ticket> ticketSpecification = buildSpecification(
      search,
      status,
      priority,
      assigneeId,
      projectId
    );
    return ticketsRepository
      .findAll(ticketSpecification)
      .stream()
      .map(this::toListingDTO)
      .toList();
  }

  private Ticket getTicketById(Long id) {
    return ticketsRepository
      .findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
  }

  public TicketDTO getTicketByIdToDTO(Long id) {
    return toDTO(getTicketById(id));
  }

  public void updateTicket(Long id, TicketDTO request) {
    Ticket ticket = getTicketById(id);
    ticket.setTitle(request.getTitle());
    ticket.setDescription(request.getDescription());
    ticket.setStatus(request.getStatus());
    ticket.setPriority(request.getPriority());
    ticket.setAssignee(userService.getUserById(request.getAssignee().getId()));
    ticket.setSection(projectsService.getSectionById(request.getSectionId()));
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

  private Ticket toTicket(TicketRequestDTO request) {
    return Ticket.builder()
      .title(request.getTitle())
      .description(request.getDescription())
      .status(request.getStatus())
      .priority(request.getPriority())
      .dueDate(request.getDueDate())
      .assignee(userService.getUserById(request.getAssigneeId()))
      .section(projectsService.getSectionById(request.getSectionId()))
      .orderIndex(request.getOrderIndex())
      .build();
  }

  private TicketDTO toDTO(Ticket ticket) {
    return TicketDTO.builder()
      .id(ticket.getId())
      .title(ticket.getTitle())
      .description(ticket.getDescription())
      .status(ticket.getStatus())
      .priority(ticket.getPriority())
      .dueDate(ticket.getDueDate())
      .assignee(userService.toDTO(ticket.getAssignee()))
      .sectionId(ticket.getSection().getId())
      .orderIndex(ticket.getOrderIndex())
      .build();
  }

  private TicketListingDTO toListingDTO(Ticket ticket) {
    return TicketListingDTO.builder()
      .id(ticket.getId())
      .title(ticket.getTitle())
      .status(ticket.getStatus())
      .priority(ticket.getPriority())
      .assignee(userService.toDTO(ticket.getAssignee()))
      .dueDate(ticket.getDueDate())
      .project(
        projectsService.toProjectTicketListDTO(ticket.getSection().getProject())
      )
      .build();
  }

  private Specification<Ticket> buildSpecification(
    String search,
    Status status,
    Priority priority,
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
