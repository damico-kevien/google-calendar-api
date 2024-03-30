package com.google.calendar.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleReminderOverride {
    private String method;
    private int minutes;
}
