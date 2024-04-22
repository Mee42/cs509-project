import "./TripCard.css";
import { Flight } from "../../model/flight";
import * as helper from "../TripCardFuncs";
import * as tz from "../../general/timezone_funcs"

interface Props {
  trip: Flight[];
  onClick?: CallableFunction;
}

export function TripCard({
  trip,
  onClick = () => {},
}: Props) {
  const [TravelTimeHours, TravelTimeMinutes] = helper.msToHoursMinutes(
    helper.getTotalTravelTime(trip));
  
  const firstFlight = trip[0];
  const lastFlight = trip[trip.length - 1];
  const dateFormat = "MM/DD/YYYY hh:mm a"

  return (
    <div
      className="TripCard"
      onClick={() => {
        onClick(trip);
      }}
    >
      <img className="TripCardImage" src={helper.getFlightImageURI()}></img>
      <div className="TripCardDetails">
        <div className="TripStartEnd">
          <div>
            {tz.convertAndFormatDateString(firstFlight.departDateTime, firstFlight.departAirport, dateFormat)} - {" "}
            {tz.convertAndFormatDateString(lastFlight.arriveDateTime, lastFlight.arriveAirport, dateFormat)}
          </div>
          <div className="TripCardSubtext">
            {helper.getFlightNumberSubStr(trip)}
          </div>
        </div>

        <div className="TripLayoverCount">
          <div className="TripLayoverLabel">{helper.getLayoverText(trip)}</div>
          <div className="TripCardSubtext">
            {helper.getLayoverFlightSubStr(trip)}
          </div>
        </div>
        <div className="TripLength">
          <div className="TripLengthLabel">
            {TravelTimeHours}h {TravelTimeMinutes}m
          </div>
          <div className="TripCardSubtext">
            {helper.getAirportID(trip[0].departAirport)} -
            {helper.getAirportID(trip[trip.length - 1].arriveAirport)}
          </div>
        </div>
      </div>
    </div>
  );
}
