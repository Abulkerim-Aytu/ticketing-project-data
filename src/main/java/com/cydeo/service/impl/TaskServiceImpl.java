package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
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
            converter.setAssignedDate(task.get().getAssignedDate());
            converter.setTaskStatus(task.get().getTaskStatus());
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
}
