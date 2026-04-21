package com.karthi.sprintiq.tickets.repository;

import com.karthi.sprintiq.tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketsRepository
  extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {}
