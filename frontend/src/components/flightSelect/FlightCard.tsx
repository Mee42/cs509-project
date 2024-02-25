import "./FlightCard.css";

export interface Flight {
  arriveAirport: string;
  arriveDateTime: string;
  departAirport: string;
  departDateTime: string;
  flightNumber: string;
  id: number;
}

interface Props {
  flight: Flight;
}

function getFlightImageURI() {
  return (
    "http://source.unsplash.com/random/?city&" +
    Math.floor(Math.random() * 30) +
    1
  );
}

export function FlightCard({ flight }: Props) {
  return (
    <div className="FlightCard">
      <img className="FlightCardImage" src={getFlightImageURI()}></img>
      <div className="FlightCardDetails">
        <div className="FlightCardArriveAirport">{flight.arriveAirport}</div>
        <div className="FlightCardArriveDateTime">{flight.arriveDateTime}</div>
        <div className="FlightCardDepartAirport">{flight.departAirport}</div>
        <div className="FlightCardDepartDateTime">{flight.departDateTime}</div>
        <div className="FlightCardFlightNumber">{flight.flightNumber}</div>
      </div>
    </div>
  );
}
