package com.example.eventmanagement.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagement.dto.BookingRequest;
import com.example.eventmanagement.entities.Booking;
import com.example.eventmanagement.entities.Event;
import com.example.eventmanagement.repository.BookingRepository;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.service.BookingService;
import com.example.eventmanagement.service.EventService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UiController {

    private final EventRepository eventRepo;
    private final BookingRepository bookingRepo;
    private final BookingService bookingService;
    private final EventService eventService;

    // =============================
    // VIEW EVENTS PAGE
    // =============================
    @GetMapping("/events")
    public String events(
            @RequestParam(required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("events",
                    eventService.searchEvents(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("events", eventRepo.findAll());
        }

        return "events";
    }


    // =============================
    // CREATE EVENT PAGE
    // =============================
    @GetMapping("/events/create")
    public String createEventPage() {
        return "create-event";
    }

    // =============================
    // CREATE EVENT ACTION
    // =============================
    @PostMapping("/events")
    public String createEvent(@ModelAttribute Event event,
                              RedirectAttributes ra) {

        event.setAvailableSeats(event.getTotalSeats());
        eventRepo.save(event);

        ra.addFlashAttribute("msg", "Event created successfully!");
        return "redirect:/events";
    }

    // =============================
    // BOOK SEAT
    // =============================
    @GetMapping("/book/{eventId}")
    public String bookPage(@PathVariable Long eventId, Model model) {
        model.addAttribute("event",
                eventRepo.findById(eventId)
                         .orElseThrow(() -> new RuntimeException("Event not found")));
        return "book-seat";
    }

    @PostMapping("/book")
    public String bookSeat(@ModelAttribute BookingRequest bookingRequest,
                           RedirectAttributes ra) {
        try {
            Booking booking = bookingService.bookSeat(bookingRequest);
            ra.addFlashAttribute(
                "msg",
                "Seat booked successfully. Seat No: " + booking.getSeatNumber()
            );
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events";
    }

    // =============================
    // VIEW ALL BOOKINGS
    // =============================
    @GetMapping("/bookings")
    public String bookings(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer seatNumber,
            Model model) {

        model.addAttribute("bookings",
                bookingService.searchBookings(keyword, email, seatNumber));

        model.addAttribute("keyword", keyword);
        model.addAttribute("email", email);
        model.addAttribute("seatNumber", seatNumber);

        return "bookings";
    }


    // =============================
    // VIEW BOOKINGS PER EVENT
    // =============================
    @GetMapping("/events/{id}/bookings")
    public String viewEventBookings(@PathVariable Long id, Model model) {

        Event event = eventRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        model.addAttribute("event", event);
        model.addAttribute("bookings",
                bookingService.getBookingsForEvent(id));

        return "event-bookings";
    }

    // =============================
    // CANCEL BOOKING
    // =============================
    @PostMapping("/booking/cancel/{id}")
    public String cancelBooking(
            @PathVariable Long id,
            @RequestParam Long eventId,
            RedirectAttributes ra) {

        bookingService.cancelBooking(id);
        ra.addFlashAttribute("msg", "Booking cancelled successfully");

        return "redirect:/events/" + eventId + "/bookings";
    }


    // =============================
    // DELETE EVENT
    // =============================
    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id,
                              RedirectAttributes ra) {
        try {
            eventService.deleteEvent(id);
            ra.addFlashAttribute("msg", "Event deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute(
                "error",
                "Error deleting event: " + e.getMessage()
            );
        }
        return "redirect:/events";
    }

    // =============================
    // LOGIN PAGE
    // =============================
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/bookings/export")
    public void exportBookingsToExcel(
            @RequestParam Long eventId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer seatNumber,
            HttpServletResponse response) throws IOException {

        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
            "Content-Disposition", "attachment; filename=bookings.xlsx");

        List<Booking> bookings =
                bookingService.searchBookingsEventId(eventId, keyword, email, seatNumber);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bookings");

        // Header row
        Row header = sheet.createRow(0);
        String[] columns = {
                "Event Name",
                "Person Name",
                "Email",
                "Seat Number",
                "Booked On"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Data rows
        int rowNum = 1;
        for (Booking booking : bookings) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(booking.getEvent().getName());
            row.createCell(1).setCellValue(booking.getAttendeeName());
            row.createCell(2).setCellValue(booking.getAttendeeEmail());
            row.createCell(3).setCellValue(booking.getSeatNumber());
            row.createCell(4).setCellValue(
                    booking.getBookingTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' hh:mm a")));
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
