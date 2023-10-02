package com.cydeo.entity;


import com.cydeo.enums.Status;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tasks")
@Where(clause = "is_deleted=false")
public class Task extends BaseEntity{

        private String taskSubject;

        private String taskDetail;

        @Enumerated(EnumType.STRING)
        private Status taskStatus;

        @Column(columnDefinition = "DATE")
        private LocalDate assignedDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assigned_employee_id")
        private User assignedEmployee;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "project_id")
        private Project project;
    }


