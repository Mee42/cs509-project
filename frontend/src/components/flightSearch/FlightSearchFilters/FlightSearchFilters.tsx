import "./FlightSearchFilters.css";
import AirportSearchFilter from "../AirportSearchFilter/AirportSearchFilter";
import FlightDateSelect from "../FlightDateSelect/FlightDateSelect";
import { useEffect, useState } from "react";
import * as flightSearchAPI from "../../../api/flightSearch";

interface Props {
  setTrips: CallableFunction;
  makingRoundTripSelection: boolean; 
  setMakingRoundTripSelection: CallableFunction
}

const FlightSearchFilters = ({ setTrips, makingRoundTripSelection, setMakingRoundTripSelection }: Props) => {
  // need to get airport list from an API
  const [departureAirportList, setDepartureAirportList] = useState<string[]>(
    []
  );
  const [arrivalAirportList, setArrivalAirportList] = useState<string[]>([]);
  const [departureAirport, setDepartureAirport] = useState<string>("");
  const [arrivalAirport, setArrivalAirport] = useState<string>("");
  const [departureDate, setDepartureDate] = useState<string>("");
  const [returnDate, setReturnDate] = useState<string>("");
  const errorStyle = "1px solid red";
  const departAirportSearchFilterID = "DepartAirportSearchFilter";
  const arriveAirportSearchFilterID = "ArriveAirportSearchFilter";
  const departureDateFilterID = "DepartureDateFilter";
  const returnDateFilterID = "ReturnDateFilter";

  async function handleSearchClick() {
    console.log(
      `Search filters: Departure ${departureAirport} ${departureDate}, Arrival ${arrivalAirport}`
    );
    let missingData = false;
    if (departureAirport.length == 0) {
      document.getElementById(departAirportSearchFilterID)!.style.outline =
        errorStyle;
      missingData = true;
    }
    if (arrivalAirport.length == 0) {
      document.getElementById(arriveAirportSearchFilterID)!.style.outline =
        errorStyle;
      missingData = true;
    }
    if (departureDate.length == 0) {
      document.getElementById(departureDateFilterID)!.style.outline =
        errorStyle;
      missingData = true;
    }
    if (makingRoundTripSelection && returnDate.length == 0) {
      document.getElementById(returnDateFilterID)!.style.outline = errorStyle;
      missingData = true;
    }

    if (!missingData) {
      flightSearchAPI.getTrips(
        departureAirport,
        arrivalAirport,
        departureDate,
        2,
        1,
        setTrips
      );
    }
  }

  // execute on page load
  useEffect(() => {
    flightSearchAPI.getArrivalAirports(setArrivalAirportList);
    flightSearchAPI.getDepartureAirports(setDepartureAirportList);
  }, []);

  return (
    <div className="FlightSearch">
      <h1 className="FlightSearchHeader">Your Dream Trip Awaits</h1>
      <div className="FlightSearchUpperFilters">
        <select
          onChange={() => {
            setMakingRoundTripSelection(!makingRoundTripSelection);
            setTrips({});
          }}
        >
          <option value={1}>One-Way</option>
          <option value={2}>Round Trip</option>
        </select>
      </div>
      <div className="FlightSearchLowerFilters">
        <AirportSearchFilter
          airports={departureAirportList}
          labelText="From?"
          onSelectAirport={setDepartureAirport}
          inputID={departAirportSearchFilterID}
          defaultInputValue="Atlanta (ATL)"
        ></AirportSearchFilter>
        <AirportSearchFilter
          airports={arrivalAirportList}
          labelText="To?"
          onSelectAirport={setArrivalAirport}
          inputID={arriveAirportSearchFilterID}
          defaultInputValue="Tucson (TUS)"
        ></AirportSearchFilter>
        <FlightDateSelect
          labelText="Departure"
          onSelectDate={setDepartureDate}
          inputID={departureDateFilterID}
          defaultInputValue={"2023-01-01"}
        ></FlightDateSelect>
        {makingRoundTripSelection && (
          <FlightDateSelect
            labelText="Return"
            onSelectDate={setReturnDate}
            inputID={returnDateFilterID}
            defaultInputValue={"2023-01-01"}
          ></FlightDateSelect>
        )}
        <button onClick={handleSearchClick}>Go!</button>
      </div>
    </div>
  );
};

export default FlightSearchFilters;
