package cs509.backend;

import cs509.backend.Controller.FlightController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
public class HTTPTests {

    @Container
    @ServiceConnection
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testDB")
            .withUsername("root")
            .withPassword("root")
            .withInitScript("testSql.sql");

    @Autowired
    private FlightController flightController;

    @Test
    public void testGetArriveAirportEndpoint() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(flightController).build();
        ResultActions e = mvc.perform(get("/api/flights/arriveAirport")).andExpectAll(
                status().isOk(),
                content().contentType("application/json")
        );
        assertThat(e.andReturn().getResponse().getContentAsString()).matches("\\[(\"[^\"]*?\",?)*]");
    }
}
