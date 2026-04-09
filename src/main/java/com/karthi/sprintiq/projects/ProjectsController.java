package com.karthi.sprintiq.projects;

import com.karthi.sprintiq.projects.dto.ProjectDTO;
import com.karthi.sprintiq.projects.dto.SectionCreateDTO;
import com.karthi.sprintiq.projects.dto.SectionDTO;
import com.karthi.sprintiq.tickets.dto.TicketDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectsController {

  private final ProjectsService projectsService;

  @PostMapping
  public ProjectDTO createProject(@RequestBody ProjectDTO request) {
    return projectsService.createProject(request);
  }

  @GetMapping
  public List<ProjectDTO> getAllProjects() {
    return projectsService.getAllProjects();
  }

  @GetMapping("/{id}")
  public ProjectDTO getProjectById(@PathVariable Long id) {
    return projectsService.getProjectByIdToDTO(id);
  }

  @PutMapping("/{id}")
  public void updateProject(
    @PathVariable Long id,
    @RequestBody ProjectDTO request
  ) {
    projectsService.updateProject(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteProject(@PathVariable Long id) {
    projectsService.deleteProject(id);
  }

  @PostMapping("/{projectId}/sections")
  public SectionCreateDTO createSection(
    @PathVariable Long projectId,
    @RequestBody SectionCreateDTO sectionCreateDTO
  ) {
    return projectsService.createSection(projectId, sectionCreateDTO);
  }

  @GetMapping("/{projectId}/sections")
  public List<SectionDTO> getSections(@PathVariable Long projectId) {
    return projectsService.getSections(projectId);
  }

  @PostMapping("/{sectionId}/tickets")
  public TicketDTO addTicketToSection(
    @PathVariable Long sectionId,
    @RequestBody TicketDTO ticket
  ) {
    return projectsService.addTicketToSection(sectionId, ticket);
  }
}
