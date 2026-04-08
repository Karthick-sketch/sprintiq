package com.karthi.sprintiq.projects;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.projects.dto.ProjectDTO;
import com.karthi.sprintiq.projects.dto.SectionCreateDTO;
import com.karthi.sprintiq.projects.entity.Project;
import com.karthi.sprintiq.projects.entity.ProjectUser;
import com.karthi.sprintiq.projects.entity.Section;
import com.karthi.sprintiq.projects.repository.ProjectsRepository;
import com.karthi.sprintiq.projects.repository.SectionRepository;
import com.karthi.sprintiq.user.UserService;
import com.karthi.sprintiq.user.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectsService {

  private final ProjectsRepository projectsRepository;
  private final SectionRepository sectionRepository;
  private final UserService userService;

  // -------- Projects ------------------------------------------------------
  public ProjectDTO createProject(ProjectDTO request) {
    Project project = new Project();
    project.setName(request.getName());
    project.setDescription(request.getDescription());
    project.setOwner(userService.getUserById(request.getOwnerId()));
    project.setTeamMembers(
      request
        .getTeamMemberIds()
        .stream()
        .map(userId -> toProjectUser(project, userService.getUserById(userId)))
        .toList()
    );
    return toProjectDTO(projectsRepository.save(project));
  }

  public List<ProjectDTO> getAllProjects() {
    return projectsRepository
      .findAll()
      .stream()
      .map(this::toProjectDTO)
      .toList();
  }

  public Project getProjectById(Long id) {
    return projectsRepository
      .findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Project not found"));
  }

  public ProjectDTO getProjectByIdToDTO(Long id) {
    Project project = getProjectById(id);
    return toProjectDTO(project);
  }

  public void updateProject(Long id, ProjectDTO request) {
    Project project = getProjectById(id);
    project.setName(request.getName());
    project.setDescription(request.getDescription());
    project.setOwner(userService.getUserById(request.getOwnerId()));
    project.setTeamMembers(
      request
        .getTeamMemberIds()
        .stream()
        .map(userId -> toProjectUser(project, userService.getUserById(userId)))
        .toList()
    );
    projectsRepository.save(project);
  }

  public void deleteProject(Long id) {
    Project project = getProjectById(id);
    projectsRepository.delete(project);
  }

  private ProjectDTO toProjectDTO(Project project) {
    ProjectDTO dto = new ProjectDTO();
    dto.setId(project.getId());
    dto.setName(project.getName());
    dto.setDescription(project.getDescription());
    dto.setOwnerId(project.getOwner().getId());
    dto.setTeamMemberIds(
      project
        .getTeamMembers()
        .stream()
        .map(projectUser -> projectUser.getUser().getId())
        .toList()
    );
    return dto;
  }

  private ProjectUser toProjectUser(Project project, User user) {
    ProjectUser projectUser = new ProjectUser();
    projectUser.setProject(project);
    projectUser.setUser(user);
    return projectUser;
  }

  // -------- Project Sections ------------------------------------------------
  public SectionCreateDTO createSection(
    Long projectId,
    SectionCreateDTO sectionCreateDTO
  ) {
    Section section = new Section();
    section.setName(sectionCreateDTO.getName());
    section.setProject(getProjectById(projectId));
    sectionRepository.save(section);
    return sectionCreateDTO;
  }

  public Section getSectionById(Long sectionId) {
    return sectionRepository
      .findById(sectionId)
      .orElseThrow(() ->
        new EntityNotFoundException("Project section not found")
      );
  }

  public List<Section> getSectionsByProjectId(Long projectId) {
    return sectionRepository.findByProjectId(projectId);
  }
}
