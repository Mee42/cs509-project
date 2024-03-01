package cs509.backend.Data;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
public class FlightOneConnection extends Flight {

    // DTO class - type, and name of fields matter to map data for JdbcClient
    private final String connection1;
    private final LocalDateTime leg1ArriveDateTime;
    private final LocalDateTime leg2DepartDateTime;
    private final String flightNumber2;

    public FlightOneConnection(String startAirport, String connection1, String finalAirport, LocalDateTime startDepartDateTime, LocalDateTime leg1ArriveDateTime,
                               LocalDateTime leg2DepartDateTime, LocalDateTime finalArriveDateTime, String flightNumber1, String flightNumber2) {
        super(startAirport, finalAirport, startDepartDateTime, finalArriveDateTime, flightNumber1);
        this.connection1 = connection1;
        this.leg1ArriveDateTime = leg1ArriveDateTime;
        this.leg2DepartDateTime = leg2DepartDateTime;
        this.flightNumber2 = flightNumber2;
    }

    @Override
    public Flight[] getFlights() {
        return new Flight[] {
                new Flight(getStartAirport(), connection1, getStartDepartDateTime(), getLeg1ArriveDateTime(), getFlightNumber1()),
                new Flight(connection1, getFinalAirport(), leg2DepartDateTime, getFinalArriveDateTime(), flightNumber2)
        };
    }

}
