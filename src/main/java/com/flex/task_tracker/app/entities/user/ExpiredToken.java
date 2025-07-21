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
@Table(name = "expired_tokens", indexes = {
        @Index(name = "idx_user_id", columnList = "userId")
})
public class ExpiredToken {

    @Id
    @Column(length = 600)
    private String id;
    private Integer userId;

}
