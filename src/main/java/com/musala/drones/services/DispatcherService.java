package com.musala.drones.services;

import com.musala.drones.exception.DispatcherException;
import com.musala.drones.model.Drone;
import com.musala.drones.model.Medication;
import com.musala.drones.repositories.DroneRepository;
import com.musala.drones.repositories.MedicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

        if (droneRepository.count() == 10) {
            throw new DispatcherException("The Dispatch  service can not hold a fleet with more than 10 drones");
        }

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
    public List<Drone> fetchAllDrones() {

        List<Drone> drones = droneRepository.findAll();

        return drones;
    }

    public List<Drone> fetchAvailableDrones() {

        List<Drone> drones = droneRepository.findByState("LOADING");

        return drones;
    }

    public Drone fetchDroneBySerialNumber(String serialNumber) {

        Drone drone = droneRepository.findById(serialNumber).orElse(null);

        if (drone == null) {
            throw new DispatcherException("Drone with Serial Number: " + serialNumber + " does not exist!\n"
                    + "\nMedication must be loaded unto existing drone specified by serial number");
        }

        return drone;
    }

    public void loadMedication( String droneSerialNumber, Medication medicationRequest, MultipartFile image) {

        Drone drone = droneRepository.findById(droneSerialNumber).orElse(null);

        if (drone == null) {
            throw new DispatcherException("Drone with the Serial Number: " + droneSerialNumber + " does not exist!\n"
                    + "\nMedication must be loaded unto existing drone specified by serial number");
        }

        if (!drone.getState().equals("LOADING")) {
            throw new DispatcherException("Drone cannot be loaded because it as a state of loading\n"
                    + "\nYou can prepare a drone for loading if it is IDLE by changing the state to loading.");
        }

        if ((drone.getWeight() + medicationRequest.getWeight()) > 500) {

            throw new DispatcherException("Drone cannot be loaded with more than 500gr.");
        }

        Medication medication = new Medication();

        BeanUtils.copyProperties(medicationRequest, medication);

        String extension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."), image.getOriginalFilename().length());

        String imageUrl = storageService.store(image, "drone/medication_image",
                droneSerialNumber + (int) (Math.random() * 10000) % 1000 + extension);

        medication.setImage(imageUrl);

        medication.setDroneSerialNumber(droneSerialNumber);

        medicationRepository.save(medication);

        drone.setWeight(drone.getWeight() + medication.getWeight());

        droneRepository.save(drone);

    }

    public List<Medication> fetchDroneLoad(String droneSerialNumber) {

        Boolean droneExists = droneRepository.existsById(droneSerialNumber);

        if (!droneExists) {
            throw new DispatcherException("Drone with Serial Number: " + droneSerialNumber + " does not exist!");
        }

        List<Medication> medications = medicationRepository.findByDroneSerialNumber(droneSerialNumber);

        return medications;
    }

    public Drone nextDroneState(String serialNumber) {

        Drone drone = droneRepository.findById(serialNumber).orElse(null);

        if (drone == null) {
            throw new DispatcherException("Drone with Serial Number: " + serialNumber + " does not exist!");
        }

        switch (drone.getState()) {
            case "IDLE":
                drone.setState("LOADING");
                break;
            case "LOADING":
                drone.setState("LOADED");
                break;
            case "LOADED":
                drone.setState("DELIVERING");
                break;
            case "DELIVERING":
                drone.setState("DELIVERED");
                break;
            case "DELIVERED":
                drone.setState("RETURNING");
                List<Medication> medications = medicationRepository.findByDroneSerialNumber(serialNumber);
                medications.forEach((medication) -> {
                    storageService.deleteOne(medication.getImage());
                });
                medicationRepository.deleteByDroneSerialNumber(serialNumber);
                break;
            case "RETURNING":
                drone.setState("IDLE");
                break;
        }

        Drone savedDrone = droneRepository.save(drone);

        return savedDrone;
    }
}
