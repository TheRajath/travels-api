package com.tourism.travels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelsController {

    private final TravelsService travelsService;

    @GetMapping("/packages")
    public List<PackageEntity> getPackages() {

        return travelsService.getPackageDetails();
    }

    @GetMapping("/totalPackages")
    public Integer getTotalPackages() {

        return 20;
    }

}
