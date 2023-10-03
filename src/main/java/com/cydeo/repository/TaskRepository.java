package com.cydeo.repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.servlet.tags.form.SelectTag;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectCode = ?1 AND t.taskStatus <> 'COMPLETE'")
    int totalNonCompletedTask(String projectCode);

    @Query(value = "SELECT COUNT(*) " +
            "FROM tasks t JOIN projects p on t.project_id=p.id " +
            "WHERE p.project_code=?1 AND t.task_status='COMPLETE'",nativeQuery = true)
    int totalCompletedTask(String projectCode);

    List<Task> findAllByProject(Project project);

    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);

    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);


}
