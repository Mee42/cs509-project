package cs509.backend.Repository;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightOneConnection;
import cs509.backend.Data.FlightTwoConnection;
import cs509.backend.Service.FlightService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class FlightRepository {
    private final JdbcClient jdbcClient;
    public FlightRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Flight[] findFlightWithNoConnection(FlightService.FlightInfo flightInfo) {
        String sql = "WITH CombinedFlights AS (SELECT * FROM deltas UNION SELECT * FROM southwests) " +
                "SELECT " +
                    "DepartAirport AS StartAirport, " +
                    "ArriveAirport AS FinalAirport, " +
                    "DepartDateTime AS StartDepartDateTime, " +
                    "ArriveDateTime AS FinalArriveDateTime, " +
                    "FlightNumber AS FlightNumber1 " +
                "FROM CombinedFlights " +
                    "WHERE DepartAirport = :departAirport " +
                    "AND ArriveAirport = :arriveAirport " +
                    "AND (:departDateTimeStart IS NULL OR DepartDateTime BETWEEN :departDateTimeStart AND :departDateTimeEnd) " +
                "ORDER BY " +
                    "CASE " +
                        "WHEN (:sort = 'Depart') THEN StartDepartDateTime " +
                        "WHEN (:sort = 'TravelTime') THEN ABS(TIMESTAMPDIFF(MINUTE, StartDepartDateTime, FinalArriveDateTime)) " +
                        "ELSE FinalArriveDateTime " +
                    "END * CASE WHEN (:orderBy = 'ASC') THEN 1 ELSE -1 END " +
                "LIMIT :start, :count";

        return jdbcClient.sql(sql)
                .paramSource(flightInfo)
                .query(Flight.class)
                .list().toArray(new Flight[0]);
    }

    public FlightOneConnection[] findFlightWithOneConnection(FlightService.FlightInfo flightInfo) {
        String sql = "WITH CombinedFlights AS (SELECT * FROM deltas UNION SELECT * FROM southwests) " +
                "SELECT " +
                    "A.DepartAirport AS StartAirport, " +
                    "A.ArriveAirport AS Connection1, " +
                    "B.ArriveAirport AS FinalAirport, " +
                    "A.DepartDateTime AS StartDepartDateTime, " +
                    "A.ArriveDateTime AS Leg1ArriveDateTime, " +
                    "B.DepartDateTime AS Leg2DepartDateTime, " +
                    "B.ArriveDateTime AS FinalArriveDateTime, " +
                    "A.FlightNumber AS FlightNumber1, " +
                    "B.FlightNumber AS FlightNumber2 " +
                "FROM CombinedFlights A " +
                "JOIN CombinedFlights B ON A.ArriveAirport = B.DepartAirport " +
                "WHERE " +
                    "A.DepartAirport = :departAirport " +
                    "AND B.ArriveAirport = :arriveAirport " +
                    "AND B.DepartDateTime >= A.ArriveDateTime " +
                    "AND ABS(TIMESTAMPDIFF(MINUTE, A.ArriveDateTime, B.DepartDateTime)) BETWEEN :minConnTime AND :maxConnTime " +
                    "AND (:departDateTimeStart IS NULL OR A.DepartDateTime BETWEEN :departDateTimeStart AND :departDateTimeEnd) " +
                "ORDER BY " +
                    "CASE " +
                        "WHEN (:sort = 'Depart') THEN StartDepartDateTime " +
                        "WHEN (:sort = 'TravelTime') THEN ABS(TIMESTAMPDIFF(MINUTE, StartDepartDateTime, FinalArriveDateTime)) " +
                        "ELSE FinalArriveDateTime " +
                    "END * CASE WHEN (:orderBy = 'ASC') THEN 1 ELSE -1 END " +
                "LIMIT :start, :count";

        return jdbcClient.sql(sql)
                .paramSource(flightInfo)
                .query(FlightOneConnection.class)
                .list().toArray(new FlightOneConnection[0]);
    }

    public FlightTwoConnection[] findFlightWithTwoConnection(FlightService.FlightInfo flightInfo) {
        String sql = "WITH CombinedFlights AS (SELECT * from deltas UNION SELECT * from southwests) " +
                "SELECT " +
                    "A.DepartAirport AS StartAirport, " +
                    "A.ArriveAirport AS Connection1, " +
                    "B.ArriveAirport AS Connection2, " +
                    "C.ArriveAirport AS FinalAirport, " +
                    "A.DepartDateTime AS StartDepartDateTime, " +
                    "A.ArriveDateTime AS Leg1ArriveDateTime, " +
                    "B.DepartDateTime AS Leg2DepartDateTime, " +
                    "B.ArriveDateTime AS Leg2ArriveDateTime, " +
                    "C.DepartDateTime AS Leg3DepartDateTime, " +
                    "C.ArriveDateTime AS FinalArriveDateTime, " +
                    "A.FlightNumber AS FlightNumber1, " +
                    "B.FlightNumber AS FlightNumber2, " +
                    "C.FlightNumber AS FlightNumber3 " +
                "FROM CombinedFlights AS A " +
                "JOIN CombinedFlights AS B ON A.ArriveAirport = B.DepartAirport " +
                "JOIN CombinedFlights AS C ON B.ArriveAirport = C.DepartAirport " +
                "WHERE " +
                    "A.DepartAirport = :departAirport " +
                    "AND C.ArriveAirport = :arriveAirport " +
                    "AND A.DepartAirport != C.DepartAirport " +
                    "AND A.ArriveAirport != C.ArriveAirport " +
                    "AND B.DepartDateTime > A.ArriveDateTime " +
                    "AND C.DepartDateTime > B.ArriveDateTime " +
                    "AND ABS(TIMESTAMPDIFF(MINUTE, A.ArriveDateTime, B.DepartDateTime)) BETWEEN :minConnTime AND :maxConnTime " +
                    "AND ABS(TIMESTAMPDIFF(MINUTE, B.ArriveDateTime, C.DepartDateTime)) BETWEEN :minConnTime AND :maxConnTime " +
                    "AND (:departDateTimeStart IS NULL OR A.DepartDateTime BETWEEN :departDateTimeStart AND :departDateTimeEnd) " +
                "ORDER BY " +
                    "CASE " +
                        "WHEN (:sort = 'Depart') THEN StartDepartDateTime " +
                        "WHEN (:sort = 'TravelTime') THEN ABS(TIMESTAMPDIFF(MINUTE, StartDepartDateTime, FinalArriveDateTime)) " +
                        "ELSE FinalArriveDateTime " +
                    "END * CASE WHEN (:orderBy = 'ASC') THEN 1 ELSE -1 END " +
                "LIMIT :start, :count";

        return jdbcClient.sql(sql)
                .paramSource(flightInfo)
                .query(FlightTwoConnection.class)
                .list().toArray(new FlightTwoConnection[0]);
    }

    public Integer getFlightId(Flight flight) {
        String sql = "WITH CombinedFlights AS (SELECT * from deltas UNION SELECT * from southwests) " +
                "SELECT Id " +
                "FROM CombinedFlights " +
                "WHERE DepartAirport = :startAirport " +
                    "AND ArriveAirport = :finalAirport " +
                    "AND DepartDateTime = :startDepartDateTime " +
                    "AND ArriveDateTime = :finalArriveDateTime " +
                    "AND FlightNumber = :flightNumber1";
        return jdbcClient.sql(sql)
                .paramSource(flight)
                .query(Integer.class)
                .stream().findAny().orElse(null);
    }

    public String[] getAllDepartAirports() {
        String sql = "SELECT DISTINCT(DepartAirport) FROM deltas " +
                "UNION " +
                "SELECT DISTINCT(DepartAirport) FROM southwests " +
                "ORDER BY DepartAirport";
        return jdbcClient.sql(sql).query(String.class).list().toArray(new String[0]);
    }

    public String[] getAllArriveAirports() {
        String sql = "SELECT DISTINCT(ArriveAirport) FROM deltas " +
                "UNION " +
                "SELECT DISTINCT(ArriveAirport) FROM southwests " +
                "ORDER BY ArriveAirport";
        return jdbcClient.sql(sql).query(String.class).list().toArray(new String[0]);
    }
}
