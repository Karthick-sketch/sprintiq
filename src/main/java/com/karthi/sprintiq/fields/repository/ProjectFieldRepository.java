package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.ProjectField;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFieldRepository extends JpaRepository<ProjectField, Long> {

    List<ProjectField> findByProjectIdOrderBySortOrderAsc(Long projectId);

    List<ProjectField> findByProjectIdAndEnabledTrueOrderBySortOrderAsc(Long projectId);

    Optional<ProjectField> findByProjectIdAndFieldId(Long projectId, Long fieldId);

    boolean existsByProjectIdAndFieldId(Long projectId, Long fieldId);

    @Query("SELECT COUNT(tfv) > 0 FROM TicketFieldValue tfv WHERE tfv.projectField.id = :projectFieldId")
    boolean hasValues(Long projectFieldId);
}
