import axios from "axios";
import { submitReservationEndpoint } from "../endpoints";
import { Flight } from "../model/flight";
import { toast } from "react-toastify";

export async function submitReservation(
  trip: Flight[],
  successFunc: CallableFunction,
  errorFunc: CallableFunction
) {
  let tripsReserved = false;

  new Promise((r) => setTimeout(r, 3000)).then(() => {
    if (!tripsReserved) {
      toast("System is working...");
    }
  });

  return await axios
    .post(
      submitReservationEndpoint,
      {
        flights: trip,
      },
      { timeout: 10000 }
    )
    .then((response) => {
      tripsReserved = true;
      console.log(response);
      successFunc();
    })
    .catch((error) => {
      tripsReserved = true;
      console.error(`failed to submit reservation: ${error}`);
      errorFunc();
    });
}
