import "./App.css";
import FlightSearchFilters from "./flightSearch/components/FlightSearchFilters";
import Navbar from "./general/components/navbar";
import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { FlightSearchQuery } from "./model/flightSearchQuery";
import { FlightSelect } from "./flightSelect/components/flightSelect";

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
