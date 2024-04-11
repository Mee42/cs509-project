import "./FlightSearchFilters.css";
import AirportSearchFilter from "./AirportSearchFilter";
import FlightDateSelect from "./FlightDateSelect";
import { useEffect, useState, useRef} from "react";
import * as flightSearchAPI from "../flightSearch";
import { FlightSearchQuery } from "../../model/flightSearchQuery";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

interface Props {
  setSearchQueries: CallableFunction;
}

const FlightSearchFilters = ({ setSearchQueries }: Props) => {
  const [departureAirportList, setDepartureAirportList] = useState<string[]>(
    []
  );
  const [arrivalAirportList, setArrivalAirportList] = useState<string[]>([]);
  const [departureAirport, setDepartureAirport] = useState<string>("");
  const [arrivalAirport, setArrivalAirport] = useState<string>("");
  const [departureDate, setDepartureDate] = useState<string>("");
  const [returnDate, setReturnDate] = useState<string>("");
  const [connectionNum, setConnectionNum] = useState<number>(0);
  const [makingRoundTripSelection, setMakingRoundTripSelection] =
    useState<boolean>(false);
  const errorStyle = "1px solid red";
  const departAirportSearchFilterID = "DepartAirportSearchFilter";
  const arriveAirportSearchFilterID = "ArriveAirportSearchFilter";
  const departureDateFilterID = "DepartureDateFilter";
  const returnDateFilterID = "ReturnDateFilter";
  const arrivalAirportsRetrieved = useRef(false);
  const departureAirportsRetrieved = useRef(false);

  function packSearchQueries() {
    let searchQueries: FlightSearchQuery[] = [
      new FlightSearchQuery(
        arrivalAirport,
        departureAirport,
        departureDate,
        "",
        connectionNum
      ),
    ];

    if (makingRoundTripSelection) {
      searchQueries.push(
        new FlightSearchQuery(
          departureAirport,
          arrivalAirport,
          returnDate,
          "",
          connectionNum
        )
      );
    }

    setSearchQueries(searchQueries);
  }

  function handleSearchClick() {
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
      packSearchQueries();
    }
  }

  function getArrivalAirports() {
    if (!arrivalAirportsRetrieved.current) {
      arrivalAirportsRetrieved.current = true
      flightSearchAPI.getArrivalAirports(setArrivalAirportList, () => {
        toast("ERROR: Failed to retrieve arrival airports");
      });
    }
  }

  function getDepartureAirports() {
    if (!departureAirportsRetrieved.current) {
      departureAirportsRetrieved.current = true
      flightSearchAPI.getDepartureAirports(setDepartureAirportList, () => {
        toast("ERROR: Failed to retrieve departure airports");
      });
    }
  }

  // execute on page load
  useEffect(() => {
    getArrivalAirports();
    getDepartureAirports();
  }, []);

  return (
    <div className="FlightSearch">
      <ToastContainer position="top-center" />
      <h1 className="FlightSearchHeader">Your Dream Trip Awaits</h1>
      <div className="FlightSearchUpperFilters">
        <select
          onChange={() => {
            setMakingRoundTripSelection(!makingRoundTripSelection);
          }}
        >
          <option>One-Way</option>
          <option>Round Trip</option>
        </select>
        <select
          onChange={(event) => {
            setConnectionNum(Number(event.target.value));
          }}
        >
          <option>0</option>
          <option>1</option>
          <option>2</option>
        </select>
      </div>
      <div className="FlightSearchLowerFilters">
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
        {makingRoundTripSelection && (
          <FlightDateSelect
            labelText="Return"
            onSelectDate={setReturnDate}
            inputID={returnDateFilterID}
          ></FlightDateSelect>
        )}
        <button onClick={handleSearchClick}>Go!</button>
      </div>
    </div>
  );
};

export default FlightSearchFilters;
