package com.project.backend.dto.team.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {

    private Long id;
    private String name;
    private String joinCode;
    private Long eventId;
}

