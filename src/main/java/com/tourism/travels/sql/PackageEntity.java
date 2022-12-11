package com.tourism.travels.sql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "package")
public class PackageEntity {

    @Id
    @Column(name = "id")
    private int packageId;

    @Column(name = "package_Name")
    private String packageName;

    @Column(name = "trip_duration")
    private String tripDuration;

    @Column(name = "cost_per_person")
    private int costPerPerson;

}
