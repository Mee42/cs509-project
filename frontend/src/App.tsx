import FlightSearchFilters from "./components/flightSearch/FlightSearchFilters/FlightSearchFilters";
import Navbar from "./components/general/navbar/navbar";
import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { FlightSearchQuery } from "./model/flightSearchQuery";
import { FlightSelect } from "./components/flightSelect/flightSelect";

function App() {
  const [searchQueries, setSearchQueries] = useState<FlightSearchQuery[]>([]);

  return (
    <>
      <Navbar></Navbar>
      <FlightSearchFilters
        setSearchQueries={setSearchQueries}
      ></FlightSearchFilters>
      <FlightSelect searchQueries={searchQueries}></FlightSelect>
    </>
  );
}

export default App;
