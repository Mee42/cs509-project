import "./flightSelect.css";
import { useEffect, useState } from "react";
import { TripCard } from "./TripCard";
import { Flight } from "../../model/flight";
import * as flightSearchAPI from "../../api/flightSearch";
import { FlightSearchQuery } from "../../model/flightSearchQuery";
import { FlightSelectFilterButtons } from "./flightSelectFilterButtons";
import { FlightSelectFilterOption } from "../../model/flightSelectFilterOption";
import { submitReservation } from "../../api/flightReservation";

interface Props {
  searchQueries: FlightSearchQuery[];
}

export function FlightSelect({ searchQueries }: Props) {
  const filterOptions = [
    new FlightSelectFilterOption("Departure Date", "Depart"),
    new FlightSelectFilterOption("Arrival Date", "Arrive"),
    new FlightSelectFilterOption("Travel Time", "TravelTime"),
  ];

  const [trips, setTrips] = useState<Flight[][]>([]);
  const [selectedTrips, setSelectedTrips] = useState<Flight[][]>([]);
  const [selectedFilter, setSelectedFilter] = useState(filterOptions[0]);
  const [currentBatchNum, setCurrentBatchNum] = useState(1);

  function resetFlightSelection() {
    if (trips.length > 0) setTrips([]);
    if (selectedTrips.length > 0) setSelectedTrips([]);
  }

  // clear selection & reset selectable trips when new search criteria is submitted
  useEffect(() => {
    console.log("search critieria changed:" + searchQueries);
    resetFlightSelection();
    getTripsFromCurrentSelections();
  }, [searchQueries]);

  // get next set of trips to select when selected trips changes
  useEffect(() => {
    console.log("selected trips changed:" + selectedTrips);
    if (selectedTrips.length != searchQueries.length)
      getTripsFromCurrentSelections();
    else {
      setTrips([]);
    }
  }, [selectedTrips]);

  // update trips on filter change
  useEffect(() => {
    console.log("filter changed: " + selectedFilter.value);
    getTripsFromCurrentSelections();
  }, [selectedFilter]);

  function getTripsFromCurrentSelections() {
    flightSearchAPI.getTrips(
      searchQueries[selectedTrips.length],
      currentBatchNum,
      setTrips,
      selectedFilter.value
    );
  }

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

  function handleReservationClick() {
    console.log("reservation clicked");
    // selectedTrips.map((trip) => {
    //   submitReservation(trip);
    // });
  }

  return (
    <div className="FlightSelectContainer">
      {searchQueries.length > 0 && (
        <FlightSelectFilterButtons
          filterOptions={filterOptions}
          onChange={setSelectedFilter}
          selectedOption={selectedFilter}
        />
      )}
      <div className="TripSelectContainer">
        <div className="TripCardContainer SelectedTrips">
          {getTripCardsForSelectedTrips()}
        </div>
        {selectedTrips.length > 0 && (
          <button onClick={removeLastSelectedTrip}>
            Remove Previous Selection
          </button>
        )}
        <div className="TripCardContainer TripsToSelect">
          {getTripCardsForTripsToSelect()}
        </div>
      </div>
      {isTripSelectionFull() && (
        <button onClick={handleReservationClick}>Make Reservation</button>
      )}
    </div>
  );
}
