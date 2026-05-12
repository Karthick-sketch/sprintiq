package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.ProjectFieldOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFieldOptionRepository extends JpaRepository<ProjectFieldOption, Long> {

    List<ProjectFieldOption> findByProjectFieldIdOrderBySortOrderAsc(Long projectFieldId);

    List<ProjectFieldOption> findByProjectFieldIdAndActiveTrue(Long projectFieldId);
}
