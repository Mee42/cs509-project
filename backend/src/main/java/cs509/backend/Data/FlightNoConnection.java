package cs509.backend.Data;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FlightNoConnection extends Flight {
    public FlightNoConnection(String startAirport, String finalAirport, LocalDateTime startDepartDateTime,
                              LocalDateTime finalArriveDateTime, String flightNumber1) {
        super(startAirport, finalAirport, startDepartDateTime, finalArriveDateTime, flightNumber1);
    }
}
