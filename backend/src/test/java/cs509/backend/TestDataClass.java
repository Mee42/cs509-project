package cs509.backend;

import cs509.backend.Data.Flight;
import cs509.backend.Data.FlightOneConnection;
import cs509.backend.Data.FlightTwoConnection;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

// Testing the rest of public methods of data access class
@TestComponent
public class TestDataClass {
    @Test
    public void testFlightGetFlights() {
        Flight t = new Flight("start", "final", LocalDateTime.now(), LocalDateTime.now(), "num");
        Flight[] f = t.getFlights();
        assertThat(f.length).isEqualTo(1);
        assertThat(f[0]).usingRecursiveComparison().isEqualTo(t);
    }

    @Test
    public void testFlightOneConnectionGetFlights() {
        LocalDateTime time = LocalDateTime.now();
        FlightOneConnection t = new FlightOneConnection("start", "conn1", "final", time,
                time, time, time, "num1", "num2");
        Flight[] f = t.getFlights();
        assertThat(f.length).isEqualTo(2);
        assertThat(f[0]).usingRecursiveComparison().isEqualTo(new Flight("start", "conn1", time, time, "num1"));
        assertThat(f[1]).usingRecursiveComparison().isEqualTo(new Flight("conn1", "final", time, time, "num2"));
    }

    @Test
    public void testFlightTwoConnectionsGetFlights() {
        LocalDateTime time = LocalDateTime.now();
        FlightTwoConnection t = new FlightTwoConnection("start", "conn1", "conn2", "final",
                time, time, time, time, time, time, "num1", "num2", "num3");
        Flight[] f = t.getFlights();
        assertThat(f.length).isEqualTo(3);
        assertThat(f[0]).usingRecursiveComparison().isEqualTo(new Flight("start", "conn1", time, time, "num1"));
        assertThat(f[1]).usingRecursiveComparison().isEqualTo(new Flight("conn1", "conn2", time, time, "num2"));
        assertThat(f[2]).usingRecursiveComparison().isEqualTo(new Flight("conn2", "final", time, time, "num3"));
    }
}
