import "./flightSelect.css";
import usePrevious from "use-previous";
import { useEffect, useState } from "react";
import { TripCard } from "./TripCard";
import { Flight } from "../../model/flight";
import * as flightSearchAPI from "../../flightSearch/flightSearch";
import { FlightSearchQuery } from "../../model/flightSearchQuery";
import { FlightSelectFilterButtons } from "./flightSelectFilterButtons";
import { FlightSelectFilterOption } from "../../model/flightSelectFilterOption";
import { submitReservation } from "../flightReservation";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

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
  const [nextTripBatch, setNextTripBatch] = useState<Flight[][]>([]);
  const [selectedTrips, setSelectedTrips] = useState<Flight[][]>([]);
  const [selectedFilter, setSelectedFilter] = useState(filterOptions[0]);
  const [currentBatchNum, setCurrentBatchNum] = useState(1);
  const prevBatchNum = usePrevious(currentBatchNum);

  // skip useEffect trigger on first mount
  const [didMount, setDidMount] = useState(false);
  useEffect(() => {
    setDidMount(true);
  }, []);

  function resetFlightSelection() {
    if (selectedTrips.length > 0) setSelectedTrips([]);
  }

  function goBackToFirstBatch() {
    setCurrentBatchNum(1);
    getTripsFromCurrentSelections();
    getNextTripBatch();
  }

  // clear selection & reset selectable trips when new search criteria is submitted
  useEffect(() => {
    if (didMount) {
      console.log("search critieria changed:" + searchQueries);
      resetFlightSelection();
      goBackToFirstBatch();
    }
  }, [searchQueries]);

  // get next set of trips to select when selected trips changes
  useEffect(() => {
    if (didMount) {
      console.log("selected trips changed:" + selectedTrips);
      if (selectedTrips.length != searchQueries.length)
        getTripsFromCurrentSelections();
      else {
        setTrips([]);
      }
    }
  }, [selectedTrips]);

  // update trips on filter change
  useEffect(() => {
    if (didMount) {
      console.log("filter changed: " + selectedFilter.value);
      goBackToFirstBatch();
    }
  }, [selectedFilter]);

  useEffect(() => {
    if (
      trips.length === 0 &&
      searchQueries.length > 0 &&
      !isTripSelectionFull()
    ) {
      console.log("no trips");
      toast("No trips found");
    }
  }, [trips]);

  useEffect(() => {
    if (didMount) {
      if (currentBatchNum > prevBatchNum) {
        setTrips(nextTripBatch);
        getNextTripBatch();
      } else {
        getTripsFromCurrentSelections();
        getNextTripBatch();
      }
    }
  }, [currentBatchNum]);

  function getTripsFromCurrentSelections() {
    flightSearchAPI.getTrips(
      searchQueries[selectedTrips.length],
      currentBatchNum,
      setTrips,
      selectedFilter.value,
      () => {
        toast("ERROR: Failed to get trips");
      }
    );
  }

  function getNextTripBatch() {
    let nextBatchNum = currentBatchNum + 1;
    console.log("getting batch " + nextBatchNum);
    flightSearchAPI.getTrips(
      searchQueries[selectedTrips.length],
      nextBatchNum,
      setNextTripBatch,
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
            id={"SelectableTripCard" + idx}
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
        return <TripCard trip={trip} id={"SelectedTripCard" + idx} key={idx}></TripCard>;
      })
    );
  }

  function handleReservationClick() {
    console.log("reservation clicked");
    selectedTrips.map((trip) => {
      submitReservation(
        trip,
        () => {
          toast("Reservation Success!");
        },
        () => {
          toast("ERROR: Failed to submit reservation");
        }
      );
    });
  }

  function decrementBatchNumber() {
    if (currentBatchNum != 1) {
      setCurrentBatchNum(currentBatchNum - 1);
    }
  }

  function incrementBatchNumber() {
    if (nextTripBatch.length != 0) {
      setCurrentBatchNum(currentBatchNum + 1);
    }
  }

  return (
    <div className="FlightSelectContainer">
      {searchQueries.length > 0 && (
        <div className="FlightSelectModifiers">
          {currentBatchNum != 1 && (
            <button className="BatchButton" onClick={decrementBatchNumber}>
              {"<"}
            </button>
          )}
          <FlightSelectFilterButtons
            filterOptions={filterOptions}
            onChange={setSelectedFilter}
            selectedOption={selectedFilter}
          />
          {nextTripBatch.length > 0 && (
            <button className="BatchButton" onClick={incrementBatchNumber}>
              {">"}
            </button>
          )}
        </div>
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
