package com.musala.drones.controller;

import com.musala.drones.dto.Response;
import com.musala.drones.model.Drone;
import com.musala.drones.model.Medication;
import com.musala.drones.services.DispatcherService;
import com.musala.drones.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/drone")
@CrossOrigin(origins = "*")
public class DispatcherController {

    @Autowired
    DispatcherService droneService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/fetchdrones")
    public ResponseEntity<?> fetchDrones() {

        List result = droneService.fetchAllDrones();

        Response response = new Response(true, "Drones retrieved successfully", result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<?> fetchAvailableDrones() {

        List result = droneService.fetchAvailableDrones();

        Response response = new Response(true, "Available drones retrieved successfully", result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<?> fetchDrone(@PathVariable String serialNumber) {

        Drone result = droneService.fetchDroneBySerialNumber(serialNumber);

        Response response = new Response(true, "Drone retieved successfully", result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serialNumber}/load")
    public ResponseEntity<?> fetchDroneLoad(@PathVariable String serialNumber) {

        List<Medication> result = droneService.fetchDroneLoad(serialNumber);

        Response response = new Response(true, "Drone medication load retrieved successfully", result);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response> registerDrones(@Valid @RequestBody Drone droneRequest) {

        Drone result = droneService.registerDrone(droneRequest);

        Response response = new Response(true, "Drone registered successfully", result);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/medication_image/{image}")
    public ResponseEntity<?> loadMedicationImage(@PathVariable String image) {

        Resource resource = storageService.loadAsResource("drone/medication_image/" + image);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<?> loadMedication(@PathVariable String serialNumber, @Valid @RequestPart Medication medication, @RequestPart MultipartFile image) {

        droneService.loadMedication(serialNumber, medication, image);

        Response response = new Response(true, "Medication loaded successfully", null);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{serialNumber}/next-state")
    public ResponseEntity<?> changeDroneState(@PathVariable String serialNumber) {

        Drone result = droneService.nextDroneState(serialNumber);

        Response response = new Response(true, "Drone state changed successfully", result);

        return ResponseEntity.ok(response);
    }
}
