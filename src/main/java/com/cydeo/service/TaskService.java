package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {
    List<TaskDTO> listAllTasks();
    TaskDTO findById(Long id);
    void save(TaskDTO dto);
    void delete(Long id);
    void update(TaskDTO dto);

    int totalNonCompletedTask(String projectCode);

    int totalCompletedTask(String projectCode);

    void deleteByProject(ProjectDTO projectDTO);

    void completeByProject(ProjectDTO projectDTO);

    List<TaskDTO>listAllTasksByStatusIsNot(Status status);

    List<TaskDTO>listAllTasksByStatus(Status status);

    List<TaskDTO>listAllNonCompetedByAssignedEmployee(UserDTO assignedEmployee);
}
