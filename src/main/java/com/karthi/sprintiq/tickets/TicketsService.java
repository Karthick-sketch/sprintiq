package com.karthi.sprintiq.tickets;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.projects.ProjectsService;
import com.karthi.sprintiq.tickets.dto.TicketDTO;
import com.karthi.sprintiq.tickets.entity.Ticket;
import com.karthi.sprintiq.tickets.repository.TicketsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketsService {

  private final TicketsRepository ticketsRepository;
  private final ProjectsService projectsService;

  public TicketDTO createTicket(TicketDTO request) {
    Ticket ticket = new Ticket();
    ticket.setTitle(request.getTitle());
    ticket.setDescription(request.getDescription());
    ticket.setStatus(request.getStatus());
    ticket.setPriority(request.getPriority());
    ticket.setAssignee(request.getAssignee());
    ticket.setProject(projectsService.getProjectById(request.getProjectId()));
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
    ticket.setAssignee(request.getAssignee());
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
    dto.setAssignee(ticket.getAssignee());
    dto.setProjectId(ticket.getProject().getId());
    return dto;
  }
}
