import FlightSearchFilters from "./components/flightSearch/FlightSearchFilters/FlightSearchFilters";
import Navbar from "./components/general/navbar/navbar";
import { useState } from "react";
import { TripCard } from "./components/flightSelect/TripCard";
import { Flight } from "./model/flight";
import "./App.css";

function App() {
  const [trips, setTrips] = useState<Flight[][]>([]);

  return (
    <>
      <Navbar></Navbar>
      <FlightSearchFilters setTrips={setTrips}></FlightSearchFilters>
      <div className="TripCardContainer">
        {trips.length ? (
          trips.map((trip: Flight[], index: number) => {
            return <TripCard trip={trip} key={index}></TripCard>;
          })
        ) : (
          <></>
        )}
      </div>
    </>
  );
}

export default App;
