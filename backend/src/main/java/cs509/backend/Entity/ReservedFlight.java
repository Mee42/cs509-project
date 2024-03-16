package cs509.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ReservedFlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "FlightId", nullable = false)
    private int flightId;

    @Column(name = "FlightTable", nullable = false)
    private String flightTable;

    public ReservedFlight(int flightId, String flightTable) {
        this.flightId = flightId;
        this.flightTable = flightTable;
    }
}
