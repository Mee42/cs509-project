package cs509.backend.Service;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightForm;
import cs509.backend.Enum.OrderBy;
import cs509.backend.Enum.SortBy;
import cs509.backend.Repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    // must be matching field names with named parameters for jdbc
    public record FlightInfo(String departAirport, String arriveAirport,
                             LocalDateTime departDateTimeStart, LocalDateTime departDateTimeEnd,
                             int start, int count, int minConnTime, int maxConnTime, String sortBy, String orderBy) {}

    public HashMap<String, List<Flight[]>> findFlightBy(FlightForm flightForm, int page, int count) {
        // recheck just in case depart airport or arrive airport is not given.
        if (flightForm.getDepartAirport().isEmpty() || flightForm.getArriveAirport().isEmpty())
            return null;

        String numberOfConnection = flightForm.getConnectionNum();
        int start = (page * count) - count + 1;
        final int minConnectionTime = 60; // 1h in minute
        final int maxConnectionTime = 1440; // 24h in minute

        List<Flight[]> flight = findOneWayFlights(numberOfConnection,
                getFlightInfo(flightForm, start, count, minConnectionTime, maxConnectionTime));

        HashMap<String, List<Flight[]>> flightDetails = new HashMap<>();
        if (flightForm.isRoundTrip())
            flightDetails.put("inbound", flight);
        else
            flightDetails.put("outbound", flight);

        return flightDetails;
    }

    public List<Flight[]> findOneWayFlights(String numberOfConnection, FlightInfo flightInfo) {
        List<Flight[]> flightList = new ArrayList<>();
        switch (numberOfConnection) {
            case "2":
                flightList.add(flightRepository.findFlightWithTwoConnection(flightInfo));
            case "1":
                flightList.add(flightRepository.findFlightWithOneConnection(flightInfo));
            case "0":
                flightList.add(flightRepository.findFlightWithNoConnection(flightInfo));
                break;
            default:
                return null;
        }

        Flight[] combinedFlightArray = flightList.stream()
                .flatMap(Arrays::stream)
                .toArray(Flight[]::new);

        if (flightInfo.sortBy.equals(SortBy.Depart.toString())) {
            if (flightInfo.orderBy.equals(OrderBy.DESC.toString()))
                Arrays.sort(combinedFlightArray, Comparator.comparing(Flight::getStartDepartDateTime).reversed());
            Arrays.sort(combinedFlightArray, Comparator.comparing(Flight::getStartDepartDateTime));
        }
        else if (flightInfo.sortBy.equals(SortBy.Arrive.toString())) {
            if (flightInfo.orderBy.equals(OrderBy.DESC.toString()))
                Arrays.sort(combinedFlightArray, Comparator.comparing(Flight::getFinalArriveDateTime).reversed());
            Arrays.sort(combinedFlightArray, Comparator.comparing(Flight::getFinalArriveDateTime));
        }
        else if (flightInfo.sortBy.equals(SortBy.TravelTime.toString())) {
            if (flightInfo.orderBy.equals(OrderBy.DESC.toString()))
                Arrays.sort(combinedFlightArray, Comparator.comparing(Flight::getFlightDuration).reversed());
            Arrays.sort(combinedFlightArray, Comparator.comparing(Flight::getFlightDuration));
        }

        List<Flight[]> flights = new ArrayList<>();
        for (Flight t : combinedFlightArray)
            flights.add(t.getFlights());
        return flights;
    }

    private LocalDateTime checkWindowTime(LocalTime windowTime, LocalDate baseDate, LocalTime defaultTime) {
        if (baseDate == null)
            return null;
        return (windowTime != null) ? LocalDateTime.of(baseDate, windowTime) : (defaultTime != null) ? LocalDateTime.of(baseDate, defaultTime) : null;
    }

    private FlightInfo getFlightInfo(FlightForm flightForm, int start, int count, int minConnTime, int maxConnTime) {
        LocalDate departDate = flightForm.getDepartDate();
        LocalDateTime departStartWindow = checkWindowTime(flightForm.getDepartTimeStart(), departDate, LocalTime.parse("00:00:00"));
        LocalDateTime departEndWindow = checkWindowTime(flightForm.getDepartTimeEnd(), departDate, LocalTime.parse("23:59:00"));

        return new FlightInfo(flightForm.getDepartAirport(), flightForm.getArriveAirport(), departStartWindow, departEndWindow,
                start, count, minConnTime, maxConnTime, flightForm.getSort().toString(), flightForm.getOrder().toString());
    }

    public String[] getAllDepartAirports() {
        return flightRepository.getAllDepartAirports();
    }

    public String[] getAllArriveAirports() {
        return flightRepository.getAllArriveAirports();
    }
}