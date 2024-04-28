import {
  arrivalAirportsEndpoint,
  departureAirportsEndpoint,
  flightSearchEndpoint,
} from "../endpoints";
import axios from "axios";
import { FlightSearchQuery } from "../model/flightSearchQuery";
import { toast } from "react-toastify";

async function getAirports(
  setAirportList: CallableFunction,
  airportsEndpoint: string,
  errorFunc: CallableFunction
) {
  let airportsFetched = false;

  new Promise((r) => setTimeout(r, 3000)).then(() => {
    if (!airportsFetched) {
      toast("System is working...");
    }
  });

  await axios
    .get(airportsEndpoint, { timeout: 10000 })
    .then((response) => {
      airportsFetched = true;
      setAirportList(response.data);
    })
    .catch(() => {
      airportsFetched = true;
      errorFunc();
    });
}

export function getDepartureAirports(
  setDepartureAirportList: CallableFunction,
  errorFunc: CallableFunction
) {
  return getAirports(
    setDepartureAirportList,
    departureAirportsEndpoint,
    errorFunc
  );
}

export function getArrivalAirports(
  setArrivalAirportList: CallableFunction,
  errorFunc: CallableFunction
) {
  return getAirports(setArrivalAirportList, arrivalAirportsEndpoint, errorFunc);
}

export async function getTrips(
  searchQuery: FlightSearchQuery,
  batchNum: number,
  setTrips: CallableFunction,
  sortMethod: string,
  errorFunc: CallableFunction = () => {}
) {
  let tripsFetched = false;

  new Promise((r) => setTimeout(r, 3000)).then(() => {
    if (!tripsFetched) {
      toast("System is working...");
    }
  });

  return await axios
    .post(
      flightSearchEndpoint + "/" + batchNum,
      {
        departAirport: searchQuery.departAirport,
        arriveAirport: searchQuery.arriveAirport,
        departDate: searchQuery.date,
        connectionNum: searchQuery.connectionNum,
        departTimeStart: searchQuery.startTime,
        departTimeEnd: searchQuery.endTime,
        sort: sortMethod,
        order: "ASC",
      },
      { timeout: 10000 }
    )
    .then((response) => {
      tripsFetched = true;
      const trips = response.data["outbound"];
      console.log(trips);
      setTrips(trips);
    })
    .catch((error) => {
      tripsFetched = true;
      console.log(error);
      errorFunc();
    });
}
