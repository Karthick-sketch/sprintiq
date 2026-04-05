package com.karthi.sprintiq.projects.repository;

import com.karthi.sprintiq.projects.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Long> {}
