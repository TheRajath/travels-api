package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.AddPackageRequest;
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
    public AddPackageRequest addPackage(@Valid @RequestBody AddPackageRequest addPackageRequest) {

        var packageEntity = travelMapper.toPackageEntity(addPackageRequest);
        var packageEntityWithUpdates = packageService.addNewPackage(packageEntity);

        return travelMapper.toAddPackageRequest(packageEntityWithUpdates);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(NO_CONTENT)
    public void deletePackage(@PathVariable String customerId) {

        packageService.deleteByPackageId(Integer.parseInt(customerId));
    }

}
