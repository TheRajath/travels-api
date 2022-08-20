package com.tourism.travels.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "package")
public class PackageEntity {

    @Id
    private int id;

    @Column(name = "package_Name")
    private String packageName;

    @Column(name = "trip_duration")
    private String tripDuration;

    @Column(name = "cost_per_person")
    private int costPerPerson;
}
