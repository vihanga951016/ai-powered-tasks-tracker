package com.flex.task_tracker.app.entities.designation.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignPermissions {

    private Integer designationId;
    private List<Integer> permissions;
}
