import {
  arrivalAirportsEndpoint,
  departureAirportsEndpoint,
  flightSearchEndpoint,
} from "../definitions";
import axios from "axios";
import { FlightSearchQuery } from "../model/flightSearchQuery";

async function getAirports(
  setAirportList: CallableFunction,
  airportsEndpoint: string
) {
  await axios
    .get(airportsEndpoint)
    .then((response) => {
      setAirportList(response.data);
    })
    .catch((error) => {
      console.error(`failed to get airports: ${error}`);
    });
}

export function getDepartureAirports(
  setDepartureAirportList: CallableFunction
) {
  getAirports(setDepartureAirportList, departureAirportsEndpoint);
}

export function getArrivalAirports(setArrivalAirportList: CallableFunction) {
  getAirports(setArrivalAirportList, arrivalAirportsEndpoint);
}

export async function getTrips(
  searchQuery: FlightSearchQuery,
  batchNum: number,
  setTrips: CallableFunction,
  sortMethod: string
) {
  await axios
    .post(flightSearchEndpoint + "/" + batchNum, {
      departAirport: searchQuery.departAirport,
      arriveAirport: searchQuery.arriveAirport,
      departDate: searchQuery.date,
      connectionNum: searchQuery.connectionNum,
      sort: sortMethod,
      order: "ASC",
    })
    .then((response) => {
      const trips = response.data["outbound"];
      console.log(trips);
      setTrips(trips);
    })
    .catch((error) => {
      console.error(`failed to get flights: ${error}`);
    });
}
