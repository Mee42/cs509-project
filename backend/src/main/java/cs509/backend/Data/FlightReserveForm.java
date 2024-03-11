package cs509.backend.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlightReserveForm implements Form {
    private final Flight[] flights;
    private String name;

    @Override
    public String checkAllFields() {
        if (flights == null)
            return "No flights retrieved, got null";
        else if (flights.length == 0)
            return "No flights retrieved, got 0";
        if (name.isEmpty())
            return "Client's name is empty";
        return null;
    }
}
