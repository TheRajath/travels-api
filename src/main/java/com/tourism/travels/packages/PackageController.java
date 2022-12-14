package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.PackageRequest;
import com.tourism.travels.pojo.PackageResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
public class PackageController {

    private final TravelMapper travelMapper;
    private final PackageService packageService;

    @GetMapping
    public List<PackageResource> getPackages() {

        return packageService.getPackageDetails().stream()
                .map(travelMapper::toPackageResource)
                .toList();
    }

    @GetMapping("/{packageId}")
    public PackageResource getPackageById(@PathVariable String packageId) {

        var packageEntity = packageService.getPackageEntityById(Integer.parseInt(packageId));

        return travelMapper.toPackageResource(packageEntity);
    }

    @PutMapping("/add")
    public PackageRequest addPackage(@Valid @RequestBody PackageRequest packageRequest) {

        var packageEntity = travelMapper.toPackageEntity(packageRequest);
        var newPackage = packageService.addNewPackage(packageEntity);

        return travelMapper.toPackageRequest(newPackage);
    }

    @PutMapping("/update")
    public PackageRequest updatePackage(@Valid @RequestBody PackageRequest packageRequest) {

        var packageEntity = travelMapper.toPackageEntity(packageRequest);
        var packageEntityWithUpdates = packageService.updateExistingPackage(packageEntity);

        return travelMapper.toPackageRequest(packageEntityWithUpdates);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{customerId}")
    public void deletePackage(@PathVariable String customerId) {

        packageService.deleteByPackageId(Integer.parseInt(customerId));
    }

}
