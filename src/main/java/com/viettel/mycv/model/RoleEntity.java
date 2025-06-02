package com.viettel.mycv.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name="role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @OneToMany(mappedBy="role", fetch = FetchType.LAZY)
    private Set<UserHasRole> userHasRoles = new HashSet<>();
}
