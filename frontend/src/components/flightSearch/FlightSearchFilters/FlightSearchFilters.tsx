import "./FlightSearchFilters.css";
import AirportSearchFilter from "../AirportSearchFilter/AirportSearchFilter";
import FlightDateSelect from "../FlightDateSelect/FlightDateSelect";

const FlightSearchFilters = () => {
  // need to get airport list from an API
  let airportList = ["BOS", "NYC", "LAX"];
  return (
    <>
      <h1>Your Dream Trip Awaits</h1>
      <div className="FlightSearchFilters">
        <AirportSearchFilter
          airports={airportList}
          labelText="From: "
        ></AirportSearchFilter>
        <AirportSearchFilter
          airports={airportList}
          labelText="To: "
        ></AirportSearchFilter>
        <FlightDateSelect labelText="Departure Date"></FlightDateSelect>
        <FlightDateSelect labelText="Arrival Date"></FlightDateSelect>
        <button>Go!</button>
      </div>
    </>
  );
};

export default FlightSearchFilters;
