package com.viettel.mycv.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="user_has_role")
public class UserHasRole {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable=false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
}
