package com.tourism.travels;

import com.tourism.travels.controller.TravelsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TravelsApplicationTests {

	private TravelsController travelsController;

	@BeforeEach
	void setUp() {

		travelsController = new TravelsController();
	}

	@Test
	void works() {
		// Arrange
		var travels = "Travels";

		// Act
		var returnedString = travelsController.getPackages(travels);

		// Assert
		assertThat(returnedString).isEqualTo(travels);

	}

}
