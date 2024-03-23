package cs509.backend;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightReserveForm;
import cs509.backend.Repository.FlightReserveRepository;
import cs509.backend.Service.FlightReserveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

// This test requires having docker desktop running
// All tests will use the same database container so remember to rollback if inserting and take into account all tests share the same data
@SpringBootTest // Don't want to start the whole application for the test, but since JpaRepository is used, therefore need help from Spring to initialize it.
@Testcontainers
public class FlightReserveServiceTest {

    @Container
    @ServiceConnection
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testDB")
            .withUsername("root")
            .withPassword("root")
            .withInitScript("testSql.sql");

    @Autowired
    private FlightReserveService flightReserveService;

    @Autowired
    private FlightReserveRepository flightReserveRepository;

    @Test
    @Transactional
    public void testReserveExistedFlight() {
        Flight f = new Flight("Paris", "Japan", LocalDateTime.parse("2023-01-01T01:00:00"),
                LocalDateTime.parse("2023-01-01T00:03:00"), "WN309");

        Flight f2 = new Flight("Random", "Japan", LocalDateTime.parse("2023-01-01T01:00:00"),
                LocalDateTime.parse("2023-01-01T00:03:00"), "WN309");

        FlightReserveForm form = new FlightReserveForm(new Flight[] {f});

        String result = flightReserveService.reserveFlight(form);
        assertThat(result).isEqualTo(null);
        assertThat(flightReserveRepository.count()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void testReserveNonExistedFlight() {
        Flight f2 = new Flight("Random", "Japan", LocalDateTime.parse("2023-01-01T01:00:00"),
                LocalDateTime.parse("2023-01-01T00:03:00"), "WN309");

        FlightReserveForm form = new FlightReserveForm(new Flight[] {f2});

        String result = flightReserveService.reserveFlight(form);
        assertThat(result).isNotEqualTo(null);
        assertThat(flightReserveRepository.count()).isEqualTo(0);
    }
}
