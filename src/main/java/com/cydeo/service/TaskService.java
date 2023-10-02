package com.cydeo.service;

import com.cydeo.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> listAllTasks();
    TaskDTO certainTask(Long id);
    void save(TaskDTO dto);
    void delete(Long id);
    void update(TaskDTO dto);
}
