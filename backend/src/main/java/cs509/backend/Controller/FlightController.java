package cs509.backend.Controller;

import cs509.backend.Data.FlightForm;
import cs509.backend.Data.FlightReserveForm;
import cs509.backend.Service.FlightReserveService;
import cs509.backend.Service.FlightService;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name = "Get departure airports", description = "GET an array of strings containing the names of all departure airports")
    @GetMapping("/departAirport")
    public String[] getAllDepartAirports() {
        return flightService.getAllDepartAirports();
    }
    @Tag(name = "Get arrive airports", description = "GET an array of strings containing the names of all arrive airports")
    @GetMapping("/arriveAirport")
    public String[] getAllArriveAirports() {
        return flightService.getAllArriveAirports();
    }

    @Tag(name = "Submit Flight Inquiry Form", description = "It receives a FlightForm object as the request body and an integer page as the path variable. It then calls the checkAllFields() method of the flightForm object to validate that the form fields are complete, and if the validation passes, it calls the findFlightBy() method in the flightService to look up the flight information and return the query result. If the form validation fails, a BAD_REQUEST response with an error message is returned.")
    @PostMapping("/submitForm/{page}")
    public ResponseEntity<?> submitForm(@RequestBody FlightForm flightForm, @PathVariable("page") int page) {
        String msg = flightForm.checkAllFields();
        if (msg == null)
            return ResponseEntity.ok(flightService.findFlightBy(flightForm, page, 10));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error message: " + msg);
    }


    @Tag(name = "Book Flights", description = "If the validation passes, the reserveFlight() method in the flightReserveService is called to make the flight reservation. If the reservation is successful, an OK response is returned. If the booking fails, a BAD_REQUEST response with an error message is returned.")
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

