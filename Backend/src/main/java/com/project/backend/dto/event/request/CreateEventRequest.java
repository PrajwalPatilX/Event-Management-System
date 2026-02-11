package com.project.backend.dto.event.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private Long organizationId;
}
