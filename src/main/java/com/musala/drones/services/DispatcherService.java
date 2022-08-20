package com.musala.drones.services;

import com.musala.drones.exception.DispatcherException;
import com.musala.drones.model.Drone;
import com.musala.drones.repositories.DroneRepository;
import com.musala.drones.repositories.MedicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DispatcherService {
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private StorageService storageService;

    public Drone registerDrone( Drone droneRequest) {


        if (droneRepository.existsById(droneRequest.getSerialNumber())) {
            throw new DispatcherException("Dispatch service  already has drone with Serial number: " + droneRequest.getSerialNumber());
        }

        Drone drone = new Drone();

        if ((droneRequest.getBatteryLevel() < 25) || droneRequest.getState().equals("IDLE")) {
            drone.setState("IDLE");
        } else {
            drone.setState("LOADING");
        }

        drone.setSerialNumber(droneRequest.getSerialNumber());

        drone.setModel(droneRequest.getModel());

        drone.setBatteryLevel(droneRequest.getBatteryLevel());

        Drone savedDrone = droneRepository.save(drone);

        return savedDrone;
    }
}
