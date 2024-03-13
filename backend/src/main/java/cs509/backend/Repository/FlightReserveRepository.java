package cs509.backend.Repository;

import cs509.backend.Entity.ReservedFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightReserveRepository extends JpaRepository<ReservedFlight, Integer> {
}
