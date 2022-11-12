package com.tourism.travels.customer;

import com.tourism.travels.sql.PackageEntity;
import com.tourism.travels.sql.TicketEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TravelMapperTest {

    private final TravelMapper travelMapper = new TravelMapperImpl();

    @Nested
    class MapSearchResource {

        @Test
        void works() {
            // Arrange
            var packageEntity = new PackageEntity();
            packageEntity.setCostPerPerson(1500);

            var ticketEntity = new TicketEntity();
            ticketEntity.setTotalMembers(2);
            ticketEntity.setPackageEntity(packageEntity);

            // Act
            var searchTicketResource = travelMapper.mapTicketDetails(ticketEntity);

            // Assert
            assertThat(searchTicketResource.getTotalCostOfTrip()).isEqualTo(3000);
        }

    }

}
