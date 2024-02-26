package cs509.backend.Controller;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightForm;
import cs509.backend.Service.FlightService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@RestController
@RequestMapping("/api/allflights")
@CrossOrigin
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{page}") // IGNORE - just for manual testing
    public HashMap<String, HashMap<String, Flight[]>> getManualTest(@PathVariable("page") int page) {
        String dA = "Atlanta (ATL)";
        String aA = "Tucson (TUS)";
        LocalDate dD = LocalDate.parse("2023-01-01");
        LocalDate aD = LocalDate.parse("2023-01-01");
        LocalTime dStart = null;
        LocalTime dEnd = null;
        LocalTime aStart = null;
        LocalTime aEnd = null;
        String connectionNumber = "All";

        LocalDate rdD = LocalDate.parse("2023-01-01");

        FlightForm t = new FlightForm(dA, aA, dD, aD, "", false, connectionNumber,
                dStart, dEnd, aStart, aEnd);

        return flightService.findFlightBy(t, page, 10);
    }

    @GetMapping("/departAirports")
    public String[] getAllDepartAirports() {
        return flightService.getAllDepartAirports();
    }

    @GetMapping("/arriveAirport")
    public String[] getAllArriveAirports() {
        return flightService.getAllArriveAirports();
    }

    @PostMapping("/submitForm/{page}")
    public HashMap<String, HashMap<String, Flight[]>> submitForm(@RequestBody FlightForm flightForm,
                                                                 @PathVariable("page") int page) {
        return flightService.findFlightBy(flightForm, page, 10);
    }
}
