package com.karthi.sprintiq.fields.controller;

import com.karthi.sprintiq.fields.service.TicketFieldValueService;
import com.karthi.sprintiq.fields.dto.TicketFieldValueRequest;
import com.karthi.sprintiq.fields.dto.TicketFieldValueResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets/{ticketId}/field-values")
@RequiredArgsConstructor
public class TicketFieldValueController {

    private final TicketFieldValueService ticketFieldValueService;

    @GetMapping
    public List<TicketFieldValueResponse> getFieldValues(@PathVariable Long ticketId) {
        return ticketFieldValueService.getFieldValues(ticketId);
    }

    @PutMapping
    public List<TicketFieldValueResponse> saveFieldValues(
        @PathVariable Long ticketId,
        @RequestBody List<TicketFieldValueRequest> requests
    ) {
        return ticketFieldValueService.saveFieldValues(ticketId, requests);
    }

    @PatchMapping("/{projectFieldId}")
    public TicketFieldValueResponse patchFieldValue(
        @PathVariable Long ticketId,
        @PathVariable Long projectFieldId,
        @RequestBody TicketFieldValueRequest request
    ) {
        return ticketFieldValueService.patchFieldValue(ticketId, projectFieldId, request);
    }
}
