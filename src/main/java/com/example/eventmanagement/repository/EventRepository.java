package com.example.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventmanagement.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	 List<Event> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String description, String location);
}
