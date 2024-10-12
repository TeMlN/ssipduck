package com.server.ssipduck.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserIdDto {
    private Long id;

    public void create(Long id) {
        this.id = id;
    }
}
