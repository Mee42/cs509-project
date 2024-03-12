export class FlightSearchQuery {
  arriveAirport: string;
  departAirport: string;
  date: string;
  time: string;
  connectionNum: number;

  constructor(
    arriveAirport: string,
    departAirport: string,
    date: string,
    time: string,
    connectionNum: number
  ) {
    this.arriveAirport = arriveAirport;
    this.departAirport = departAirport;
    this.date = date;
    this.time = time;
    this.connectionNum = connectionNum;
  }
}
