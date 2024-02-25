package cs509.backend.Repositories;

import cs509.backend.Entities.FlightDeltasTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlightRepository extends JpaRepository<FlightDeltasTable, Integer> {

    @Query(value = "SELECT DISTINCT(DepartAirport) FROM deltas UNION " +
            "SELECT DISTINCT(DepartAirport) FROM southwests " +
            "ORDER BY DepartAirport", nativeQuery = true)
    String[] getAllDepartAirport();

    @Query(value = "SELECT DISTINCT(ArriveAirport) FROM deltas UNION " +
            "SELECT DISTINCT(ArriveAirport) FROM southwests " +
            "ORDER BY ArriveAirport", nativeQuery = true)
    String[] getAllArriveAirport();
}