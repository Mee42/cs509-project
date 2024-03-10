package cs509.backend.Data;

import cs509.backend.Enum.OrderBy;
import cs509.backend.Enum.SortBy;
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

    private boolean roundTrip = false;  // default - false - no round trip
    private String connectionNum = "2"; // default 2 - accept (0, 1, 2)

    // only used if depart date is specified
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departTimeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departTimeEnd;

    private String sort = "arrive"; // default arrive - accept (depart, arrive, travelTime)
    private String order = "asc";   // default asc - accept (asc, desc)

    public SortBy getSort() {
        return (sort.equals("depart")) ? SortBy.Depart : (sort.equals("travelTime")) ? SortBy.TravelTime : SortBy.Arrive;
    }

    public OrderBy getOrder() {
        return (order.equals("asc")) ? OrderBy.ASC : OrderBy.DESC;
    }

}