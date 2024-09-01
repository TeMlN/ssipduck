package com.server.ssipduck.domain.anime.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity @Table(name = "anime")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Anime {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
