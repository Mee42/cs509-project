import axios from "axios";
import { submitReservationEndpoint } from "../endpoints";
import { Flight } from "../model/flight";

export async function submitReservation(trip: Flight[], successFunc: CallableFunction, errorFunc: CallableFunction) {
  return await axios
    .post(submitReservationEndpoint, {
      flights: trip,
    })
    .then((response) => {
      console.log(response);
      successFunc();
    })
    .catch((error) => {
      console.error(`failed to submit reservation: ${error}`);
      errorFunc();
    });
}
