package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.AlreadyExistsException;
import com.tourism.travels.exception.NotFoundException;
import com.tourism.travels.sql.PackageEntity;
import com.tourism.travels.sql.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final TravelMapper travelMapper;
    private final PackageRepository packageRepository;

    public List<PackageEntity> getPackageDetails() {

        return packageRepository.findAll();
    }

    public PackageEntity getPackageEntityById(int packageId) {

        return packageRepository.findById(packageId)
                .orElseThrow(NotFoundException::new);
    }

    public PackageEntity addNewPackage(PackageEntity packageEntityWithUpdates) {

        var packageId = packageEntityWithUpdates.getPackageId();

        packageRepository.findById(packageId)
                .ifPresent(x -> { throw new AlreadyExistsException("Package with is id: "
                           + packageId + " already exists"); });

        return packageRepository.save(packageEntityWithUpdates);
    }

    public PackageEntity updateExistingPackage(PackageEntity packageEntityWithUpdates) {

        var packageId = packageEntityWithUpdates.getPackageId();

        var packageEntity = packageRepository.findById(packageId)
                .orElseThrow(NotFoundException::new);

        travelMapper.updatePackageEntity(packageEntity, packageEntityWithUpdates);

        return packageRepository.save(packageEntity);
    }

    public void deleteByPackageId(int packageId) {

        packageRepository.findById(packageId)
                .orElseThrow(NotFoundException::new);

        packageRepository.deleteById(packageId);
    }

}
