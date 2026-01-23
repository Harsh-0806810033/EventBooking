package com.example.eventmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eventmanagement.dto.BookingRequest;
import com.example.eventmanagement.entities.Booking;
import com.example.eventmanagement.entities.Event;
import com.example.eventmanagement.repository.BookingRepository;
import com.example.eventmanagement.repository.EventRepository;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public EventService(EventRepository eventRepository, BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Optional: delete all bookings related to this event
        //bookingRepository.deleteByEvent(event);

        eventRepository.delete(event);
    }
}