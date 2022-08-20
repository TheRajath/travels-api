package com.tourism.travels.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TravelsControllerTest {

    @Mock
    private TravelsService travelsService;

    private TravelsController travelsController;

    @BeforeEach
    void setUp() {

        travelsController = new TravelsController(travelsService);
    }

    /*@Test
    void getPackagesTest() {
        // Arrange
        var travels = "Travels";

        // Act
        var returnedString = travelsController.getPackages(travels);

        // Assert
        assertThat(returnedString).isEqualTo(travels);

    }*/

    @Test
    void getTotalPackagesTest() {
        // Arrange/Act
        var returnedNumberOfPackages = travelsController.getTotalPackages();

        // Assert
        assertThat(returnedNumberOfPackages).isEqualTo(20);

    }

}
