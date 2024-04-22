import * as tcf from "../../src/flightSelect/TripCardFuncs";
import { Flight } from "../../src/model/flight";
import { describe, it, vi, expect, Mocked, expectTypeOf } from "vitest";

describe("getFlightImageURI()", () => {
  it("Should return a city image URI", () => {
    expect(tcf.getFlightImageURI()).toContain(
      "http://source.unsplash.com/random/?city"
    );
  });
});

describe("getLayoverText()", () => {
  const flight = Flight as Mocked<typeof Flight>;

  it("Should return 'Nonstop' with 1 flight", () => {
    const trip = [flight];
    expect(tcf.getLayoverText(trip)).toEqual("Nonstop");
  });
  it("Should return '1 stop' with 2 flights", () => {
    const trip = [flight, flight];
    expect(tcf.getLayoverText(trip)).toEqual("1 stop");
  });

  it("Should otherwise return stop count + 'stops'", () => {
    const trip = [flight, flight, flight];
    expect(tcf.getLayoverText(trip)).toEqual("2 stops");
  });
});

describe("getTotalTravelTime()", () => {
  it("Should equal difference between final flight arrive time and first flight depart time", () => {
    const flight1 = {
      arriveAirport: "",
      arriveDateTime: "",
      departAirport: "",
      departDateTime: "2023-01-01T19:29:00",
      flightNumber: "",
      id: 1,
    };
    const flight2 = {
      arriveAirport: "",
      arriveDateTime: "2023-01-03T12:00:00",
      departAirport: "",
      departDateTime: "",
      flightNumber: "",
      id: 1,
    };
    const trip = [flight1, flight2];
    const expectedTravelTime =
      Date.parse(flight2.arriveDateTime) - Date.parse(flight1.departDateTime);
    expectTypeOf(expectedTravelTime).toMatchTypeOf<Number>();
    expect(tcf.getTotalTravelTime(trip)).toEqual(expectedTravelTime);
  });
});

describe("msToHoursMinutes()", () => {
  it("Should return an array of hours and minutes", () => {
    expect(tcf.msToHoursMinutes(3720000)).toEqual([1, 2]);
  });
});

describe("getAirportID()", () => {
  it("Should return subtext between parenthesis", () => {
    const expectedString = "Hooplah";
    expect(tcf.getAirportID(`Test (${expectedString})`)).toEqual(
      expectedString
    );
  });

  it("Should throw error when text has no parentheses", () => {
    expect(() => {
      tcf.getAirportID("Test");
    }).toThrowError(TypeError);
  });
});

describe("getLayoverFlightSubStr()", () => {
  const airportCode1 = "ONE";
  const flight1 = {
    arriveAirport: `(${airportCode1})`,
    arriveDateTime: "",
    departAirport: "",
    departDateTime: "",
    flightNumber: "",
    id: 1,
  };

  const airportCode2 = "TWO";
  const flight2 = {
    arriveAirport: `(${airportCode2})`,
    arriveDateTime: "",
    departAirport: "",
    departDateTime: "",
    flightNumber: "",
    id: 1,
  };

  const airportCode3 = "THREE";
  const flight3 = {
    arriveAirport: `(${airportCode3})`,
    arriveDateTime: "",
    departAirport: "",
    departDateTime: "",
    flightNumber: "",
    id: 1,
  };

  it("Should comma separate all arrival airports except final one", () => {
    const trip = [flight1, flight2, flight3];
    expect(tcf.getLayoverFlightSubStr(trip)).toEqual(
      `${airportCode1}, ${airportCode2}`
    );
  });

  it("Should be empty for a trip with 1 flight", () => {
    const trip = [flight1];
    expect(tcf.getLayoverFlightSubStr(trip)).toEqual("");
  });
});

describe("getFlightNumberSubStr()", () => {
  const flight1 = {
    arriveAirport: "",
    arriveDateTime: "",
    departAirport: "",
    departDateTime: "",
    flightNumber: "FN1",
    id: 1,
  };

  const flight2 = {
    arriveAirport: "",
    arriveDateTime: "",
    departAirport: "",
    departDateTime: "",
    flightNumber: "FN2",
    id: 1,
  };

  it("Should comma separate flight numbers from trip", () => {
    const trip = [flight1, flight2];
    expect(tcf.getFlightNumberSubStr(trip)).toEqual(
      `${flight1.flightNumber}, ${flight2.flightNumber}`
    );
  });
});
