package cs509.backend.Data;

public record FlightReserveForm(Flight[] flights) implements Form {
    @Override
    public String checkAllFields() {
        if (flights == null)
            return "No flights retrieved, got null";
        else if (flights.length == 0)
            return "No flights retrieved, got 0";
        return null;
    }
}
