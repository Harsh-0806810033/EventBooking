package com.example.eventmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
	private Long eventId;       // hidden field from form
    private String name;        // attendee name
    private String email;       // optional
    private String phoneNumber;
}
