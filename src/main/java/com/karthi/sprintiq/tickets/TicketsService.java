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
    Ticket ticket = new Ticket();
    ticket.setTitle(request.getTitle());
    ticket.setDescription(request.getDescription());
    ticket.setStatus(request.getStatus());
    ticket.setPriority(request.getPriority());
    ticket.setDueDate(request.getDueDate());
    ticket.setAssignee(userService.getUserById(request.getAssigneeId()));
    ticket.setSection(projectsService.getSectionById(request.getSectionId()));
    ticket.setOrderIndex(request.getOrderIndex());
    return toDTO(ticketsRepository.save(ticket));
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
    Ticket ticket = getTicketById(id);
    return toDTO(ticket);
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
    Ticket ticket = getTicketById(id);
    ticketsRepository.delete(ticket);
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

  private TicketDTO toDTO(Ticket ticket) {
    TicketDTO dto = new TicketDTO();
    dto.setId(ticket.getId());
    dto.setTitle(ticket.getTitle());
    dto.setDescription(ticket.getDescription());
    dto.setStatus(ticket.getStatus());
    dto.setPriority(ticket.getPriority());
    dto.setAssignee(userService.toDTO(ticket.getAssignee()));
    dto.setSectionId(ticket.getSection().getId());
    return dto;
  }

  private TicketListingDTO toListingDTO(Ticket ticket) {
    TicketListingDTO dto = new TicketListingDTO();
    dto.setId(ticket.getId());
    dto.setTitle(ticket.getTitle());
    dto.setStatus(ticket.getStatus());
    dto.setPriority(ticket.getPriority());
    dto.setAssignee(userService.toDTO(ticket.getAssignee()));
    dto.setDueDate(ticket.getDueDate());
    dto.setProject(
      projectsService.toProjectTicketListDTO(ticket.getSection().getProject())
    );
    return dto;
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
