import {
  arrivalAirportsEndpoint,
  departureAirportsEndpoint,
  flightSearchEndpoint,
} from "../endpoints";
import axios from "axios";
import { FlightSearchQuery } from "../model/flightSearchQuery";

async function getAirports(
  setAirportList: CallableFunction,
  airportsEndpoint: string,
  errorFunc: CallableFunction
) {
  await axios
    .get(airportsEndpoint, { timeout: 5000 })
    .then((response) => {
      setAirportList(response.data);
    })
    .catch(() => {
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
  return await axios
    .post(flightSearchEndpoint + "/" + batchNum, {
      departAirport: searchQuery.departAirport,
      arriveAirport: searchQuery.arriveAirport,
      departDate: searchQuery.date,
      connectionNum: searchQuery.connectionNum,
      departTimeStart: searchQuery.startTime,
      departTimeEnd: searchQuery.endTime,
      sort: sortMethod,
      order: "ASC",
      timeout: 5000,
    })
    .then((response) => {
      const trips = response.data["outbound"];
      console.log(trips);
      setTrips(trips);
    })
    .catch((error) => {
      console.log(error);
      errorFunc();
    });
}
