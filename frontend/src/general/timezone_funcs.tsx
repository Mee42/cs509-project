import moment from "moment-timezone";
import airportTimezone from "airport-timezone";

export function convertAndFormatDateString(
  flightDateTimeStr: string,
  airportString: string,
  format: string
) {
  const timezone = getTimezoneFromAirportString(airportString);
  return utcToTimezone(flightDateTimeStr, timezone).format(format);
}

function utcToTimezone(flightDateTimeStr: string, timezone: string) {
  if (flightDateTimeStr.slice(-1) != "Z") {
    flightDateTimeStr += "Z";
  }
  return moment(flightDateTimeStr).tz(timezone);
}

function getTimezoneFromAirportString(airportString: string) {
  return getTimezoneFromAirportCode(
    getAirportCodeFromAirportString(airportString)
  );
}

function getTimezoneFromAirportCode(airportCode: string) {
  return airportTimezone.filter((airport) => {
    return airport.code === airportCode;
  })[0].timezone;
}

function getAirportCodeFromAirportString(airportString: string) {
  return airportString.split("(")[1].split(")")[0];
}
