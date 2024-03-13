package cs509.backend.Service;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightReserveForm;
import cs509.backend.Entity.ReservedFlight;
import cs509.backend.Repository.FlightRepository;
import cs509.backend.Repository.FlightReserveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightReserveService {

    private final FlightReserveRepository flightReserveRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public String reserveFlight(FlightReserveForm flightReserveForm) {
        List<Integer> flightIds = new ArrayList<>();
        for (Flight f : flightReserveForm.getFlights()) {
            int id = getFlightId(f);
            if (id == -1)
                return "Unknown flight: " + f;
            flightIds.add(id);
        }
        for (Integer id : flightIds) {
            ReservedFlight r = new ReservedFlight(id, flightReserveForm.getName());
            flightReserveRepository.save(r);
        }
        return null;
    }

    private int getFlightId(Flight flight) {
        Integer id = flightRepository.getFlightId(flight);
        if (id != null)
            return id;
        return -1;
    }
}
