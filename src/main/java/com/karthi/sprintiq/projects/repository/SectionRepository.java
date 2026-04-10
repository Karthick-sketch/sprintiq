package com.karthi.sprintiq.projects.repository;

import com.karthi.sprintiq.projects.entity.Section;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
  List<Section> findByProjectId(Long projectId);
}
