package cs509.backend.Repositories;

import cs509.backend.Models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository<S extends Flight, Integer> extends JpaRepository<S, Integer> {
}
