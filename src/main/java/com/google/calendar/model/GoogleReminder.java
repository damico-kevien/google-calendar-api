package com.google.calendar.model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleReminder {

    private boolean useDefault;
    private List<GoogleReminderOverride> overrides;
}
