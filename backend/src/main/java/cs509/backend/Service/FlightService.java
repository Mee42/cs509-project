package cs509.backend.Service;

import cs509.backend.Data.*;
import cs509.backend.Repositories.FlightRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final JdbcClient jdbcClient;

    public record FlightInfo(String departAirport, String arriveAirport,
                             LocalDateTime departDateTimeStart, LocalDateTime departDateTimeEnd,
                             LocalDateTime arriveDateTimeStart, LocalDateTime arriveDateTimeEnd) {}

    public FlightService(FlightRepository flightRepository, JdbcClient jdbcClient) {
        this.flightRepository = flightRepository;
        this.jdbcClient = jdbcClient;
    }

    public HashMap<String, HashMap<String, Flight[]>> findFlightBy(FlightForm flightForm, int page, int count) {
        if (flightForm.getDepartAirport().isEmpty() || flightForm.getArriveAirport().isEmpty())
            return null;

        String numberOfConnection = flightForm.getConnectionNum();
        int start = getLimitStartQuery(page, count);
        final String key1 = "0", key2 = "1", key3 = "2";
        final int minConnectionTime = 60; // in minute
        HashMap<String, HashMap<String, Flight[]>> flightDetails = new HashMap<>();

        HashMap<String, Flight[]> outboundFlight = findOneWayFlights(key1, key2, key3, numberOfConnection,
                getFlightInfo(flightForm, false), start, count, minConnectionTime);

        flightDetails.put("outbound", outboundFlight);

        if (flightForm.isRoundTrip()) {
            if (checkDateABeforeB(flightForm.getRoundTripDepartDate(), flightForm.getArriveDate()))
                return null;

            // if inbound depart date is not given,
            if (flightForm.getRoundTripDepartDate() == null) {
                // set inbound depart date time to the earliest outbound arrive date time + 60 minutes transition.
                // all flights are sorted with the earliest arrive date time already, so just get the first flight.
                if (numberOfConnection.equalsIgnoreCase("all")) {
                    List<LocalDateTime> t = new ArrayList<>();
                    for(Flight[] f : outboundFlight.values())
                        if (f.length != 0)
                            t.add(f[0].getFinalArriveDateTime());
                        else
                            t.add(null);
                    LocalDateTime eOutArriveDateTime = findEarliest(t.get(0), t.get(1), t.get(2)).plusMinutes(minConnectionTime);
                    flightForm.setRoundTripDepartDate(eOutArriveDateTime.toLocalDate());
                    flightForm.setRoundTripDepartTimeStart(eOutArriveDateTime.toLocalTime());
                }
                else if (outboundFlight.get(numberOfConnection).length != 0) {
                    LocalDateTime eOutArriveDateTime = outboundFlight.get(numberOfConnection)[0].getFinalArriveDateTime().plusMinutes(minConnectionTime);
                    flightForm.setRoundTripDepartDate(eOutArriveDateTime.toLocalDate());
                    flightForm.setRoundTripDepartTimeStart(eOutArriveDateTime.toLocalTime());
                }
            }
            flightDetails.put("inbound", findOneWayFlights(key1, key2, key3, numberOfConnection,
                    getFlightInfo(flightForm, true), start, count, minConnectionTime));
        }
        return flightDetails;
    }

    private static LocalDateTime findEarliest(LocalDateTime dt1, LocalDateTime dt2, LocalDateTime dt3) {
        return Stream.of(dt1, dt2, dt3)
                .filter(Objects::nonNull) // Filter out null values
                .min(Comparator.naturalOrder()) // Find the minimum date
                .orElse(null); // Return null if all dates are null
    }

    private int getLimitStartQuery(int page, int count) {
        return (page * count) - count + 1;
    }

    private LocalDateTime checkWindowTime(LocalTime windowTime, LocalDate baseDate, LocalTime defaultTime) {
        if (baseDate == null)
            return null;
        return (windowTime != null) ? LocalDateTime.of(baseDate, windowTime) : (defaultTime != null) ? LocalDateTime.of(baseDate, defaultTime) : null;
    }

    private boolean checkDateABeforeB(LocalDate dateA, LocalDate dateB) {
        if (dateA == null || dateB == null)
            return false;
        return dateA.isBefore(dateB);
    }

    public FlightInfo getFlightInfo(FlightForm flightForm, boolean getRoundTrip) {
        String departAirport = (getRoundTrip) ? flightForm.getArriveAirport() : flightForm.getDepartAirport();
        String arriveAirport = (getRoundTrip) ? flightForm.getDepartAirport() : flightForm.getArriveAirport();
        LocalDate departDate = (getRoundTrip) ? flightForm.getRoundTripDepartDate() : flightForm.getDepartDate();
        LocalDate arriveDate = (getRoundTrip) ? flightForm.getRoundTripArriveDate() : flightForm.getArriveDate();

        LocalTime departTimeStart = (getRoundTrip) ? flightForm.getRoundTripDepartTimeStart() : flightForm.getDepartTimeStart();
        LocalTime departTimeEnd = (getRoundTrip) ? flightForm.getRoundTripDepartTimeEnd() : flightForm.getDepartTimeEnd();
        LocalTime arriveTimeStart = (getRoundTrip) ? flightForm.getRoundTripArriveTimeStart() : flightForm.getArriveTimeStart();
        LocalTime arriveTimeEnd = (getRoundTrip) ? flightForm.getRoundTripArriveTimeEnd() : flightForm.getArriveTimeEnd();

        LocalDateTime departStartWindow = checkWindowTime(departTimeStart, departDate, LocalTime.parse("00:00:00"));
        LocalDateTime departEndWindow = checkWindowTime(departTimeEnd, departDate, LocalTime.parse("23:59:00"));
        LocalDateTime arriveStartWindow = checkWindowTime(arriveTimeStart, arriveDate, LocalTime.parse("00:00:00"));
        LocalDateTime arriveEndWindow = checkWindowTime(arriveTimeEnd, arriveDate, LocalTime.parse("23:59:00"));

        return new FlightInfo(departAirport, arriveAirport, departStartWindow, departEndWindow, arriveStartWindow, arriveEndWindow);
    }

    public HashMap<String, Flight[]> findOneWayFlights(String key1, String key2, String key3, String numberOfConnection,
                                                       FlightInfo flightInfo, int start, int count, int minConnTime) {
        return switch (numberOfConnection.toLowerCase()) {
            case "0" -> findFlightWithNoConnection(key1, flightInfo, start, count);
            case "1" -> findFlightWithOneConnection(key2, flightInfo, minConnTime, start, count);
            case "2" -> findFlightWithTwoConnection(key3, flightInfo, minConnTime, start, count);
            case "all" -> findWithAnyConnection(key1, key2, key3, flightInfo, minConnTime, start, count);
            default -> null;
        };
    }

    public HashMap<String, Flight[]> findFlightWithNoConnection(String key, FlightInfo flightInfo, int start, int count) {
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
                    "AND (:arriveDateTimeStart IS NULL OR ArriveDateTime BETWEEN :arriveDateTimeStart AND :arriveDateTimeEnd) " +
                "ORDER BY " +
                    "FinalArriveDateTime " +
                "LIMIT :start, :count";

        Flight[] no =  jdbcClient.sql(sql)
                .param("departAirport", flightInfo.departAirport)
                .param("arriveAirport", flightInfo.arriveAirport)
                .param("departDateTimeStart", flightInfo.departDateTimeStart)
                .param("departDateTimeEnd", flightInfo.departDateTimeEnd)
                .param("arriveDateTimeStart", flightInfo.arriveDateTimeStart)
                .param("arriveDateTimeEnd", flightInfo.arriveDateTimeEnd)
                .param("start", start)
                .param("count", count)
                .query(FlightNoConnection.class)
                .list().toArray(new FlightNoConnection[0]);

        HashMap<String, Flight[]> flights = new HashMap<>();
            flights.put(key, no);
        return flights;
    }

    public HashMap<String, Flight[]> findFlightWithOneConnection(String key, FlightInfo flightInfo, int minConnTime,
                                                                 int start, int count) {
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
                    "AND ABS(TIMESTAMPDIFF(MINUTE, A.ArriveDateTime, B.DepartDateTime)) >= :minConnectionTime " +
                    "AND (:departDateTimeStart IS NULL OR A.DepartDateTime BETWEEN :departDateTimeStart AND :departDateTimeEnd) " +
                    "AND (:arriveDateTimeStart IS NULL OR B.ArriveDateTime BETWEEN :arriveDateTimeStart AND :arriveDateTimeEnd) " +
                "ORDER BY " +
                    "FinalArriveDateTime " +
                "LIMIT :start, :count";

        Flight[] one = jdbcClient.sql(sql)
                .param("departAirport", flightInfo.departAirport)
                .param("arriveAirport", flightInfo.arriveAirport)
                .param("departDateTimeStart", flightInfo.departDateTimeStart)
                .param("departDateTimeEnd", flightInfo.departDateTimeEnd)
                .param("arriveDateTimeStart", flightInfo.arriveDateTimeStart)
                .param("arriveDateTimeEnd", flightInfo.arriveDateTimeEnd)
                .param("minConnectionTime", minConnTime)
                .param("start", start)
                .param("count", count)
                .query(FlightOneConnection.class)
                .list().toArray(new FlightOneConnection[0]);

        HashMap<String, Flight[]> flights = new HashMap<>();
        flights.put(key, one);
        return flights;
    }

    public HashMap<String, Flight[]> findFlightWithTwoConnection(String key, FlightInfo flightInfo, int minConnTime,
                                                                 int start, int count) {
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
                    "AND A.DepartAirport != B.ArriveAirport " +
                    "AND B.DepartDateTime > A.ArriveDateTime " +
                    "AND C.DepartDateTime > B.ArriveDateTime " +
                    "AND ABS(TIMESTAMPDIFF(MINUTE, A.ArriveDateTime, B.DepartDateTime)) >= :minConnectionTime " +
                    "AND ABS(TIMESTAMPDIFF(MINUTE, B.ArriveDateTime, C.DepartDateTime)) >= :minConnectionTime " +
                    "AND (:departDateTimeStart IS NULL OR A.DepartDateTime BETWEEN :departDateTimeStart AND :departDateTimeEnd) " +
                    "AND (:arriveDateTimeStart IS NULL OR C.ArriveDateTime BETWEEN :arriveDateTimeStart AND :arriveDateTimeEnd) " +
                "ORDER BY " +
                    "FinalArriveDateTime " +
                "LIMIT :start, :count";

        Flight[] two = jdbcClient.sql(sql)
                .param("departAirport", flightInfo.departAirport)
                .param("arriveAirport", flightInfo.arriveAirport)
                .param("departDateTimeStart", flightInfo.departDateTimeStart)
                .param("departDateTimeEnd", flightInfo.departDateTimeEnd)
                .param("arriveDateTimeStart", flightInfo.arriveDateTimeStart)
                .param("arriveDateTimeEnd", flightInfo.arriveDateTimeEnd)
                .param("minConnectionTime", minConnTime)
                .param("start", start)
                .param("count", count)
                .query(FlightTwoConnection.class)
                .list().toArray(new FlightTwoConnection[0]);

        HashMap<String, Flight[]> flights = new HashMap<>();
        flights.put(key, two);
        return flights;
    }

    // Search flights with 0-2 connections
    public HashMap<String, Flight[]> findWithAnyConnection(String keyNo, String keyOne, String keyTwo, FlightInfo flightInfo,
                                                           int minConnectionTime, int start, int count) {
        HashMap<String, Flight[]> flights = new HashMap<>();
        flights.put(keyNo, findFlightWithNoConnection(keyNo, flightInfo, start, count).get(keyNo));
        flights.put(keyOne, findFlightWithOneConnection(keyOne, flightInfo, minConnectionTime, start, count).get(keyOne));
        flights.put(keyTwo, findFlightWithTwoConnection(keyTwo, flightInfo, minConnectionTime, start, count).get(keyTwo));
        return flights;
    }

    public String[] getAllDepartAirports() {
        return flightRepository.getAllDepartAirport();
    }

    public String[] getAllArriveAirports() {
        return flightRepository.getAllArriveAirport();
    }
}