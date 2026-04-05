package com.karthi.sprintiq.tickets;

import com.karthi.sprintiq.tickets.dto.TicketDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public List<TicketDTO> getAllTickets() {
    return ticketsService.getAllTickets();
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
