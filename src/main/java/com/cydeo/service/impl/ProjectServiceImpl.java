package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, TaskService taskService, UserMapper userMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.taskService = taskService;
        this.userMapper = userMapper;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);

        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
    projectRepository.save(projectMapper.convertToEntity(dto));
    }

    @Override
    public void update(ProjectDTO dto) {
        // Find current project
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());

        // Map update project dto to entity object
        Project convertedProject = projectMapper.convertToEntity(dto);

        // Set id to the converted object
        convertedProject.setId(project.getId());

        // Save the updated user in the db
        convertedProject.setProjectStatus(project.getProjectStatus());

        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() + "-" + project.getId());
        //harold@manager.com-1 because of the change of the userName after deletion if the same user want to use same username just like "harold@manager.com" he/she can use without a problem.

        projectRepository.save(project);

        // Explain the code: why we use the projectMapper inside the taskService?
        // Here we just deleted the tasks that related with project.
        taskService.deleteByProject(projectMapper.convertToDto(project));
    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        // Here we just make tasks status to "complete" that related with project.
        taskService.completeByProject(projectMapper.convertToDto(project));

    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        UserDTO currentUserDto = userService.findByUserName("harold@manager.com");
        User user = userMapper.convertToEntity(currentUserDto);
        //hey DB, give me the all projects assigned to manager login in the system
        List<Project> list= projectRepository.findAllByAssignedManager(user);
        return list.stream().map(project -> {
                ProjectDTO obj= projectMapper.convertToDto(project);
                obj.setCompleteTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
                obj.setUnfinishedTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
        return obj;
    }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {
        User user = userMapper.convertToEntity(assignedManager);
        List<Project> projects = projectRepository.findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE, user);
        return projects.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }


}
