package cs509.backend.Repositories;

import cs509.backend.Models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@NoRepositoryBean
public interface FlightRepository<F extends Flight, ID extends Serializable> extends JpaRepository<F, ID> {
    F findByDepartDateTime(LocalDateTime departDateTime);

    List<F> findByArriveAirport(String arriveAirport);
}
