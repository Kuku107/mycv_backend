package com.viettel.mycv.model;

import com.viettel.mycv.common.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tbl_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name="password", nullable = false, length = 255)
    private String password;

    @Column(name = "user_status", nullable = false, length=50)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name="created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false, length = 255)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
