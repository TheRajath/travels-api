package com.tourism.travels.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TravelsControllerTest {

    private TravelsController travelsController;

    @BeforeEach
    void setUp() {

        travelsController = new TravelsController();
    }

    @Test
    void getPackagesTest() {
        // Arrange
        var travels = "Travels";

        // Act
        var returnedString = travelsController.getPackages(travels);

        // Assert
        assertThat(returnedString).isEqualTo(travels);

    }

    @Test
    void getTotalPackagesTest() {
        // Arrange/Act
        var returnedNumberOfPackages = travelsController.getTotalPackages();

        // Assert
        assertThat(returnedNumberOfPackages).isEqualTo(20);

    }

}
