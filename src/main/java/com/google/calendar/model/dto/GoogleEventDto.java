package com.google.calendar.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.calendar.model.GoogleEventDate;
import com.google.calendar.model.GoogleReminder;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleEventDto {

    private String summary;
    private String location;
    private String description;
    private GoogleEventDate start;
    private GoogleEventDate end;
    private GoogleReminder reminders;
}
