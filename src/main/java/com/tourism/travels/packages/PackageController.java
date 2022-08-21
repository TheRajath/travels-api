package com.tourism.travels.packages;

import com.tourism.travels.controller.TravelMapper;
import com.tourism.travels.pojo.PackageDetailsResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;
    private final TravelMapper travelMapper;

    @GetMapping
    public List<PackageDetailsResource> getPackages() {

        return packageService.getPackageDetails().stream()
                .map(travelMapper::toPackageDetailsResource)
                .toList();
    }

}
