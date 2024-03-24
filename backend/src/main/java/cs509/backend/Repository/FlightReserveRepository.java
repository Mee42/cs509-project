package cs509.backend.Repository;

import cs509.backend.Entity.ReservedFlight;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlightReserveRepository {
    private final JdbcClient jdbcClient;

    public FlightReserveRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Modifying
    public void saveAll(List<ReservedFlight> flights) {
        for (ReservedFlight r : flights) {
            String sql = "INSERT INTO ReservedFlight (FlightId, FlightTable) VALUES (:flightId, :flightTable);";
            jdbcClient.sql(sql)
                    .paramSource(r)
                    .update();
        }
    }

    public Integer count() {
        String sql = "SELECT COUNT(*) AS total_rows FROM ReservedFlight;";
        return jdbcClient.sql(sql).query(Integer.class).single();
    }
}
