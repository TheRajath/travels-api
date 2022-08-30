
package com.tourism.travels.customer;

import com.tourism.travels.pojo.*;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.PackageEntity;
import com.tourism.travels.sql.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel="spring")
public interface TravelMapper {

    @Mapping(target = "totalCost", source = "costPerPerson")
    PackageResource toPackageResource(PackageEntity entity);

    CustomerResource toCustomerResource(CustomerEntity entity);

    TicketEntity toTicketEntity(TicketRequest ticketRequest);

    TicketRequest toTicketRequest(TicketEntity ticketEntity);

    @Mapping(target = "totalCost", ignore = true)
    TicketResource toTicketResource(TicketEntity ticketEntity);

    PackageEntity toPackageEntity(PackageRequest packageRequest);

    PackageRequest toPackageRequest(PackageEntity packageEntity);

    CustomerEntity toCustomerEntity(CustomerRequest customerRequest);

    CustomerRequest toCustomerRequest(CustomerEntity customerEntity);

    @Mapping(target = "packageId", ignore = true)
    void updatePackageEntity(@MappingTarget PackageEntity packageEntity, PackageEntity packageEntityWithUpdates);

    @Mapping(target = "customerId", ignore = true)
    void updateCustomerEntity(@MappingTarget CustomerEntity customerEntity, CustomerEntity customerEntityWithUpdates);

}
