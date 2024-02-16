package cs509.backend.Service;
import cs509.backend.Entities.FlightDeltas;
import cs509.backend.Repositories.FlightDeltasRepository;
import org.springframework.stereotype.Service;

@Service
public class FlightDeltasService extends FlightService<FlightDeltas, FlightDeltasRepository> {
    public FlightDeltasService(FlightDeltasRepository flightRepository) {
        super(flightRepository);
    }
}
