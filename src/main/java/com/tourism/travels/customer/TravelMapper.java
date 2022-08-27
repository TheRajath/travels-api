
package com.tourism.travels.customer;

import com.tourism.travels.pojo.PackageRequest;
import com.tourism.travels.pojo.CustomerResource;
import com.tourism.travels.pojo.CustomerRequest;
import com.tourism.travels.pojo.PackageResource;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.PackageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel="spring")
public interface TravelMapper {

    @Mapping(target = "totalCost", source = "costPerPerson")
    PackageResource toPackageResource(PackageEntity entity);

    CustomerResource toCustomerResource(CustomerEntity entity);

    CustomerEntity toCustomerEntity(CustomerRequest customerRequest);

    CustomerRequest toCustomerRequest(CustomerEntity customerEntity);

    PackageEntity toPackageEntity(PackageRequest packageRequest);

    PackageRequest toPackageRequest(PackageEntity packageEntity);

    @Mapping(target = "packageId", ignore = true)
    void updatePackageEntity(@MappingTarget PackageEntity packageEntity, PackageEntity packageEntityWithUpdates);

    @Mapping(target = "customerId", ignore = true)
    void updateCustomerEntity(@MappingTarget CustomerEntity customerEntity, CustomerEntity customerEntityWithUpdates);

}
