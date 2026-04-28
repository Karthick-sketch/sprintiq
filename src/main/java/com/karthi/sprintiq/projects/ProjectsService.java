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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProjectsService {

  private final ProjectsRepository projectsRepository;
  private final SectionRepository sectionRepository;
  private final UserService userService;

  // -------- Projects ------------------------------------------------------
  public ProjectDTO createProject(ProjectDTO request) {
    Project project = new Project();
    project.setTitle(request.getTitle());
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
    return toProjectDTO(getProjectById(id));
  }

  public void updateProject(Long id, ProjectDTO request) {
    Project project = getProjectById(id);
    project.setTitle(request.getTitle());
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
    return ProjectDTO.builder()
      .id(project.getId())
      .title(project.getTitle())
      .description(project.getDescription())
      .ownerId(project.getOwner().getId())
      .teamMemberIds(
        project
          .getTeamMembers()
          .stream()
          .map(projectUser -> projectUser.getUser().getId())
          .toList()
      )
      .build();
  }

  private ProjectUser toProjectUser(Project project, User user) {
    return ProjectUser.builder().project(project).user(user).build();
  }

  // -------- Project Sections ------------------------------------------------
  public SectionDTO createSection(
    Long projectId,
    SectionCreateDTO sectionCreateDTO
  ) {
    Section section = new Section();
    section.setTitle(sectionCreateDTO.getTitle());
    section.setProject(getProjectById(projectId));
    section.setOrderIndex(sectionCreateDTO.getOrderIndex());
    return toSectionDTO(sectionRepository.save(section));
  }

  public List<SectionDTO> getSections(long projectId) {
    try {
      return sectionRepository
        .findByProjectId(projectId)
        .stream()
        .sorted(Comparator.comparing(Section::getOrderIndex))
        .map(this::toSectionDTO)
        .toList();
    } catch (Exception e) {
      log.error("Error fetching sections for project {}", projectId, e);
      return Collections.emptyList();
    }
  }

  public Section getSectionById(Long sectionId) {
    if (sectionId == null) {
      return null;
    }
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
    return SectionDTO.builder()
      .id(section.getId())
      .title(section.getTitle())
      .projectId(section.getProject().getId())
      .tickets(
        Optional.ofNullable(section.getTickets())
          .map(tickets ->
            tickets
              .stream()
              .sorted(Comparator.comparing(Ticket::getOrderIndex))
              .map(this::toProjectSectionTicketDTO)
              .toList()
          )
          .orElse(Collections.emptyList())
      )
      .orderIndex(section.getOrderIndex())
      .build();
  }

  private ProjectSectionTicketDTO toProjectSectionTicketDTO(Ticket ticket) {
    return ProjectSectionTicketDTO.builder()
      .id(ticket.getId())
      .title(ticket.getTitle())
      .status(ticket.getStatus())
      .priority(ticket.getPriority())
      .assignee(userService.toDTO(ticket.getAssignee()))
      .dueDate(ticket.getDueDate())
      .sectionId(
        ticket.getSection() != null ? ticket.getSection().getId() : null
      )
      .orderIndex(ticket.getOrderIndex())
      .build();
  }

  private Ticket toTicket(TicketDTO dto) {
    return Ticket.builder()
      .id(dto.getId())
      .title(dto.getTitle())
      .description(dto.getDescription())
      .status(dto.getStatus())
      .priority(dto.getPriority())
      .dueDate(dto.getDueDate())
      .assignee(
        dto.getAssignee() != null
          ? userService.getUserById(dto.getAssignee().getId())
          : null
      )
      .section(getSectionById(dto.getSectionId()))
      .orderIndex(dto.getOrderIndex())
      .build();
  }

  public ProjectTicketListDTO toProjectTicketListDTO(Project project) {
    return ProjectTicketListDTO.builder()
      .id(project.getId())
      .title(project.getTitle())
      .build();
  }
}
