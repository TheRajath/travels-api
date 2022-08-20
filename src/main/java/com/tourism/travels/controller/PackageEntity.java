package com.tourism.travels.controller;

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
    private String duration;

}
