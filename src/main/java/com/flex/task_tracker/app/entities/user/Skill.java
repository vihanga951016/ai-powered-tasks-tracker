package com.flex.task_tracker.app.entities.user;

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
@Table(
        name = "skills",
        indexes = {
                @Index(name = "idx_s_skill", columnList = "skill")
        }
)
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String skill;
    private boolean deleted;
}
