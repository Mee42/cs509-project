package cs509.backend.Controller;

import cs509.backend.Models.Flight;
import cs509.backend.Service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping()
    public Flight printTest() {
        Flight f = flightService.getFlightDeltasById(1);
        return f;
    }
}
