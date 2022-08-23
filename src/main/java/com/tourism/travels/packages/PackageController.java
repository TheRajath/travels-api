package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.AddPackageRequest;
import com.tourism.travels.pojo.PackageDetailsResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/{packageId}")
    public PackageDetailsResource getPackageById(@PathVariable String packageId) {

        var packageEntity = packageService.getPackageEntityById(Integer.parseInt(packageId));

        return travelMapper.toPackageDetailsResource(packageEntity);
    }

    @PutMapping("/add")
    public AddPackageRequest addPackage(@Valid @RequestBody AddPackageRequest addPackageRequest) {

        var packageEntity = travelMapper.toPackageEntity(addPackageRequest);
        var packageEntityWithUpdates = packageService.addNewPackage(packageEntity);

        return travelMapper.toAddPackageRequest(packageEntityWithUpdates);
    }

}
