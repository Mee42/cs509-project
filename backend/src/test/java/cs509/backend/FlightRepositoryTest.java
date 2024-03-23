package cs509.backend;

import cs509.backend.Data.Flight;
import cs509.backend.Enum.OrderBy;
import cs509.backend.Enum.SortBy;
import cs509.backend.Repository.FlightRepository;
import cs509.backend.Service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

// This test requires having docker desktop running
@TestComponent
@Testcontainers
public class FlightRepositoryTest {

    @Container
    @ServiceConnection
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testDB")
            .withUsername("root")
            .withPassword("root")
            .withInitScript("testSql.sql");

    DataSource dataSource = DataSourceBuilder.create()
            .url(mySQLContainer.getJdbcUrl())
            .username(mySQLContainer.getUsername())
            .password(mySQLContainer.getPassword())
            .build();

    final FlightRepository flightRepository = new FlightRepository(JdbcClient.create(dataSource));

    @Test
    public void testFindFlightWithNoConnection() {
        FlightService.FlightInfo flightInfo = new FlightService.FlightInfo("Atlanta (ATL)", "Tucson (TUS)",
                LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-01-01T23:59:00"),
                0, 20, 60, 1440, SortBy.TravelTime.toString(), OrderBy.DESC.toString());

        Flight[] f = flightRepository.findFlightWithNoConnection(flightInfo);
        assertThat(f.length).isEqualTo(1);
    }

    @Test
    public void testFindFlightWithOneConnection() {
        FlightService.FlightInfo flightInfo = new FlightService.FlightInfo("Atlanta (ATL)", "Tucson (TUS)",
                LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-01-01T23:59:00"),
                0, 20, 60, 1440, SortBy.TravelTime.toString(), OrderBy.DESC.toString());

        Flight[] f = flightRepository.findFlightWithOneConnection(flightInfo);
        assertThat(f.length).isEqualTo(1);
    }

    @Test
    public void testFindFlightWithTwoConnections() {
        FlightService.FlightInfo flightInfo = new FlightService.FlightInfo("Atlanta (ATL)", "Tucson (TUS)",
                LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-01-01T23:59:00"),
                0, 20, 60, 1440, SortBy.TravelTime.toString(), OrderBy.DESC.toString());
        Flight[] f = flightRepository.findFlightWithTwoConnection(flightInfo);
        assertThat(f.length).isEqualTo(1);
    }

    @Test
    public void testGetFlightIdFromDeltas() {
        Flight f = new Flight("Paris", "Japan", LocalDateTime.parse("2023-01-01T01:00:00"),
                LocalDateTime.parse("2023-01-01T00:03:00"), "WN309");

        Integer t = flightRepository.getFlightIdFromDeltas(f);
        assertThat(t).isEqualTo(1);
    }

    @Test
    public void testGetFlightIdFromSouthwests() {
        Flight f = new Flight("Atlanta (ATL)", "Denver (DEN)", LocalDateTime.parse("2023-01-01T20:40:00"),
                LocalDateTime.parse("2023-01-01T00:03:00"), "WN309");

        Integer t = flightRepository.getFlightIdFromSouthwests(f);
        assertThat(t).isEqualTo(1);
    }

    @Test
    public void testGetAllDepartAirports() {
        String[] f = flightRepository.getAllDepartAirports();
        assertThat(f.length).isEqualTo(6);
        assertThat(f[0]).isEqualTo("Atlanta (ATL)");
    }

    @Test
    public void testGetAllArriveAirports() {
        String[] f = flightRepository.getAllArriveAirports();
        assertThat(f.length).isEqualTo(5);
        assertThat(f[0]).isEqualTo("Atlanta (ATL)");
    }
}
