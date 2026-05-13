package com.karthi.sprintiq.tickets.controller;

import com.karthi.sprintiq.fields.service.TicketSearchService;
import com.karthi.sprintiq.fields.dto.TicketSearchRequest;
import com.karthi.sprintiq.tickets.service.TicketsService;
import com.karthi.sprintiq.tickets.dto.TicketCreateRequestDTO;
import com.karthi.sprintiq.tickets.dto.TicketDTO;
import com.karthi.sprintiq.tickets.dto.TicketListingDTO;
import com.karthi.sprintiq.tickets.dto.TicketOrderDTO;
import com.karthi.sprintiq.tickets.dto.TicketUpdateRequestDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
  private final TicketSearchService ticketSearchService;

  @PostMapping
  public TicketDTO createTicket(@RequestBody TicketCreateRequestDTO request) {
    return ticketsService.createTicket(request);
  }

  @GetMapping
  public List<TicketListingDTO> getAllTickets(
    @RequestParam(required = false) String search,
    @RequestParam(required = false) String status,
    @RequestParam(required = false) String priority,
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
    @RequestBody TicketUpdateRequestDTO request
  ) {
    ticketsService.updateTicket(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteTicket(@PathVariable Long id) {
    ticketsService.deleteTicket(id);
  }

  @PutMapping("/section/{sectionId}/reorder")
  public void reorderTickets(
    @PathVariable Long sectionId,
    @RequestBody List<TicketOrderDTO> tickets
  ) {
    ticketsService.reorderTickets(sectionId, tickets);
  }

  @PostMapping("/search")
  public Page<TicketListingDTO> searchTickets(@RequestBody TicketSearchRequest request) {
    return ticketSearchService.search(request);
  }
}
