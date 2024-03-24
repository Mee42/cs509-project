package cs509.backend;

import cs509.backend.Entity.ReservedFlight;
import cs509.backend.Repository.FlightReserveRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// This test requires having docker desktop running
// All tests will use the same database container so remember to rollback if inserting and take into account all tests share the same data
@Testcontainers
@Transactional
@SpringBootTest
class FlightReserveRepositoryTest {

    @Container
    @ServiceConnection
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testDB")
            .withUsername("root")
            .withPassword("root")
            .withInitScript("testSql.sql");

    @Autowired
    FlightReserveRepository flightReserveRepository;

    @AfterAll
    public static void tearDown() {
        // Stop the test container after all tests
        if (mySQLContainer != null) {
            mySQLContainer.stop();
        }
    }

    @Test
    @Rollback
    void testGetCount() {
        ReservedFlight r = new ReservedFlight(3, "deltas");
        ReservedFlight r2 = new ReservedFlight(3, "deltas");
        ReservedFlight r3 = new ReservedFlight(3, "deltas");
        flightReserveRepository.saveAll(List.of(r, r2, r3));
        assertThat(flightReserveRepository.count()).isEqualTo(3);
    }

    @Test
    @Rollback
    void testSaveAll() {
        ReservedFlight r = new ReservedFlight(3, "deltas");
        flightReserveRepository.saveAll(List.of(r));
        assertThat(flightReserveRepository.count()).isEqualTo(1);
    }



}
