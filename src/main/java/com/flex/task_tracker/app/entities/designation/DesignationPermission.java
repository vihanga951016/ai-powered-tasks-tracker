package com.flex.task_tracker.app.entities.designation;

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
@Table(name = "designation_permissions")
public class DesignationPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "designationId")
    private Designation designation;
    @ManyToOne
    @JoinColumn(name = "permissionId")
    private Permission permission;
}
