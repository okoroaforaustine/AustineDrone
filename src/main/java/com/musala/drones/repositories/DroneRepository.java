package com.musala.drones.repositories;

import com.musala.drones.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, String> {
    public List<Drone> findByState( String state);

}
