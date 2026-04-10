package com.karthi.sprintiq.tickets;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.projects.ProjectsService;
import com.karthi.sprintiq.tickets.dto.TicketDTO;
import com.karthi.sprintiq.tickets.entity.Ticket;
import com.karthi.sprintiq.tickets.repository.TicketsRepository;
import com.karthi.sprintiq.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketsService {

  private final TicketsRepository ticketsRepository;
  private final ProjectsService projectsService;
  private final UserService userService;

  public TicketDTO createTicket(TicketDTO request) {
    Ticket ticket = new Ticket();
    ticket.setTitle(request.getTitle());
    ticket.setDescription(request.getDescription());
    ticket.setStatus(request.getStatus());
    ticket.setPriority(request.getPriority());
    ticket.setAssignee(userService.getUserById(request.getAssigneeId()));
    ticket.setSection(projectsService.getSectionById(request.getSectionId()));
    return toDTO(ticketsRepository.save(ticket));
  }

  public List<TicketDTO> getAllTickets() {
    return ticketsRepository.findAll().stream().map(this::toDTO).toList();
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
    ticket.setAssignee(userService.getUserById(request.getAssigneeId()));
    ticket.setSection(projectsService.getSectionById(request.getSectionId()));
    ticketsRepository.save(ticket);
  }

  public void deleteTicket(Long id) {
    Ticket ticket = getTicketById(id);
    ticketsRepository.delete(ticket);
  }

  private TicketDTO toDTO(Ticket ticket) {
    TicketDTO dto = new TicketDTO();
    dto.setId(ticket.getId());
    dto.setTitle(ticket.getTitle());
    dto.setDescription(ticket.getDescription());
    dto.setStatus(ticket.getStatus());
    dto.setPriority(ticket.getPriority());
    dto.setAssigneeId(ticket.getAssignee().getId());
    dto.setSectionId(ticket.getSection().getId());
    return dto;
  }
}
