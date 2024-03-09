package cs509.backend.Data;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightForm {

    //DTO class to map form submit from frontend to backend - name, case, and type are important here

    private String departAirport; //required
    private String arriveAirport; //required

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departDate;

    private boolean roundTrip; // default - false - no round trip
    private String connectionNum; // default 2 - accept (0, 1, 2)

    // only used if depart date is specified
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departTimeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departTimeEnd;

    private String sort; // default arrive - accept (depart, arrive, travelTime)
    private String order; // default asc - accept (asc, desc)

    public FlightForm(String departAirport, String arriveAirport, LocalDate departDate,
                      boolean roundTrip, String connectionNum, LocalTime departTimeStart, LocalTime departTimeEnd) {
        this.departAirport = departAirport;
        this.arriveAirport = arriveAirport;
        this.departDate = departDate;
        this.roundTrip = roundTrip;
        this.connectionNum = connectionNum;
        this.departTimeStart = departTimeStart;
        this.departTimeEnd = departTimeEnd;
    }

    @PostConstruct
    private void initializeDefaultValue() {
        roundTrip = false;
        connectionNum = "2";
        sort = "arrive";
        order = "asc";
    }

}