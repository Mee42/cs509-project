package cs509.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "deltas")
public class FlightDeltas extends Flight {
}
