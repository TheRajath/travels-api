
package com.tourism.travels.customer;

import com.tourism.travels.pojo.CustomerSignUp;
import com.tourism.travels.pojo.PackageDetailsResource;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.PackageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface TravelMapper {

    @Mapping(target = "totalCost", source = "costPerPerson")
    PackageDetailsResource toPackageDetailsResource(PackageEntity entity);

    CustomerEntity toCustomerEntity(CustomerSignUp customerSignUp);

    CustomerSignUp toSignUpRequest(CustomerEntity customerEntity);

}
