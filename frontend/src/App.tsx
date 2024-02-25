import FlightSearchFilters from "./components/flightSearch/FlightSearchFilters/FlightSearchFilters";
import Navbar from "./components/general/navbar/navbar";
import { useState } from "react";
import { FlightCard, Flight } from "./components/flightSelect/FlightCard";

function App() {
  const [flights, setFlights] = useState<Flight[]>([]);

  return (
    <>
      <Navbar></Navbar>
      <FlightSearchFilters setFlights={setFlights}></FlightSearchFilters>
      <div className="FlightCardContainer">
        {flights.length ? (
          flights.map((flight: Flight) => {
            return <FlightCard flight={flight} key={flight.id}></FlightCard>;
          })
        ) : (
          <></>
        )}
      </div>
    </>
  );
}

export default App;
