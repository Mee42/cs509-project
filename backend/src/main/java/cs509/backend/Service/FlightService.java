package cs509.backend.Service;

import cs509.backend.Entities.Flight;
import cs509.backend.Repositories.*;
import java.time.LocalDateTime;
import java.util.List;

public abstract class FlightService<F extends Flight, R extends FlightRepository<F, Integer>> {
    protected final R flightRepository;

    public FlightService(R flightRepository) {
        this.flightRepository = flightRepository;
    }

    public F getFlightDeltasById(int id) {
        return flightRepository.findById(id).orElse(null);
    }
    public F getFlightDeltasByDepartDateTime(LocalDateTime dateTime) {
        return flightRepository
            .findByDepartDateTime(dateTime);
    }
    public List<F> getFlightDeltasByArriveAirport(String arriveAirport) {
        return flightRepository.findByArriveAirport(arriveAirport);
    }
}
