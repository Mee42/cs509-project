import "./flightSelect.css";
import { useEffect, useState } from "react";
import { TripCard } from "./TripCard";
import { Flight } from "../../model/flight";
import * as flightSearchAPI from "../../api/flightSearch";
import "bootstrap/dist/css/bootstrap.min.css";
import { FlightSearchQuery } from "../../model/flightSearchQuery";

interface Props {
  searchQueries: FlightSearchQuery[];
}

export function FlightSelect({ searchQueries }: Props) {
  const [trips, setTrips] = useState<Flight[][]>([]);
  const [selectedTrips, setSelectedTrips] = useState<Flight[][]>([]);

  function resetFlightSelection() {
    setTrips([]);
    setSelectedTrips([]);
  }

  useEffect(() => {
    resetFlightSelection();
    flightSearchAPI.getTrips(searchQueries[0], 1, setTrips);
  }, [searchQueries]);

  useEffect(() => {
    if (selectedTrips.length != searchQueries.length)
      flightSearchAPI.getTrips(
        searchQueries[selectedTrips.length],
        1,
        setTrips
      );
    else {
      setTrips([]);
    }
  }, [selectedTrips]);

  function isTripSelectionFull() {
    return (
      selectedTrips.length > searchQueries.length - 1 &&
      selectedTrips.length > 0
    );
  }

  function addTrip(tripSetter: CallableFunction, trip: Flight[]) {
    tripSetter((t: Flight[][]) => [...t, trip]);
  }

  function removeTrip(tripSetter: CallableFunction, idx: number) {
    tripSetter((t: Flight[][]) => t.filter((_, i) => i !== idx));
  }

  function handleTripSelect(idx: number) {
    if (!isTripSelectionFull()) {
      addTrip(setSelectedTrips, trips[idx]);
      removeTrip(setTrips, idx);
    }
  }

  function removeLastSelectedTrip() {
    addTrip(setTrips, selectedTrips.slice(-1)[0]);
    removeTrip(setSelectedTrips, selectedTrips.length - 1);
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
        return <TripCard trip={trip} key={idx}></TripCard>;
      })
    );
  }

  return (
    <div className="TripSelectContainer">
      <div className="TripCardContainer SelectedTrips">
        {getTripCardsForSelectedTrips()}
      </div>
      {selectedTrips.length > 0 && (
        <button onClick={removeLastSelectedTrip}>
          Remove Previous Selection
        </button>
      )}
      <div className="TripCardContainer">{getTripCardsForTripsToSelect()}</div>
      {isTripSelectionFull() && <button>Make Reservation</button>}
    </div>
  );
}
