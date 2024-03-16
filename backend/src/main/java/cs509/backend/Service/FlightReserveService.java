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
        List<ReservedFlight> flights = new ArrayList<>();
        for (Flight f : flightReserveForm.flights()) {
            ReservedFlight r = getFlightId(f);
            if (r == null)
                return "Unknown flight: " + f;
            flights.add(r);
        }
        flightReserveRepository.saveAll(flights);
        return null;
    }

    private ReservedFlight getFlightId(Flight flight) {
        Integer deltas_id = flightRepository.getFlightIdFromDeltas(flight);
        if (deltas_id != null)
            return new ReservedFlight(deltas_id, "deltas");
        Integer southwests_id = flightRepository.getFlightIdFromSouthwests(flight);
        if (southwests_id != null)
            return new ReservedFlight(southwests_id, "southwests");
        return null;
    }
}
