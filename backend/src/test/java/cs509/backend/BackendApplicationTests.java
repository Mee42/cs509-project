package cs509.backend;

import cs509.backend.Controller.FlightController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
class BackendApplicationTests {


	@Autowired
	FlightController flightController;

	@Test
	void contextLoads() {
		assertThat(flightController).isNotNull();
	}

	@Test
	void allArriveAirportsFollowStringFormat() {
		List<String> airports = Arrays.stream(flightController.getAllArriveAirports()).toList();
		for(var airport : airports) {
			assertThat(airport).matches(".*?\\(.*?\\)");
		}
	}

	@Test
	void getAllArriveAirportsReturnsNoDuplicates() {
		List<String> airports = Arrays.stream(flightController.getAllArriveAirports()).toList();
		assertThat(new HashSet<>(airports).size()).isEqualTo(airports.size());
	}

}
