
package com.tourism.travels.controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface TravelMapper {

    @Mapping(target = "totalCost", source = "costPerPerson")
    PackageDetailsResource toPackageDetailsResource(PackageEntity entity);

    CustomerEntity toCustomerEntity(CustomerSignUp customerSignUp);

    CustomerSignUp toSignUpRequest(CustomerEntity customerEntity);

}
