import "./TripCard.css";
import moment from "moment";

export interface Flight {
  arriveAirport: string;
  arriveDateTime: string;
  departAirport: string;
  departDateTime: string;
  flightNumber: string;
  id: number;
}

interface Props {
  trip: Flight[];
}

function getFlightImageURI() {
  return (
    "http://source.unsplash.com/random/?city&" +
    Math.floor(Math.random() * 30) +
    1
  );
}

function getLayoverText(trip: Flight[]) {
  switch (trip.length) {
    case 1:
      return "Nonstop";
    case 2:
      return "1 stop";
    default:
      return trip.length - 1 + " stops";
  }
}

function getTotalTravelTime(trip: Flight[]) {
  const TravelTime =
    Date.parse(trip[trip.length - 1].departDateTime) -
    Date.parse(trip[0].arriveDateTime);
  console.log(trip[trip.length - 1].departDateTime);
  console.log(trip[0].arriveDateTime);
  return TravelTime;
}

function msToHoursMinutes(ms: number) {
  var milliseconds = Math.floor((ms % 1000) / 100),
    seconds = Math.floor((ms / 1000) % 60),
    minutes = Math.floor((ms / (1000 * 60)) % 60),
    hours = Math.floor((ms / (1000 * 60 * 60)) % 24);
  return [hours, minutes];
}

function formatDateString(flightDateTimeStr: string) {
  const date: number = Date.parse(flightDateTimeStr);
  return moment(date).format("MM/DD/YYYY hh:mm a");
}

function getAirportID(fullAirportString: string) {
  return fullAirportString.split("(")[1].split(")")[0];
}

function getLayoverFlightStr(trip: Flight[]) {
  return trip
    .slice(0, -1)
    .map((flight) => {
      return getAirportID(flight.arriveAirport);
    })
    .join(", ");
}

export function TripCard({ trip }: Props) {
  const [TravelTimeHours, TravelTimeMinutes] = msToHoursMinutes(
    getTotalTravelTime(trip)
  );

  return (
    <div className="TripCard">
      <img className="TripCardImage" src={getFlightImageURI()}></img>
      <div className="TripCardDetails">
        <div className="TripStartEnd">
          {formatDateString(trip[0].departDateTime)} -
          {formatDateString(trip[trip.length - 1].arriveDateTime)}
        </div>
        <div className="TripLayoverCount">
          <div className="TripLayoverLabel">{getLayoverText(trip)}</div>
          <div className="TripCardSubtext">{getLayoverFlightStr(trip)}</div>
        </div>
        <div className="TripLength">
          <div className="TripLengthLabel">
            {TravelTimeHours}h {TravelTimeMinutes}m
          </div>
          <div className="TripCardSubtext">
            {getAirportID(trip[0].departAirport)} -
            {getAirportID(trip[trip.length - 1].arriveAirport)}
          </div>
        </div>
      </div>
    </div>
  );
}
