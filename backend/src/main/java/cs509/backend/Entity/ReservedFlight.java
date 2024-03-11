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

    @Column(name = "FlightIds", nullable = false)
    private int flightIds;

    @Column(name = "ClientName", nullable = false)
    private String name;

    public ReservedFlight(int flightIds, String name) {
        this.flightIds = flightIds;
        this.name = name;
    }
}
