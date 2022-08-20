package com.musala.drones.repositories;

import com.musala.drones.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    public List<Medication> findByDroneSerialNumber( String droneSerialNumber);

    public void deleteByDroneSerialNumber(String droneSerialNumber);
}
