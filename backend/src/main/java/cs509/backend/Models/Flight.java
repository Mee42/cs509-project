package cs509.backend.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "DepartDateTime", nullable = false)
    private LocalDateTime departDateTime;

    @Column(name = "ArriveDateTime", nullable = false)
    private LocalDateTime arriveDateTime;

    @Column(name = "DepartAirport", nullable = false)
    private String departAirport;

    @Column(name = "ArriveAirport", nullable = false)
    private String arriveAirport;

    @Column(name = "FlightNumber", nullable = false)
    private String flightNumber;
}
