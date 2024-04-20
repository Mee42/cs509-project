import airportTimezone from "airport-timezone";
import { Flight } from "../model/flight";
import moment from "moment-timezone";

export function getFlightImageURI() {
  return (
    "http://source.unsplash.com/random/?city&" +
    Math.floor(Math.random() * 30) +
    1
  );
}

export function getLayoverText(trip: Flight[]) {
  switch (trip.length) {
    case 1:
      return "Nonstop";
    case 2:
      return "1 stop";
    default:
      return trip.length - 1 + " stops";
  }
}

export function getTotalTravelTime(trip: Flight[]) {
  const TravelTime =
    Date.parse(trip[trip.length - 1].arriveDateTime) -
    Date.parse(trip[0].departDateTime);
  return TravelTime;
}

export function msToHoursMinutes(ms: number) {
  return [
    Math.floor((ms / (1000 * 60 * 60)) % 24),
    Math.floor((ms / (1000 * 60)) % 60),
  ];
}

export function formatDateString(
  flightDateTimeStr: string,
  airportCode: string
) {
  var format: string = "MM/DD/YYYY hh:mm a";
  var timezone: string = getTimezoneFromAirportCode(airportCode);
  return moment(flightDateTimeStr + "Z")
    .tz(timezone)
    .format(format);
}

export function getAirportID(fullAirportString: string) {
  return fullAirportString.split("(")[1].split(")")[0];
}

export function getLayoverFlightSubStr(trip: Flight[]) {
  return trip
    .slice(0, -1)
    .map((flight) => {
      return getAirportID(flight.arriveAirport);
    })
    .join(", ");
}

export function getFlightNumberSubStr(trip: Flight[]) {
  return trip
    .map((flight) => {
      return flight.flightNumber;
    })
    .join(", ");
}

export function getTimezoneFromAirportCode(airportCode: string) {
  return airportTimezone.filter((airport) => {
    return airport.code === airportCode;
  })[0].timezone;
}

export function getAirportCodeFromAirportString(airportString: string) {
  return airportString.split("(")[1].split(")")[0];
}
