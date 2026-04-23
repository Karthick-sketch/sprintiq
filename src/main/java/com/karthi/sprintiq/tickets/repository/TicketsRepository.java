package com.karthi.sprintiq.tickets.repository;

import com.karthi.sprintiq.tickets.entity.Ticket;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketsRepository
  extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket>
{
  List<Ticket> findByIdIn(Set<Long> ids);
}
