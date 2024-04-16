export class FlightSearchQuery {
  arriveAirport: string;
  departAirport: string;
  date: string;
  startTime: string;
  endTime: string;
  connectionNum: number;

  constructor(
    arriveAirport: string,
    departAirport: string,
    date: string,
    startTime: string,
    endTime: string,
    connectionNum: number
  ) {
    this.arriveAirport = arriveAirport;
    this.departAirport = departAirport;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.connectionNum = connectionNum;
  }
}
