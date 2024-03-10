package cs509.backend.Controller;

import cs509.backend.Data.FlightForm;
import cs509.backend.Service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{page}") // IGNORE - just for manual testing
    public ResponseEntity<?> getManualTest(@PathVariable("page") int page) {
        String dA = "Atlanta (ATL)";
        String aA = "Tucson (TUS)";
        LocalDate dD = LocalDate.parse("2023-01-01");
        LocalTime dStart = null;
        LocalTime dEnd = null;
        String connectionNumber = "0";
        FlightForm flightForm = new FlightForm(dA, aA, dD, false, connectionNumber,
                dStart, dEnd, "depart", "asc");

        String msg = flightForm.checkAllFields();
        if (msg == null)
            return ResponseEntity.ok(flightService.findFlightBy(flightForm, page, 10));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error message: " + msg);
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
    public ResponseEntity<?> submitForm(@RequestBody FlightForm flightForm, @PathVariable("page") int page) {
        String msg = flightForm.checkAllFields();
        if (msg == null)
            return ResponseEntity.ok(flightService.findFlightBy(flightForm, page, 10));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error message: " + msg);
    }
}
