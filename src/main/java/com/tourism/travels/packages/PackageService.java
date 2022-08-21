package com.tourism.travels.packages;

import com.tourism.travels.sql.PackageEntity;
import com.tourism.travels.sql.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    public List<PackageEntity> getPackageDetails() {

        return packageRepository.findAll();
    }

}
