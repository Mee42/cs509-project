package cs509.backend;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightForm;
import cs509.backend.Enum.OrderBy;
import cs509.backend.Enum.SortBy;
import cs509.backend.Repository.FlightRepository;
import cs509.backend.Service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// This test requires having docker desktop running
@Testcontainers
@TestComponent
public class FlightServiceTest {

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
    final FlightService flightService = new FlightService(flightRepository);

    @Test
    public void testGetAllDepartAirports() {
        String[] arriveAirports = flightService.getAllDepartAirports();
        assertThat(arriveAirports.length).isEqualTo(6);
        assertThat(arriveAirports[0]).isEqualTo("Atlanta (ATL)");
    }

    @Test
    public void testGetAllArriveAirports() {
        String[] arriveAirports = flightService.getAllArriveAirports();
        assertThat(arriveAirports.length).isEqualTo(5);
        assertThat(arriveAirports[0]).isEqualTo("Atlanta (ATL)");
    }

    @Test
    public void testFindOneWayFlightTwoConnections() {
        FlightService.FlightInfo flightInfo = new FlightService.FlightInfo("Atlanta (ATL)", "Tucson (TUS)",
                LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-01-01T23:59:00"),
                0, 20, 60, 1440, SortBy.TravelTime.toString(), OrderBy.DESC.toString());

        List<Flight[]> flightList = flightService.findOneWayFlights("2", flightInfo);
        Flight[] flightArray = flightList.get(0);

        assertThat(flightList.size()).isEqualTo(3); // 2 connections search also include 1 and 0 connections
        assertThat(flightArray.length).isEqualTo(3);
        assertThat(flightArray[0].getFinalArriveDateTime().isBefore(flightArray[1].getStartDepartDateTime())).isTrue();
        assertThat(flightArray[1].getFinalArriveDateTime().isBefore(flightArray[2].getStartDepartDateTime())).isTrue();
        assertThat(flightArray[0].getStartAirport().equals(flightArray[2].getStartAirport())).isFalse();
        assertThat(flightArray[0].getFinalAirport().equals(flightArray[2].getFinalAirport())).isFalse();
        assertThat(flightArray[0].getStartAirport()).isEqualTo("Atlanta (ATL)");
        assertThat(flightArray[2].getFinalAirport()).isEqualTo("Tucson (TUS)");
    }

    @Test
    public void testFindOneWayFlightOneConnection() {
        FlightService.FlightInfo flightInfo = new FlightService.FlightInfo("Atlanta (ATL)", "Tucson (TUS)",
                LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-01-01T23:59:00"),
                0, 20, 60, 1440, SortBy.TravelTime.toString(), OrderBy.DESC.toString());

        List<Flight[]> flightList = flightService.findOneWayFlights("1", flightInfo);
        Flight[] flightArray = flightList.get(0);

        assertThat(flightList.size()).isEqualTo(2); // 1 connection search also include 0 connections
        assertThat(flightArray.length).isEqualTo(2);
        assertThat(flightArray[0].getFinalArriveDateTime().isBefore(flightArray[1].getStartDepartDateTime())).isTrue();
        assertThat(flightArray[0].getStartAirport()).isEqualTo("Atlanta (ATL)");
        assertThat(flightArray[1].getFinalAirport()).isEqualTo("Tucson (TUS)");
    }

    @Test
    public void testFindOneWayNoConnection() {
        FlightService.FlightInfo flightInfo = new FlightService.FlightInfo("Atlanta (ATL)", "Tucson (TUS)",
                LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-01-01T23:59:00"),
                0, 20, 60, 1440, SortBy.TravelTime.toString(), OrderBy.DESC.toString());

        List<Flight[]> flightList = flightService.findOneWayFlights("0", flightInfo);
        Flight[] flightArray = flightList.get(0);

        assertThat(flightList.size()).isEqualTo(1); // 1 connection search also include 0 connections
        assertThat(flightArray.length).isEqualTo(1);
        assertThat(flightArray[0].getStartAirport()).isEqualTo("Atlanta (ATL)");
        assertThat(flightArray[0].getFinalAirport()).isEqualTo("Tucson (TUS)");
    }

    @Test
    public void testFindFlightBy() {
        FlightForm form = new FlightForm("Paris", "Tucson (TUS)", LocalDate.parse("2023-01-01"),
                true, "2", LocalTime.parse("00:00:00"), LocalTime.parse("23:59:00"), SortBy.Arrive, OrderBy.ASC);

        HashMap<String, List<Flight[]>> flights = flightService.findFlightBy(form, 1, 10);
        List<Flight[]> flightList = flights.get("inbound");

        assertThat(flights.get("inbound")).isEqualTo(flightList);
        assertThat(flights.get("outbound")).isEqualTo(null);
        assertThat(flightList.isEmpty()).isTrue(); // the flight Paris to Tucson has too long connection time.
    }




}
