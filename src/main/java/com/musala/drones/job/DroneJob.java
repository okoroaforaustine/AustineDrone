package com.musala.drones.job;

import com.musala.drones.model.Drone;
import com.musala.drones.services.DispatcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DroneJob {

    @Autowired
    DispatcherService droneService;

    @Scheduled(fixedRate = 10000)
    public void doCheckBatteryLevels() {

        List<Drone> drones = droneService.fetchAllDrones();

        if (drones.isEmpty()) {

            return;
        }

        drones.forEach((drone) -> {
            StringBuilder data = new StringBuilder();

            data.append("Drone(");
            data.append(drone.getSerialNumber());
            data.append(") battery level is at ");
            data.append(drone.getBatteryLevel());
            data.append("%.");

            log.info(data.toString());

            data = null;
        });

    }
}
