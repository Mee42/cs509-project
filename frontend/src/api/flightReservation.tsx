import axios from "axios";
import { submitReservationEndpoint } from "../definitions";
import { Flight } from "../model/flight";

export async function submitReservation(trip: Flight[]) {
  await axios
    .post(submitReservationEndpoint, {
      flights: trip,
    })
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      console.error(`failed to submit reservation: ${error}`);
    });
}
