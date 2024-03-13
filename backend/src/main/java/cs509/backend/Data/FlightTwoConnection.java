package cs509.backend.Data;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class FlightTwoConnection extends FlightOneConnection {

    private final String connection2;
    private final LocalDateTime leg2ArriveDateTime;
    private final LocalDateTime leg3DepartDateTime;
    private final String flightNumber3;

    public FlightTwoConnection(String startAirport, String connection1, String connection2, String finalAirport,
                               LocalDateTime startDepartDateTime, LocalDateTime leg1ArriveDateTime, LocalDateTime leg2DepartDateTime,
                               LocalDateTime leg2ArriveDateTime, LocalDateTime leg3DepartDateTime, LocalDateTime finalArriveDateTime,
                               String flightNumber1, String flightNumber2, String flightNumber3) {
        super(startAirport, connection1, finalAirport, startDepartDateTime, leg1ArriveDateTime,
                leg2DepartDateTime, finalArriveDateTime, flightNumber1, flightNumber2);
        this.connection2 = connection2;
        this.leg2ArriveDateTime = leg2ArriveDateTime;
        this.leg3DepartDateTime = leg3DepartDateTime;
        this.flightNumber3 = flightNumber3;
    }

    @Override
    public Flight[] getFlights() {
        return new Flight[] {
                new Flight(getStartAirport(), getConnection1(), getStartDepartDateTime(), getLeg1ArriveDateTime(), getFlightNumber1()),
                new Flight(getConnection1(), connection2, getLeg2DepartDateTime(), leg2ArriveDateTime, getFlightNumber2()),
                new Flight(connection2, getFinalAirport(), leg3DepartDateTime, getFinalArriveDateTime(), flightNumber3)
        };
    }
}
