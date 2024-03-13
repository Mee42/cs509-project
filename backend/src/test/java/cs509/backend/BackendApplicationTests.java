package cs509.backend;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightForm;
import cs509.backend.Enum.OrderBy;
import cs509.backend.Enum.SortBy;
import cs509.backend.Repository.FlightRepository;
import cs509.backend.Service.FlightService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class BackendApplicationTests {


	@Autowired
	FlightService flightService;

	@Autowired
	FlightRepository flightRepository;

	@Test
	void contextLoads() {
		assertThat(flightService).isNotNull();
		assertThat(flightService).isNotNull();
	}

	@Test
	void allArriveAirportsFollowStringFormat() {
		List<String> airports = Arrays.stream(flightService.getAllArriveAirports()).toList();
		for(var airport : airports) {
			assertThat(airport).matches(".*?\\(.*?\\)");
		}
	}

	@Test
	void getAllArriveAirportsReturnsNoDuplicates() {
		List<String> airports = Arrays.stream(flightService.getAllArriveAirports()).toList();
		assertThat(new HashSet<>(airports).size()).isEqualTo(airports.size());
	}


	@Test
	void allDepartAirportsFollowStringFormat() {
		List<String> airports = Arrays.stream(flightService.getAllDepartAirports()).toList();
		for(var airport : airports) {
			assertThat(airport).matches(".*?\\(.*?\\)");
		}
	}

	@Test
	void getAllDepartAirportsReturnsNoDuplicates() {
		List<String> airports = Arrays.stream(flightService.getAllDepartAirports()).toList();
		assertThat(new HashSet<>(airports).size()).isEqualTo(airports.size());
	}


	@Test
	void allOneWayFlightsFromJFKtoDENAreReasonable() {
        List<Flight[]> flights = flightService.findOneWayFlights("0", new FlightService.FlightInfo(
				"New York (JFK)",
				"Denver (DEN)",
				LocalDateTime.now().minusYears(5),
				LocalDateTime.now(),
				0, 100, 0, Integer.MAX_VALUE, SortBy.Arrive, OrderBy.ASC
		));
		for(var flight : flights) {
			assertThat(flight.length).isEqualTo(1);
			assertThat(flight[0].getStartAirport()).isEqualTo("New York (JFK)");
			assertThat(flight[0].getFinalAirport()).isEqualTo("Denver (DEN)");
			// no flight takes longer than a day
			assertThat(flight[0].getStartDepartDateTime().plusDays(1)).isAfter(flight[0].getFinalArriveDateTime());
		}
		assertThat(flights.size()).isGreaterThan(5); // just make sure we're checking a few flights
	}

	@Test
	void layoverLengthConstraintWorks() {
		List<Flight[]> flights = flightService.findOneWayFlights("1", new FlightService.FlightInfo(
				"New York (JFK)",
				"Denver (DEN)",
				LocalDateTime.now().minusYears(5),
				LocalDateTime.now(),
				0, 1000, 1, 55, SortBy.Arrive, OrderBy.ASC 
		));
		for(var flight : flights) {
			if(flight.length == 2) {
				assertThat(flight[0].getFinalArriveDateTime()).isBefore(flight[1].getStartDepartDateTime());
				// less than an hour difference
				assertThat(flight[0].getFinalArriveDateTime().plusHours(1)).isAfter(flight[1].getStartDepartDateTime());
			}
		}
		assertThat(flights.size()).isGreaterThan(3);
	}

	@Test
	void flightsReturnedAreUnique() {
		List<Flight> flights = flightService.findOneWayFlights("0", new FlightService.FlightInfo(
				"New York (JFK)",
				"Denver (DEN)",
				LocalDateTime.now().minusYears(5),
				LocalDateTime.now(),
				0, 1000, 1, 55, SortBy.Depart, OrderBy.ASC
		)).stream().map(it -> it[0]).toList();
		Set<Integer> IDs = flights.stream().map(it -> flightRepository.getFlightId(it)).collect(Collectors.toSet());
		assertThat(IDs.size()).isEqualTo(flights.size());

	}

	@Test
	void testFlightFormValidationFunction() {
		FlightForm a = new FlightForm("New York (JFK)", "Denver (DEN)", LocalDate.now(), false,
				"2", LocalTime.MIN, LocalTime.MAX, SortBy.Arrive, OrderBy.ASC);
		assertThat(a.checkAllFields()).isNull();

		a = new FlightForm("", "Denver (DEN)", LocalDate.now(), false,
				"2", LocalTime.MIN, LocalTime.MAX, SortBy.Arrive, OrderBy.ASC);
		assertThat(a.checkAllFields()).isNotNull();

		a = new FlightForm("New York (JFK)", "", LocalDate.now(), false,
				"2", LocalTime.MIN, LocalTime.MAX, SortBy.Arrive, OrderBy.ASC);
		assertThat(a.checkAllFields()).isNotNull();

		a = new FlightForm("New York (JFK)", "Denver (DEN)", LocalDate.now(), false,
				"2", LocalTime.MAX, LocalTime.MIN, SortBy.Arrive, OrderBy.ASC);
		assertThat(a.checkAllFields()).isNotNull(); // reversed  time

		a = new FlightForm("New York (JFK)", "Denver (DEN)", LocalDate.now(), false,
				"8", LocalTime.MIN, LocalTime.MAX, SortBy.Arrive, OrderBy.ASC);
		assertThat(a.checkAllFields()).isNotNull(); // illegal connection num

		a = new FlightForm("New York (JFK)", "Denver (DEN)", LocalDate.now(), false,
				"antoeh", LocalTime.MIN, LocalTime.MAX, SortBy.Arrive, OrderBy.ASC);
		assertThat(a.checkAllFields()).isNotNull(); // connection num is a non-integer

		a = new FlightForm("New York (JFK)", "Denver (DEN)", LocalDate.now(), false,
				"", LocalTime.MIN, LocalTime.MAX, SortBy.Arrive, OrderBy.ASC);
		assertThat(a.checkAllFields()).isNotNull(); // connection num blank
	}
}
