import "react-toastify/dist/ReactToastify.css";

import {
  arrivalAirportsEndpoint,
  departureAirportsEndpoint,
  flightSearchEndpoint,
} from "../definitions";
import axios from "axios";

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
  departAirport: string,
  arriveAirport: string,
  departDate: string,
  setTrips: CallableFunction
) {
  await axios
    .post(flightSearchEndpoint + "/3", {
      departAirport: departAirport,
      arriveAirport: arriveAirport,
      departDate: departDate,
      connectionNum: "2",
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
