package com.karthi.sprintiq.projects.repository;

import com.karthi.sprintiq.projects.dto.ProjectTitleDTO;
import com.karthi.sprintiq.projects.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Long> {
  @Query(
      value =
          "SELECT new com.karthi.sprintiq.projects.dto.ProjectTitleDTO(p.id, p.title) FROM Project p")
  List<ProjectTitleDTO> findAllIdAndTitle();
}
