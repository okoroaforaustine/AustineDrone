package com.musala.drones.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@Entity
@Table
public class Drone implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @NotNull(message = "Drone Serial Number cannot be null")
    @Size(min = 10, max = 100, message = "Serial should be between 10 to 100 characters")
    private String serialNumber;

    @Column
    @NotBlank(message = "Model must be a valid input (Lightweight, Middleweight, Cruiserweight, Heavyweight)")
    private String model;

    @Column
    @NotNull(message = "State must be a valid input (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING)")
    private String state;

    @Column
    @Min(value = 0, message = "Battery level cannot be less than 0%")
    @Max(value = 100, message = "Battery level cannot be more than 100%")
    private Double batteryLevel;

    @Column
    private Double weight;
}
