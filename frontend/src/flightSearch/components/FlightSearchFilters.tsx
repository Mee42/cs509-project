import "./FlightSearchFilters.css";
import AirportSearchFilter from "./AirportSearchFilter";
import FlightDateTimeSelect from "./FlightDateSelect";
import { useEffect, useState, useRef } from "react";
import * as flightSearchAPI from "../flightSearch";
import { FlightSearchQuery } from "../../model/flightSearchQuery";
import { ToastContainer, toast } from "react-toastify";
import Select from "react-select";
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
  const [departureStartTime, setDepartureStartTime] =
    useState<string>("00:00:00");
  const [departureEndTime, setDepartureEndTime] = useState<string>("23:59:00");
  const [returnDate, setReturnDate] = useState<string>("");
  const [returnStartTime, setReturnStartTime] = useState<string>("00:00:00");
  const [returnEndTime, setReturnEndTime] = useState<string>("23:59:00");
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
  const roundTripOptions = ["One-Way", "Round-Trip"];
  const connectionNumOptions = [
    "0 Connections",
    "1 Connection",
    "2 Connections",
  ];

  function packSearchQueries() {
    let searchQueries: FlightSearchQuery[] = [
      new FlightSearchQuery(
        arrivalAirport,
        departureAirport,
        departureDate,
        departureStartTime,
        departureEndTime,
        connectionNum
      ),
    ];

    if (makingRoundTripSelection) {
      searchQueries.push(
        new FlightSearchQuery(
          departureAirport,
          arrivalAirport,
          returnDate,
          returnStartTime,
          returnEndTime,
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
      arrivalAirportsRetrieved.current = true;
      flightSearchAPI.getArrivalAirports(setArrivalAirportList, () => {
        toast("ERROR: Failed to retrieve arrival airports");
      });
    }
  }

  function getDepartureAirports() {
    if (!departureAirportsRetrieved.current) {
      departureAirportsRetrieved.current = true;
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
        <Select
          onChange={() => {
            setMakingRoundTripSelection(!makingRoundTripSelection);
          }}
          options={roundTripOptions.map((selection) => ({
            value: selection,
            label: selection,
          }))}
          defaultValue={{
            label: roundTripOptions[0],
            value: roundTripOptions[0],
          }}
        ></Select>
        <Select
          onChange={(selectedOption) => {
            setConnectionNum(Number(selectedOption?.value[0]));
          }}
          options={connectionNumOptions.map((selection) => ({
            value: selection,
            label: selection,
          }))}
          defaultValue={{
            label: connectionNumOptions[0],
            value: connectionNumOptions[0],
          }}
        ></Select>
      </div>
      <div className="FlightSearchLowerFilters">
        <AirportSearchFilter
          airports={departureAirportList}
          outerLabelText=""
          innerLabelText="From?"
          onSelectAirport={setDepartureAirport}
          inputID={departAirportSearchFilterID}
        ></AirportSearchFilter>
        <AirportSearchFilter
          airports={arrivalAirportList}
          outerLabelText=""
          innerLabelText="To?"
          onSelectAirport={setArrivalAirport}
          inputID={arriveAirportSearchFilterID}
        ></AirportSearchFilter>
        <FlightDateTimeSelect
          labelText="Departure"
          onSelectDate={setDepartureDate}
          onSelectStartTime={setDepartureStartTime}
          onSelectEndTime={setDepartureEndTime}
          startTimeDefaultValue={departureStartTime}
          endTimeDefaultValue={departureEndTime}
          inputID={departureDateFilterID}
        ></FlightDateTimeSelect>
        {makingRoundTripSelection && (
          <FlightDateTimeSelect
            labelText="Return"
            onSelectDate={setReturnDate}
            onSelectStartTime={setReturnStartTime}
            onSelectEndTime={setReturnEndTime}
            startTimeDefaultValue={returnStartTime}
            endTimeDefaultValue={returnEndTime}
            inputID={returnDateFilterID}
          ></FlightDateTimeSelect>
        )}
        <button onClick={handleSearchClick}>Go!</button>
      </div>
    </div>
  );
};

export default FlightSearchFilters;
