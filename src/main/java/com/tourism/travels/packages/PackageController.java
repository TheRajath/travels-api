package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.PackageRequest;
import com.tourism.travels.pojo.PackageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;
    private final TravelMapper travelMapper;

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
        var packageEntityWithUpdates = packageService.addNewPackage(packageEntity);

        return travelMapper.toPackageRequest(packageEntityWithUpdates);
    }

    @PutMapping("/update")
    public PackageRequest updatePackage(@Valid @RequestBody PackageRequest packageRequest) {

        var packageEntity = travelMapper.toPackageEntity(packageRequest);
        var packageEntityWithUpdates = packageService.updateExistingPackage(packageEntity);

        return travelMapper.toPackageRequest(packageEntityWithUpdates);
    }


    @DeleteMapping("/{customerId}")
    @ResponseStatus(NO_CONTENT)
    public void deletePackage(@PathVariable String customerId) {

        packageService.deleteByPackageId(Integer.parseInt(customerId));
    }

}
