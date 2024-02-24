import "./FlightSearchFilters.css";
import AirportSearchFilter from "../AirportSearchFilter/AirportSearchFilter";
import FlightDateSelect from "../FlightDateSelect/FlightDateSelect";
import { useState } from "react";

const FlightSearchFilters = () => {
  // need to get airport list from an API
  let airportList = ["BOS", "NYC", "LAX"];

  const [arrivalDate, setArrivalDate] = useState("");
  const [departureDate, setDepartureDate] = useState("");

  return (
    <div id="FlightSearch">
      <h1 id="FlightSearchHeader">Your Dream Trip Awaits</h1>
      <div id="FlightSearchUpperFilters"></div>

      <div id="FlightSearchLowerFilters">
        <AirportSearchFilter
          airports={airportList}
          labelText="From?"
        ></AirportSearchFilter>
        <AirportSearchFilter
          airports={airportList}
          labelText="To?"
        ></AirportSearchFilter>
        <FlightDateSelect
          labelText="Departure"
          onSelectDate={(dateVal: string) => {
            setDepartureDate(dateVal);
          }}
        ></FlightDateSelect>
        <FlightDateSelect
          labelText="Arrival"
          onSelectDate={(dateVal: string) => {
            setArrivalDate(dateVal);
          }}
        ></FlightDateSelect>
        <button
          onClick={() => {
            console.log([departureDate, arrivalDate]);
          }}
        >
          Go!
        </button>
      </div>
    </div>
  );
};

export default FlightSearchFilters;
