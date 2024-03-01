package cs509.backend.Controller;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightForm;
import cs509.backend.Service.FlightService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{page}") // IGNORE - just for manual testing
    public HashMap<String, List<Flight[]>> getManualTest(@PathVariable("page") int page) {
        String dA = "Atlanta (ATL)";
        String aA = "Tucson (TUS)";
        LocalDate dD = LocalDate.parse("2023-01-01");
        LocalTime dStart = null;
        LocalTime dEnd = null;
        String connectionNumber = "2";
        FlightForm t = new FlightForm(dA, aA, dD, "", false, connectionNumber,
                dStart, dEnd);

        return flightService.findFlightBy(t, page, 10);
    }

    @GetMapping("/departAirport")
    public String[] getAllDepartAirports() {
        return flightService.getAllDepartAirports();
    }

    @GetMapping("/arriveAirport")
    public String[] getAllArriveAirports() {
        return flightService.getAllArriveAirports();
    }

    @PostMapping("/submitForm/{page}")
    public HashMap<String, List<Flight[]>> submitForm(@RequestBody FlightForm flightForm,
                                                                 @PathVariable("page") int page) {
        return flightService.findFlightBy(flightForm, page, 10);
    }
}
