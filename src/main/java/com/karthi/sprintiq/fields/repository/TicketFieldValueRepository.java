package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.TicketFieldValue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketFieldValueRepository extends JpaRepository<TicketFieldValue, Long> {

    List<TicketFieldValue> findByTicketIdOrderByProjectFieldIdAscValueIndexAsc(Long ticketId);

    void deleteByTicketIdAndProjectFieldId(Long ticketId, Long projectFieldId);

    boolean existsByProjectFieldId(Long projectFieldId);
}
