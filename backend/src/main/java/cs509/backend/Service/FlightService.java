package cs509.backend.Service;

import cs509.backend.Models.Flight;
import cs509.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {
    private final FlightDeltasRepository deltasRepository;

    public FlightService(FlightDeltasRepository deltasRepository) {
        this.deltasRepository = deltasRepository;
    }

    public Flight getFlightDeltasById(int id) {
        return deltasRepository.findById(id).orElse(null);
    }

}
