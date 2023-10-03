package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
//        task.ifPresent(taskMapper::convertToDto);
        if (task.isPresent()){
            return  taskMapper.convertToDto(task.get());
        }
        return null;
    }

    @Override
    public void save(TaskDTO dto) {
    dto.setTaskStatus(Status.OPEN);
    dto.setAssignedDate(LocalDate.now());
    taskRepository.save(taskMapper.convertToEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            task.get().setIsDeleted(true);
            taskRepository.save(task.get());
        }
    }

    @Override
    public void update(TaskDTO dto) {

        Optional<Task> task = taskRepository.findById(dto.getId());
        Task converter = taskMapper.convertToEntity(dto);

        if (task.isPresent()){
            converter.setTaskStatus(dto.getTaskStatus() == null ? task.get().getTaskStatus() : dto.getTaskStatus()); // this one related wit competeByProject methods
            converter.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(converter);
        }

    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTask(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTask(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        Project project = projectMapper.convertToEntity(projectDTO);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.forEach(task -> delete(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        Project project = projectMapper.convertToEntity(projectDTO);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.stream().map(taskMapper::convertToDto).forEach(taskDTO -> {
        taskDTO.setTaskStatus(Status.COMPLETE);
        update(taskDTO);
        });
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        User loggedInUser = userMapper.convertToEntity(userService.findByUserName("john@employee.com"));
        List<Task> tasks = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,loggedInUser);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        User loggedInUser = userMapper.convertToEntity(userService.findByUserName("john@employee.com"));
        List<Task> tasks = taskRepository.findAllByTaskStatusAndAssignedEmployee(status,loggedInUser);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());    }
}