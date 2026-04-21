package com.karthi.sprintiq.tickets;

import com.karthi.sprintiq.tickets.dto.TicketDTO;
import com.karthi.sprintiq.tickets.dto.TicketListingDTO;
import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketsController {

  private final TicketsService ticketsService;

  @PostMapping
  public TicketDTO createTicket(@RequestBody TicketDTO request) {
    return ticketsService.createTicket(request);
  }

  @GetMapping
  public List<TicketListingDTO> getAllTickets(
    @RequestParam(required = false) String search,
    @RequestParam(required = false) Status status,
    @RequestParam(required = false) Priority priority,
    @RequestParam(required = false) Long assigneeId,
    @RequestParam(required = false) Long projectId
  ) {
    return ticketsService.getAllTickets(
      search,
      status,
      priority,
      assigneeId,
      projectId
    );
  }

  @GetMapping("/{id}")
  public TicketDTO getTicketById(@PathVariable Long id) {
    return ticketsService.getTicketByIdToDTO(id);
  }

  @PutMapping("/{id}")
  public void updateTicket(
    @PathVariable Long id,
    @RequestBody TicketDTO request
  ) {
    ticketsService.updateTicket(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteTicket(@PathVariable Long id) {
    ticketsService.deleteTicket(id);
  }
}
