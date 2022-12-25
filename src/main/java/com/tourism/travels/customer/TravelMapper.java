
package com.tourism.travels.customer;

import com.tourism.travels.pojo.*;
import com.tourism.travels.pojo.SearchTicketResource.TicketDetail;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.PackageEntity;
import com.tourism.travels.sql.TicketEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel="spring")
public interface TravelMapper {

    @Mapping(target = "totalCost", source = "costPerPerson")
    PackageResource toPackageResource(PackageEntity entity);

    CustomerResource toCustomerResource(CustomerEntity entity);

    @Mapping(target = "packageEntity", ignore = true)
    @Mapping(target = "customerEntity", ignore = true)
    TicketEntity toTicketEntity(TicketRequest ticketRequest);

    TicketRequest toTicketRequest(TicketEntity ticketEntity);

    @Mapping(target = "totalCost", ignore = true)
    TicketResource toTicketResource(TicketEntity ticketEntity);

    PackageEntity toPackageEntity(PackageRequest packageRequest);

    PackageRequest toPackageRequest(PackageEntity packageEntity);

    CustomerEntity toCustomerEntity(CustomerRequest customerRequest);

    CustomerRequest toCustomerRequest(CustomerEntity customerEntity);

    @Mapping(target = "ticketId", ignore = true)
    void updateTicketEntity(@MappingTarget TicketEntity ticketEntity, TicketEntity ticketEntityWithUpdates);

    @Mapping(target = "packageId", ignore = true)
    void updatePackageEntity(@MappingTarget PackageEntity packageEntity, PackageEntity packageEntityWithUpdates);

    @Mapping(target = "customerId", ignore = true)
    void updateCustomerEntity(@MappingTarget CustomerEntity customerEntity, CustomerEntity customerEntityWithUpdates);

    @Mapping(target = "totalCostOfTrip", ignore = true)
    @Mapping(target = "firstName", source = "ticketEntity.customerEntity.firstName")
    @Mapping(target = "lastName", source = "ticketEntity.customerEntity.lastName")
    @Mapping(target = "email", source = "ticketEntity.customerEntity.email")
    @Mapping(target = "packageName", source = "ticketEntity.packageEntity.packageName")
    @Mapping(target = "tripDuration", source = "ticketEntity.packageEntity.tripDuration")
    TicketDetail mapTicketDetails(TicketEntity ticketEntity);

    @AfterMapping
    default void setTotalCostInTicketResource(@MappingTarget TicketResource ticketResource, TicketEntity ticketEntity) {

        var totalCost = getCostOfTrip(ticketEntity);

        ticketResource.setTotalCost(totalCost);
    }

    @AfterMapping
    default void setTotalCostForSearchResource(@MappingTarget TicketDetail ticketDetail, TicketEntity ticketEntity) {

        var totalCostOfTrip = getCostOfTrip(ticketEntity);

        ticketDetail.setTotalCostOfTrip(totalCostOfTrip);
    }

    private int getCostOfTrip(TicketEntity ticketEntity) {
        var totalMembers = ticketEntity.getTotalMembers();
        var costPerPerson = ticketEntity.getPackageEntity().getCostPerPerson();

        return totalMembers * costPerPerson;
    }

}
