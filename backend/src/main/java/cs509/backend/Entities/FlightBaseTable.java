package cs509.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class FlightBaseTable {
// data format: 1,'2023-01-05 01:15:00','2023-01-05 03:50:00','Houston (IAH)','Denver (DEN)','WN309'

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
