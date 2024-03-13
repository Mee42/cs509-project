package cs509.backend;

import cs509.backend.Controller.FlightController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class HTTPTests {

    @Autowired
    FlightController flightController;

    MockMvc mvc;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(flightController).build();
    }

    @Test
    void test() throws Exception {
        ResultActions e = mvc.perform(get("/api/flights/arriveAirport")).andExpectAll(
                status().isOk(),
                content().contentType("application/json")
        );
        assertThat(e.andReturn().getResponse().getContentAsString()).matches("\\[(\"[^\"]*?\",?)*]");
    }
}
