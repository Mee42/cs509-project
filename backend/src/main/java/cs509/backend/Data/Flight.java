package cs509.backend.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class Flight {

    // DTO class - type matter to map data for JdbcClient
    @JsonProperty("departAirport")
    private final String startAirport;
    @JsonProperty("arriveAirport")
    private final String finalAirport;
    @JsonProperty("departDateTime")
    private final LocalDateTime startDepartDateTime;
    @JsonProperty("arriveDateTime")
    private final LocalDateTime finalArriveDateTime;
    @JsonProperty("flightNumber")
    private final String flightNumber1;

    @JsonIgnore
    public Flight[] getFlights() {
        return new Flight[] {
                new Flight(startAirport, finalAirport, startDepartDateTime, finalArriveDateTime, flightNumber1)
        };
    }

    @JsonIgnore
    public Duration getFlightDuration() {
        return Duration.between(startDepartDateTime, finalArriveDateTime);
    }

}
