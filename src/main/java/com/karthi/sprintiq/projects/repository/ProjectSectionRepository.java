package com.karthi.sprintiq.projects.repository;

import com.karthi.sprintiq.projects.entity.ProjectSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSectionRepository
  extends JpaRepository<ProjectSection, Long> {}
