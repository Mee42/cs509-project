import "./FlightSearchFilters.css";
import AirportSearchFilter from "../AirportSearchFilter/AirportSearchFilter";
import FlightDateSelect from "../FlightDateSelect/FlightDateSelect";
import { useEffect, useState } from "react";
import axios from "axios";
import {
  arrivalAirportsEndpoint,
  departureAirportsEndpoint,
  flightSearchEndpoint,
} from "../../../definitions";

interface Props {
  setFlights: CallableFunction;
}

const FlightSearchFilters = ({ setFlights }: Props) => {
  // need to get airport list from an API
  const [departureAirportList, setDepartureAirportList] = useState<string[]>(
    []
  );
  const [arrivalAirportList, setArrivalAirportList] = useState<string[]>([]);
  const [departureAirport, setDepartureAirport] = useState<string>("");
  const [arrivalAirport, setArrivalAirport] = useState<string>("");
  const [arrivalDate, setArrivalDate] = useState<string>("");
  const [departureDate, setDepartureDate] = useState<string>("");

  async function getArrivalAirports() {
    await axios
      .get(arrivalAirportsEndpoint)
      .then((response) => {
        setArrivalAirportList(response.data);
      })
      .catch((error) => {
        console.error(`failed to get arrival airports: ${error}`);
      });
  }

  async function getDepartureAirports() {
    await axios
      .get(departureAirportsEndpoint)
      .then((response) => {
        setDepartureAirportList(response.data);
      })
      .catch((error) => {
        console.error(`failed to get departure airports: ${error}`);
      });
  }

  async function handleSearchClick() {
    console.log(
      `Search filters: Departure ${departureAirport} ${departureDate}, Arrival ${arrivalAirport} ${arrivalDate}`
    );
    await axios
      .post(flightSearchEndpoint + "/10", {
        departAirport: "Atlanta (ATL)",
        arriveAirport: "Tucson (TUS)",
        departDate: "2023-01-05",
        arriveDate: "2023-01-06",
        connectionNum: "All",
      })
      .then((response) => {
        setFlights(response.data);
      })
      .catch((error) => {
        console.error(`failed to get flights: ${error}`);
      });
  }

  // execute on page load
  useEffect(() => {
    getArrivalAirports();
    getDepartureAirports();
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
        ></AirportSearchFilter>
        <AirportSearchFilter
          airports={arrivalAirportList}
          labelText="To?"
          onSelectAirport={setArrivalAirport}
        ></AirportSearchFilter>
        <FlightDateSelect
          labelText="Departure"
          onSelectDate={setDepartureDate}
        ></FlightDateSelect>
        <FlightDateSelect
          labelText="Arrival"
          onSelectDate={setArrivalDate}
        ></FlightDateSelect>
        <button onClick={handleSearchClick}>Go!</button>
      </div>
    </div>
  );
};

export default FlightSearchFilters;
