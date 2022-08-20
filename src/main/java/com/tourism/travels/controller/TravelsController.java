package com.tourism.travels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelsController {

    private final TravelsService travelsService;
    private final TravelMapper travelMapper;

    @GetMapping("/packages")
    public List<PackageDetailsResource> getPackages() {

        return travelsService.getPackageDetails().stream()
                .map(travelMapper::toPackageDetailsResource)
                .toList();
    }

    @GetMapping("/totalPackages")
    public Integer getTotalPackages() {

        return 20;
    }

}
