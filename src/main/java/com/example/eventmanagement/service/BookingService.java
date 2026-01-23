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
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    //private final SmsService smsService;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository,
			EventRepository eventRepository, EmailService emailService/* , SmsService smsService */) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.emailService = emailService;
        //this.smsService = smsService;
    }

    /**
     * Book a seat for an event (First-Come-First-Serve)
     */
    public Booking bookSeat(BookingRequest request) {

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        // Get last seat number for this event
        int nextSeatNumber = bookingRepository
                .findTopByEventIdOrderBySeatNumberDesc(request.getEventId())
                .map(b -> b.getSeatNumber() + 1)
                .orElse(1);

        Booking booking = new Booking();
        booking.setAttendeeName(request.getName());
        booking.setAttendeeEmail(request.getEmail());
        booking.setSeatNumber(nextSeatNumber);
        booking.setAttendeePhone(request.getPhoneNumber());
        booking.setEvent(event);

        // Save booking
        //bookingRepository.save(booking);

        // Update available seats
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        event.getBookings().add(booking);
        eventRepository.save(event);
        
     // Build notification message
        String message = "Booking confirmed! Seat #" + booking.getSeatNumber() +
                         " for event: " + event.getName();
        
        // Send notifications based on provided info
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            //emailService.sendSeatConfirmation(request.getEmail(), event.getName(), nextSeatNumber);
        	System.out.println("Send Email Notification");
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            //smsService.sendSms(request.getPhoneNumber(), message);
        	System.out.println("Send notification to user phone: "+request.getPhoneNumber());
        }
        
        return booking;
    }

    /**
     * Get all bookings for an event
     */
    public List<Booking> getBookingsForEvent(Long eventId) {
        return bookingRepository.findByEventId(eventId);
    }

    /**
     * Cancel a booking and free the seat
     */
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Event event = booking.getEvent();

        // Delete booking
        bookingRepository.delete(booking);

        // Increase available seats
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);
    }
}