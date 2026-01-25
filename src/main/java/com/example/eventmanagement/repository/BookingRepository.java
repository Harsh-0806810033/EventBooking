package com.example.eventmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.eventmanagement.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByEventId(Long eventId);

    Optional<Booking> findTopByEventIdOrderBySeatNumberDesc(Long eventId);
    
    @Query("""
    	    SELECT b FROM Booking b
    	    WHERE (:keyword IS NULL OR
    	          LOWER(b.event.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    	          LOWER(b.attendeeName) LIKE LOWER(CONCAT('%', :keyword, '%')))
    	      AND (:email IS NULL OR LOWER(b.attendeeEmail) LIKE LOWER(CONCAT('%', :email, '%')))
    	      AND (:seatNumber IS NULL OR b.seatNumber = :seatNumber)
    	""")
    	List<Booking> search(
    	        @Param("keyword") String keyword,
    	        @Param("email") String email,
    	        @Param("seatNumber") Integer seatNumber);
    
    @Query("""
    		SELECT b FROM Booking b
    		WHERE b.event.id = :eventId
    		AND (:keyword IS NULL OR LOWER(b.attendeeName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		     OR LOWER(b.event.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
    		AND (:email IS NULL OR LOWER(b.attendeeEmail) LIKE LOWER(CONCAT('%', :email, '%')))
    		AND (:seatNumber IS NULL OR b.seatNumber = :seatNumber)
    		""")
    		List<Booking> searchByEvent(
    		        @Param("eventId") Long eventId,
    		        @Param("keyword") String keyword,
    		        @Param("email") String email,
    		        @Param("seatNumber") Integer seatNumber);

}
