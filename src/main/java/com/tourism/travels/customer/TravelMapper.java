
package com.tourism.travels.customer;

import com.tourism.travels.pojo.AddPackageRequest;
import com.tourism.travels.pojo.CustomerResource;
import com.tourism.travels.pojo.CustomerSignUp;
import com.tourism.travels.pojo.PackageResource;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.PackageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface TravelMapper {

    @Mapping(target = "totalCost", source = "costPerPerson")
    PackageResource toPackageResource(PackageEntity entity);

    CustomerResource toCustomerResource(CustomerEntity entity);

    CustomerEntity toCustomerEntity(CustomerSignUp customerSignUp);

    CustomerSignUp toSignUpRequest(CustomerEntity customerEntity);

    PackageEntity toPackageEntity(AddPackageRequest addPackageRequest);

    AddPackageRequest toAddPackageRequest(PackageEntity packageEntity);

}
