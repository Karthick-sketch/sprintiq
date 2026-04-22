package com.karthi.sprintiq.projects;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.projects.dto.ProjectDTO;
import com.karthi.sprintiq.projects.dto.ProjectSectionTicketDTO;
import com.karthi.sprintiq.projects.dto.ProjectTicketListDTO;
import com.karthi.sprintiq.projects.dto.SectionCreateDTO;
import com.karthi.sprintiq.projects.dto.SectionDTO;
import com.karthi.sprintiq.projects.entity.Project;
import com.karthi.sprintiq.projects.entity.ProjectUser;
import com.karthi.sprintiq.projects.entity.Section;
import com.karthi.sprintiq.projects.repository.ProjectsRepository;
import com.karthi.sprintiq.projects.repository.SectionRepository;
import com.karthi.sprintiq.tickets.dto.TicketDTO;
import com.karthi.sprintiq.tickets.entity.Ticket;
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

  public List<SectionDTO> getSections(Long projectId) {
    return sectionRepository
      .findByProjectId(projectId)
      .stream()
      .map(this::toSectionDTO)
      .toList();
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

  public TicketDTO addTicketToSection(Long sectionId, TicketDTO dto) {
    Section section = getSectionById(sectionId);
    Ticket ticket = toTicket(dto);
    ticket.setSection(section);
    section.getTickets().add(ticket);
    sectionRepository.save(section);
    dto.setId(ticket.getId());
    return dto;
  }

  private SectionDTO toSectionDTO(Section section) {
    SectionDTO dto = new SectionDTO();
    dto.setId(section.getId());
    dto.setName(section.getName());
    dto.setTickets(
      section
        .getTickets()
        .stream()
        .map(this::toProjectSectionTicketDTO)
        .toList()
    );
    return dto;
  }

  private ProjectSectionTicketDTO toProjectSectionTicketDTO(Ticket ticket) {
    ProjectSectionTicketDTO dto = new ProjectSectionTicketDTO();
    dto.setId(ticket.getId());
    dto.setTitle(ticket.getTitle());
    dto.setStatus(ticket.getStatus());
    dto.setPriority(ticket.getPriority());
    dto.setAssignee(userService.toDTO(ticket.getAssignee()));
    dto.setDueDate(ticket.getDueDate());
    dto.setSectionId(ticket.getSection().getId());
    return dto;
  }

  private Ticket toTicket(TicketDTO dto) {
    Ticket ticket = new Ticket();
    ticket.setId(dto.getId());
    ticket.setTitle(dto.getTitle());
    ticket.setDescription(dto.getDescription());
    ticket.setStatus(dto.getStatus());
    ticket.setPriority(dto.getPriority());
    ticket.setDueDate(dto.getDueDate());
    ticket.setAssignee(userService.getUserById(dto.getAssigneeId()));
    ticket.setSection(getSectionById(dto.getSectionId()));
    return ticket;
  }

  public ProjectTicketListDTO toProjectTicketListDTO(Project project) {
    ProjectTicketListDTO dto = new ProjectTicketListDTO();
    dto.setId(project.getId());
    dto.setName(project.getName());
    return dto;
  }
}
