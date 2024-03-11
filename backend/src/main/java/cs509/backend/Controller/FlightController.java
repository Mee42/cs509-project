package cs509.backend.Controller;

import cs509.backend.Data.FlightForm;
import cs509.backend.Data.FlightReserveForm;
import cs509.backend.Service.FlightReserveService;
import cs509.backend.Service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;
    private final FlightReserveService flightReserveService;

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

    @PostMapping("/reserveFlight")
    public ResponseEntity<?> reserveFlight(@RequestBody FlightReserveForm flightReserveForm) {
        String msg = flightReserveForm.checkAllFields();
        if (msg == null) {
            String msg2 = flightReserveService.reserveFlight(flightReserveForm);
            if (msg2 == null)
                return ResponseEntity.ok("ok");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error message: " + msg2);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error message: " + msg);
    }
}
