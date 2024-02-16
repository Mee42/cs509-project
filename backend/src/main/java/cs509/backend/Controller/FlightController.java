package cs509.backend.Controller;

import cs509.backend.Entities.FlightDeltas;
import cs509.backend.Service.FlightDeltasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
public class FlightController {

    private final FlightDeltasService flightService;

    public FlightController(FlightDeltasService flightService) {
        this.flightService = flightService;
    }

    @GetMapping()
    public List<FlightDeltas> printTest() {
        List<FlightDeltas> f = flightService.getFlightDeltasByArriveAirport("Denver (DEN)");
        return f;
    }
}
