package cs509.backend.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Flight {

    // DTO class - type, and name of fields matter to map data for JdbcClient
    private final String startAirport;
    private final String finalAirport;
    private final LocalDateTime startDepartDateTime;
    private final LocalDateTime finalArriveDateTime;
    private final String flightNumber1;
}
