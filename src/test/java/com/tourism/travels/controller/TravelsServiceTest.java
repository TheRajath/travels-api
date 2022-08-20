package com.tourism.travels.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelsServiceTest {

    @Mock
    private PackageRepository packageRepository;

    private TravelsService travelsService;

    @BeforeEach
    void setup() {

        travelsService = new TravelsService(packageRepository);
    }

    @Test
    void getPackageDetailsTest() {
        // Arrange
        var packageEntities = Collections.singletonList(new PackageEntity());

        when(packageRepository.findAll()).thenReturn(packageEntities);

        // Act
        var packageDetails = travelsService.getPackageDetails();

        //Assert
        assertThat(packageDetails).isEqualTo(packageEntities);

        verify(packageRepository).findAll();

        verifyNoMoreInteractions(packageRepository);
    }

}
