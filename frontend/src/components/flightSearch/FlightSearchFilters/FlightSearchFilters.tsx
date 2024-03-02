import "./FlightSearchFilters.css";
import AirportSearchFilter from "../AirportSearchFilter/AirportSearchFilter";
import FlightDateSelect from "../FlightDateSelect/FlightDateSelect";
import { useEffect, useState } from "react";
import * as flightSearchAPI from "../../../api/flightSearch";

interface Props {
  setTrips: CallableFunction;
}

const FlightSearchFilters = ({ setTrips }: Props) => {
  // need to get airport list from an API
  const [departureAirportList, setDepartureAirportList] = useState<string[]>(
    []
  );
  const [arrivalAirportList, setArrivalAirportList] = useState<string[]>([]);
  const [departureAirport, setDepartureAirport] = useState<string>("");
  const [arrivalAirport, setArrivalAirport] = useState<string>("");
  const [departureDate, setDepartureDate] = useState<string>("");
  const errorStyle = "1px solid red";
  const departAirportSearchFilterID = "DepartAirportSearchFilter";
  const arriveAirportSearchFilterID = "ArriveAirportSearchFilter";
  const departureDateFilterID = "DepartureDateFilter";

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

    if (!missingData) {
      flightSearchAPI.getTrips(
        departureAirport,
        arrivalAirport,
        departureDate,
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
    <div id="FlightSearch">
      <h1 id="FlightSearchHeader">Your Dream Trip Awaits</h1>
      <div id="FlightSearchUpperFilters"></div>
      {/* Round trip & Coach filters go here */}
      <div id="FlightSearchLowerFilters">
        <AirportSearchFilter
          airports={departureAirportList}
          labelText="From?"
          onSelectAirport={setDepartureAirport}
          inputID={departAirportSearchFilterID}
        ></AirportSearchFilter>
        <AirportSearchFilter
          airports={arrivalAirportList}
          labelText="To?"
          onSelectAirport={setArrivalAirport}
          inputID={arriveAirportSearchFilterID}
        ></AirportSearchFilter>
        <FlightDateSelect
          labelText="Departure"
          onSelectDate={setDepartureDate}
          inputID={departureDateFilterID}
        ></FlightDateSelect>
        <button onClick={handleSearchClick}>Go!</button>
      </div>
    </div>
  );
};

export default FlightSearchFilters;
