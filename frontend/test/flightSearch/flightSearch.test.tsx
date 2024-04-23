import * as fs from "../../src/flightSearch/flightSearch";
import { FlightSearchQuery } from "../../src/model/flightSearchQuery";
import { describe, it, vi, expect } from "vitest";
import axios from "axios";

vi.mock("axios");

function testGetAirportsGoodResponse(getAirportsFunc) {
  const errorFunc = vi.fn();
  const setAirportsFunc = vi.fn();
  vi.mocked(axios.get).mockResolvedValue({});
  getAirportsFunc(setAirportsFunc, errorFunc).then(() => {
    expect(errorFunc).toHaveBeenCalledTimes(0);
    expect(setAirportsFunc).toHaveBeenCalledTimes(1);
  });
}

function testGetAirportsErrorResponse(getAirportsFunc) {
  const errorFunc = vi.fn();
  const setAirportsFunc = vi.fn();
  vi.mocked(axios.get).mockImplementation(async () => {
    throw new Error();
  });
  getAirportsFunc(setAirportsFunc, errorFunc).then(() => {
    expect(errorFunc).toHaveBeenCalledTimes(1);
    expect(setAirportsFunc).toHaveBeenCalledTimes(0);
  });
}

describe("getAirports()", () => {
  it("getDepartureAirports() should populate airport list on good response", () => {
    testGetAirportsGoodResponse(fs.getDepartureAirports);
  });

  it("getDepartureAirports() should call error function if error returned from API", () => {
    testGetAirportsErrorResponse(fs.getDepartureAirports);
  });

  it("getArrivalAirports() should populate airport list on good response", () => {
    testGetAirportsGoodResponse(fs.getArrivalAirports);
  });

  it("getArrivalAirports() should call error function if error returned from API", () => {
    testGetAirportsErrorResponse(fs.getArrivalAirports);
  });
});

describe("getTrips()", () => {
  it("Should run setTrips() on good response", () => {
    const errorFunc = vi.fn();
    const setTrips = vi.fn();
    const fsq = new FlightSearchQuery("", "", "", "", "", 0);
    vi.mocked(axios.post).mockResolvedValue({ data: { outbound: [] } });
    fs.getTrips(fsq, 1, setTrips, "", errorFunc).then(() => {
      expect(errorFunc).toHaveBeenCalledTimes(0);
      expect(setTrips).toHaveBeenCalledTimes(1);
    });
  });

  it("Should run errorFunc() on error response", () => {
    const errorFunc = vi.fn();
    const setTrips = vi.fn();
    const fsq = new FlightSearchQuery("", "", "", "", "", 0);
    vi.mocked(axios.post).mockImplementation(async () => {
      throw new Error();
    });
    fs.getTrips(fsq, 1, setTrips, "", errorFunc).then(() => {
      expect(errorFunc).toHaveBeenCalledTimes(1);
      expect(setTrips).toHaveBeenCalledTimes(0);
    });
  });
});
