package cs509.backend.Data;

import cs509.backend.Enum.OrderBy;
import cs509.backend.Enum.SortBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@AllArgsConstructor
public class FlightForm implements Form {

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

    private SortBy sort = SortBy.Arrive;// default arrive - accept (depart, arrive, travelTime)
    private OrderBy order = OrderBy.ASC;   // default asc - accept (asc, desc)


    @Override
    public String checkAllFields() {
        if (departAirport.isEmpty() || arriveAirport.isEmpty()) {
            return "Depart Airport or Arrive Airport empty";
        }
        if (departTimeStart != null && departTimeEnd != null && departTimeStart.isAfter(departTimeEnd)) {
            return "Depart Time Start is after Depart Time End";
        }
        try {
            int temp = Integer.parseInt(connectionNum);
            if (temp > 2 || temp < 0)
                return "Unexpected value for connection number";
        } catch(NumberFormatException e) {
            return "Unexpected value for connection number";
        }
        return null;
    }

}