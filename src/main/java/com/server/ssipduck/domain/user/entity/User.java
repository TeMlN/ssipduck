package com.server.ssipduck.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity @Table(name = "user")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "auth_id", unique = true)
    private Long authId;

    @Column(name = "profile_image")
    private String profileImage;
}
