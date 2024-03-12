import FlightSearchFilters from "./components/flightSearch/FlightSearchFilters/FlightSearchFilters";
import Navbar from "./components/general/navbar/navbar";
import { useEffect, useState } from "react";
import { TripCard } from "./components/flightSelect/TripCard";
import { Flight } from "./model/flight";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

function App() {
  const [trips, setTrips] = useState<Flight[][]>([]);
  const [selectedTrips, setSelectedTrips] = useState<Flight[][]>([]);
  const [makingRoundTripSelection, setMakingRoundTripSelection] =
    useState<boolean>(false);

  // clear trip selection on round trip selection change
  useEffect(() => {
    setSelectedTrips([]);
  }, [makingRoundTripSelection]);

  function isTripSelectionFull() {
    return (
      selectedTrips.length > 1 ||
      (!makingRoundTripSelection && selectedTrips.length > 0)
    );
  }

  function addTripToSelectedTrips(trip: Flight[]) {
    setSelectedTrips((s) => [...s, trip]);
  }

  function addTripToTripsToSelect(trip: Flight[]) {
    setTrips((t) => [...t, trip]);
  }

  function removeTripFromTripsToSelect(idx: number) {
    setTrips(trips.filter((_, i) => i !== idx));
  }

  function removeTripFromSelectedTrips(idx: number) {
    setSelectedTrips(selectedTrips.filter((_, i) => i !== idx));
  }

  function handleTripSelect(idx: number) {
    if (!isTripSelectionFull()) {
      addTripToSelectedTrips(trips[idx]);
      removeTripFromTripsToSelect(idx);
    }
  }

  function getTripCardsForTripsToSelect() {
    return (
      trips.length > 0 &&
      trips.map((trip: Flight[], idx: number) => {
        return (
          <TripCard
            trip={trip}
            key={idx}
            onClick={() => {
              handleTripSelect(idx);
            }}
          ></TripCard>
        );
      })
    );
  }

  function getTripCardsForSelectedTrips() {
    return (
      selectedTrips.length > 0 &&
      selectedTrips.map((trip: Flight[], idx: number) => {
        return (
          <TripCard
            trip={trip}
            key={idx}
            onClick={() => {
              addTripToTripsToSelect(trip);
              removeTripFromSelectedTrips(idx);
            }}
          ></TripCard>
        );
      })
    );
  }

  return (
    <>
      <Navbar></Navbar>
      <FlightSearchFilters
        setTrips={setTrips}
        makingRoundTripSelection={makingRoundTripSelection}
        setMakingRoundTripSelection={setMakingRoundTripSelection}
      ></FlightSearchFilters>
      <div className="TripCardContainer SelectedTrips">
        {getTripCardsForSelectedTrips()}
      </div>
      <div className="TripCardContainer">{getTripCardsForTripsToSelect()}</div>
    </>
  );
}

export default App;
