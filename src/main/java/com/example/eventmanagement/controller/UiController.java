package com.example.eventmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagement.dto.BookingRequest;
import com.example.eventmanagement.entities.Booking;
import com.example.eventmanagement.entities.Event;
import com.example.eventmanagement.repository.BookingRepository;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.service.BookingService;
import com.example.eventmanagement.service.EventService;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class UiController {

    private final EventRepository eventRepo;
    private final BookingRepository bookingRepo;
    private final BookingService bookingService;
    private final EventService eventService;
    
    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("events", eventRepo.findAll());
        return "events";
    }

    @PostMapping("/events")
    public String createEvent(Event event) {
        event.setAvailableSeats(event.getTotalSeats());
        eventRepo.save(event);
        return "redirect:/events";
    }

    @GetMapping("/book/{eventId}")
    public String bookPage(@PathVariable Long eventId, Model model) {
        model.addAttribute("event", eventRepo.findById(eventId).orElseThrow());
        return "book-seat";
    }

    @PostMapping("/book")
    public String bookSeat(@ModelAttribute BookingRequest bookingRequest,
                           RedirectAttributes ra) {
        try {
            Booking booking = bookingService.bookSeat(bookingRequest);
            ra.addFlashAttribute("msg",
                "Seat booked successfully. Seat No: " + booking.getSeatNumber());
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingRepo.findAll());
        return "bookings";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/bookings";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/events/{id}/bookings")
    public String viewEventBookings(@PathVariable Long id, Model model) {
        Event event = eventRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        model.addAttribute("event", event);
        model.addAttribute("bookings", bookingService.getBookingsForEvent(id));
        return "event-bookings";
    }
    
    @PostMapping("/booking/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/events";
    }
    
    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes ra) {
        try {
            eventService.deleteEvent(id);
            ra.addFlashAttribute("msg", "Event deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error deleting event: " + e.getMessage());
        }
        return "redirect:/events";
    }

}

