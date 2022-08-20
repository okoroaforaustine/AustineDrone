package com.musala.drones.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Medication implements Serializable {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String droneSerialNumber;

    @Column
    @NotBlank(message = "Medication name cannot be blank")
    @Pattern(regexp = "[a-zA-Z0-9-_]{1,100}", message = "Medication name can only contain letters, numbers, ‘-‘, and ‘_’")
    private String name;

    @Column
    @Min(value = 0, message = "Medication can not have negative or zero weight")
    @Max(value = 500, message = "Medication must weigh less than 500gr")
    private Double weight;

    @Column
    @NotBlank(message = "Medication code cannot be blank")
    @Pattern(regexp = "[A-Z0-9_]{1,40}", message = "Medication code can only contain uppercase letters, numbers, ‘-‘, and ‘_’")
    private String code;

    @Column
    private String image;

    @Column
    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date createdAt;

    @Column
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    private Date updatedAt;
}
