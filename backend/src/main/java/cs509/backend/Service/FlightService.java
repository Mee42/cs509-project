package cs509.backend.Service;

import cs509.backend.Data.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class FlightService {
    private final JdbcClient jdbcClient;

    // must be matching field names with named parameters for jdbc
    public record FlightInfo(String departAirport, String arriveAirport,
                             LocalDateTime departDateTimeStart, LocalDateTime departDateTimeEnd,
                             int start, int count, int minConnTime, int maxConnTime) {}

    public FlightService(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    public HashMap<String, List<Flight[]>> findFlightBy(FlightForm flightForm, int page, int count) {
        if (flightForm.getDepartAirport().isEmpty() || flightForm.getArriveAirport().isEmpty())
            return null;

        String numberOfConnection = flightForm.getConnectionNum();
        int start = (page * count) - count + 1;
        final int minConnectionTime = 60; // 1h in minute
        final int maxConnectionTime = 1440; // 24h in minute

        List<Flight[]> flight = findOneWayFlights(numberOfConnection, getFlightInfo(flightForm, start, count, minConnectionTime, maxConnectionTime));

        HashMap<String, List<Flight[]>> flightDetails = new HashMap<>();
        if (flightForm.isRoundTrip())
            flightDetails.put("inbound", flight);
        else
            flightDetails.put("outbound", flight);

        return flightDetails;
    }

    private LocalDateTime checkWindowTime(LocalTime windowTime, LocalDate baseDate, LocalTime defaultTime) {
        if (baseDate == null)
            return null;
        return (windowTime != null) ? LocalDateTime.of(baseDate, windowTime) : (defaultTime != null) ? LocalDateTime.of(baseDate, defaultTime) : null;
    }

    public FlightInfo getFlightInfo(FlightForm flightForm, int start, int count, int minConnTime, int maxConnTime) {
        LocalDate departDate = flightForm.getDepartDate();
        LocalDateTime departStartWindow = checkWindowTime(flightForm.getDepartTimeStart(), departDate, LocalTime.parse("00:00:00"));
        LocalDateTime departEndWindow = checkWindowTime(flightForm.getDepartTimeEnd(), departDate, LocalTime.parse("23:59:00"));

        return new FlightInfo(flightForm.getDepartAirport(), flightForm.getArriveAirport(),
                departStartWindow, departEndWindow, start, count, minConnTime, maxConnTime);
    }

    public List<Flight[]> findOneWayFlights(String numberOfConnection, FlightInfo flightInfo) {
        List<Flight[]> flightList = new ArrayList<>();
        switch (numberOfConnection) {
            case "2":
                Flight[] two = findFlightWithTwoConnection(flightInfo);
                for (Flight t : two)
                    flightList.add(t.getFlights());
            case "1":
                Flight[] one = findFlightWithOneConnection(flightInfo);
                for (Flight t : one)
                    flightList.add(t.getFlights());
            case "0":
                Flight[] no = findFlightWithNoConnection(flightInfo);
                for (Flight t : no)
                    flightList.add(t.getFlights());
            default:
                return flightList;
        }
    }

    public Flight[] findFlightWithNoConnection(FlightInfo flightInfo) {
        String sql =
                "WITH CombinedFlights AS (SELECT * FROM deltas UNION SELECT * FROM southwests) " +
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
                    "FinalArriveDateTime " +
                "LIMIT :start, :count";

        return jdbcClient.sql(sql)
                .paramSource(flightInfo)
                .query(Flight.class)
                .list().toArray(new Flight[0]);
    }

    public FlightOneConnection[] findFlightWithOneConnection(FlightInfo flightInfo) {
        String sql =
                "WITH CombinedFlights AS (SELECT * FROM deltas UNION SELECT * FROM southwests) " +
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
                "FROM " +
                    "CombinedFlights A " +
                    "JOIN " +
                        "CombinedFlights B ON A.ArriveAirport = B.DepartAirport " +
                "WHERE " +
                    "A.DepartAirport = :departAirport " +
                    "AND B.ArriveAirport = :arriveAirport " +
                    "AND B.DepartDateTime >= A.ArriveDateTime " +
                    "AND ABS(TIMESTAMPDIFF(MINUTE, A.ArriveDateTime, B.DepartDateTime)) BETWEEN :minConnTime AND :maxConnTime " +
                    "AND (:departDateTimeStart IS NULL OR A.DepartDateTime BETWEEN :departDateTimeStart AND :departDateTimeEnd) " +
                "ORDER BY " +
                    "FinalArriveDateTime " +
                "LIMIT :start, :count";

        return jdbcClient.sql(sql)
                .paramSource(flightInfo)
                .query(FlightOneConnection.class)
                .list().toArray(new FlightOneConnection[0]);
    }

    public FlightTwoConnection[] findFlightWithTwoConnection(FlightInfo flightInfo) {
        String sql =
                "WITH CombinedFlights AS ( SELECT * from deltas UNION SELECT * from southwests) " +
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
                    "JOIN " +
                        "CombinedFlights AS B ON A.ArriveAirport = B.DepartAirport " +
                    "JOIN " +
                        "CombinedFlights AS C ON B.ArriveAirport = C.DepartAirport " +
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
                    "FinalArriveDateTime " +
                "LIMIT :start, :count";

        return jdbcClient.sql(sql)
                .paramSource(flightInfo)
                .query(FlightTwoConnection.class)
                .list().toArray(new FlightTwoConnection[0]);
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