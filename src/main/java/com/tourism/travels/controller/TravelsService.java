package com.tourism.travels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelsService {

    private final PackageRepository packageRepository;

    public List<PackageEntity> getPackageDetails() {

        return packageRepository.findAll();
    }

}
