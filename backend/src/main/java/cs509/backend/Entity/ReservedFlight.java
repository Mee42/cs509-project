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

    public ReservedFlight(int flightId) {
        this.flightId = flightId;
    }
}
