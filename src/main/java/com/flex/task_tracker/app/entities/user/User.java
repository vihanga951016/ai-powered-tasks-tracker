package com.flex.task_tracker.app.entities.user;

import com.flex.task_tracker.app.entities.designation.Designation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToOne
    @JoinColumn(name = "designationId")
    private Designation designation;
    private String role;
    private boolean restricted;
    private boolean deleted;

}
