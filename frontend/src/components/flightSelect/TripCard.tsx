import "./TripCard.css";
import { Flight } from "../../model/flight";
import * as helper from "./TripCardFuncs";

interface Props {
  trip: Flight[];
}

export function TripCard({ trip }: Props) {
  const [TravelTimeHours, TravelTimeMinutes] = helper.msToHoursMinutes(
    helper.getTotalTravelTime(trip)
  );

  return (
    <div className="TripCard">
      <img className="TripCardImage" src={helper.getFlightImageURI()}></img>
      <div className="TripCardDetails">
        <div className="TripStartEnd">
          <div>
            {helper.formatDateString(trip[0].departDateTime)} -{" "}
            {helper.formatDateString(trip[trip.length - 1].arriveDateTime)}
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