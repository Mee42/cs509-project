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

    //DTO class to map form submit from frontend to backend - name and case are important here

    private String departAirport; //required
    private String arriveAirport; //required

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arriveDate;

    private String classSeat;  // default - Economy
    private boolean roundTrip; // default - false - no round trip
    private String connectionNum; // default - all - accept (0, 1, 2, all)

    // only used if outbound depart date is specified
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departTimeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departTimeEnd;
    // only used if outbound arrive date is specified
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime arriveTimeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime arriveTimeEnd;

    // only used if round trip is true
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate roundTripDepartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate roundTripArriveDate;

    // only used if round trip is true and round trip depart date is specified
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime roundTripDepartTimeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime roundTripDepartTimeEnd;
    // only used if round trip is true and round trip arrive date is specified
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime roundTripArriveTimeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime roundTripArriveTimeEnd;

    public FlightForm(String departAirport, String arriveAirport, LocalDate departDate, LocalDate arriveDate,
                      String classSeat, boolean roundTrip, String connectionNum, LocalTime departTimeStart,
                      LocalTime departTimeEnd, LocalTime arriveTimeStart, LocalTime arriveTimeEnd) {
        this.departAirport = departAirport;
        this.arriveAirport = arriveAirport;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
        this.classSeat = classSeat;
        this.roundTrip = roundTrip;
        this.connectionNum = connectionNum;
        this.departTimeStart = departTimeStart;
        this.departTimeEnd = departTimeEnd;
        this.arriveTimeStart = arriveTimeStart;
        this.arriveTimeEnd = arriveTimeEnd;
    }


    @PostConstruct
    private void initializeDefaultValue() {
        classSeat = "Economy";
        roundTrip = false;
        connectionNum = "All";
    }

}