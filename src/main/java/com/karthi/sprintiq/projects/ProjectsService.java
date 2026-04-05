package com.karthi.sprintiq.projects;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.projects.dto.ProjectDTO;
import com.karthi.sprintiq.projects.entity.Project;
import com.karthi.sprintiq.projects.repository.ProjectsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectsService {

  private final ProjectsRepository projectsRepository;

  public ProjectDTO createProject(ProjectDTO request) {
    Project project = new Project();
    project.setName(request.getName());
    project.setDescription(request.getDescription());
    return toDTO(projectsRepository.save(project));
  }

  public List<ProjectDTO> getAllProjects() {
    return projectsRepository.findAll().stream().map(this::toDTO).toList();
  }

  public Project getProjectById(Long id) {
    return projectsRepository
      .findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Project not found"));
  }

  public ProjectDTO getProjectByIdToDTO(Long id) {
    Project project = getProjectById(id);
    return toDTO(project);
  }

  public void updateProject(Long id, ProjectDTO request) {
    Project project = getProjectById(id);
    project.setName(request.getName());
    project.setDescription(request.getDescription());
    project.setOwner(request.getOwner());
    project.setTeamMembers(request.getTeamMembers());
    projectsRepository.save(project);
  }

  public void deleteProject(Long id) {
    Project project = getProjectById(id);
    projectsRepository.delete(project);
  }

  private ProjectDTO toDTO(Project project) {
    ProjectDTO dto = new ProjectDTO();
    dto.setId(project.getId());
    dto.setName(project.getName());
    dto.setDescription(project.getDescription());
    dto.setOwner(project.getOwner());
    dto.setTeamMembers(project.getTeamMembers());
    return dto;
  }
}
