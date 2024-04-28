import { describe, it, expect, vi, afterEach } from "vitest";
import { render, fireEvent, cleanup, screen } from "@testing-library/react";
import React from "react";
import matchers from "@testing-library/jest-dom/matchers";

import * as fs from "../../../src/flightSearch/flightSearch";
import { FlightSelect } from "../../../src/flightSelect/components/flightSelect";
import { FlightSearchQuery } from "../../../src/model/flightSearchQuery";
import { submitReservation } from "../../../src/flightSelect/flightReservation";
import { useState } from "react";

// setup
vi.mock("../../../src/flightSearch/flightSearch");
vi.mock("../../../src/flightSelect/flightReservation");
expect.extend(matchers);

// global constants
const arrivalDateFilterButton = "FlightSelectArrivalDate";
const oneWaySearchQueries = [
  new FlightSearchQuery(
    "Atlanta (ATL)",
    "Tuscon (TUS)",
    "2023-01-01",
    "00:00:00",
    "23:59:00",
    0
  ),
];
const roundTripSearchQueries = [
  oneWaySearchQueries[0],
  new FlightSearchQuery(
    "Tuscon (TUS)",
    "Atlanta (ATL)",
    "2023-01-01",
    "00:00:00",
    "23:59:00",
    0
  ),
];

//tests
describe("FlightSearchFilters", () => {
  afterEach(() => {
    vi.resetAllMocks();
    cleanup();
  });

  it("Should call flightSearch api with selected sorting", () => {
    vi.mocked(fs.getTrips).mockImplementation(
      async (searchQueries, currentBatchNum, setTrips, filterValue) => {}
    );
    const { getByTestId } = render(
      <FlightSelect searchQueries={oneWaySearchQueries} />
    );

    fireEvent.click(getByTestId(arrivalDateFilterButton));
    expect(fs.getTrips.mock.calls.slice(-1)[0][3]).toEqual("Arrive");
    fireEvent.click(getByTestId("FlightSelectTravelTime"));
    expect(fs.getTrips.mock.calls.slice(-1)[0][3]).toEqual("TravelTime");
    fireEvent.click(getByTestId("FlightSelectDepartureDate"));
    expect(fs.getTrips.mock.calls.slice(-1)[0][3]).toEqual("Depart");
  });

  it("Should allow the user to select one trip when One-Way selected", () => {
    vi.mocked(fs.getTrips).mockImplementation(
      async (searchQueries, currentBatchNum, setTrips, filterValue) => {
        setTrips([
          [
            {
              arriveAirport: "Tuscon (TUS)",
              arriveDateTime: "2023-01-01T00:00:00",
              departAirport: "Atlanta (ATL)",
              departDateTime: "2023-01-01T02:00:00",
              flightNumber: "Flight123",
              id: 0,
            },
          ],
        ]);
      }
    );
    const { getByTestId } = render(
      <FlightSelect searchQueries={oneWaySearchQueries} />
    );
    fireEvent.click(getByTestId(arrivalDateFilterButton));
    const tripCard = getByTestId("SelectableTripCard0");
    expect(screen.queryByText("Make Reservation")).toBe(null);
    fireEvent.click(tripCard);
    screen.getByText("Make Reservation");
  });

  it("Should allow the user to select two trips when Round-Trip selected", () => {
    vi.mocked(fs.getTrips).mockImplementation(
      async (searchQueries, currentBatchNum, setTrips, filterValue) => {
        setTrips([
          [
            {
              arriveAirport: "Tuscon (TUS)",
              arriveDateTime: "2023-01-01T00:00:00",
              departAirport: "Atlanta (ATL)",
              departDateTime: "2023-01-01T02:00:00",
              flightNumber: "Flight123",
              id: 0,
            },
          ],
        ]);
      }
    );
    const { getByTestId } = render(
      <FlightSelect searchQueries={roundTripSearchQueries} />
    );

    fireEvent.click(getByTestId(arrivalDateFilterButton));
    const tripCard1 = getByTestId("SelectableTripCard0");
    expect(screen.queryByText("Make Reservation")).toBe(null);
    fireEvent.click(tripCard1);
    expect(screen.queryByText("Make Reservation")).toBe(null);
    const tripCard2 = getByTestId("SelectableTripCard0");
    fireEvent.click(tripCard2);
    screen.getByText("Make Reservation");
  });

  it("Should display arrival and departure time in airport local time", () => {
    const trip = [
      {
        arriveAirport: "Tuscon (TUS)",
        arriveDateTime: "2023-01-01T00:00:00",
        departAirport: "Atlanta (ATL)",
        departDateTime: "2023-01-01T02:00:00",
        flightNumber: "Flight123",
        id: 0,
      },
    ];
    vi.mocked(fs.getTrips).mockImplementation(
      async (searchQueries, currentBatchNum, setTrips, filterValue) => {
        setTrips([trip]);
      }
    );
    const { getByTestId } = render(
      <FlightSelect searchQueries={roundTripSearchQueries} />
    );

    fireEvent.click(getByTestId(arrivalDateFilterButton));
    const tripCard = getByTestId("SelectableTripCard0");
    const tripStartEndStr =
      tripCard.children[1].children[0].children[0].innerHTML;
    expect(tripStartEndStr).toEqual(
      "12/31/2022 09:00 pm - 12/31/2022 05:00 pm"
    );
  });

  it("Should display trip details after submitting a reservation", () => {
    vi.mocked(fs.getTrips).mockImplementation(
      async (searchQueries, currentBatchNum, setTrips, filterValue) => {
        setTrips([
          [
            {
              arriveAirport: "Tuscon (TUS)",
              arriveDateTime: "2023-01-01T00:00:00",
              departAirport: "Atlanta (ATL)",
              departDateTime: "2023-01-01T02:00:00",
              flightNumber: "Flight123",
              id: 0,
            },
          ],
        ]);
      }
    );
    vi.mocked(submitReservation).mockImplementation(async () => {});
    const { getByTestId } = render(
      <FlightSelect searchQueries={oneWaySearchQueries} />
    );

    fireEvent.click(getByTestId(arrivalDateFilterButton));
    const tripCard = getByTestId("SelectableTripCard0");
    expect(screen.queryByText("Make Reservation")).toBe(null);
    fireEvent.click(tripCard);
    fireEvent.click(screen.getByText("Make Reservation"));
    getByTestId("SelectedTripCard0");
  });

  it("Should update when searchQueries change", () => {
    const buttonTestId = "clickme";
    function ContainerComponent() {
      const [searchQueries, setSearchQueries] = useState(oneWaySearchQueries);
      return (
        <>
          <button
            data-testid={buttonTestId}
            onClick={() => {
              setSearchQueries(roundTripSearchQueries);
            }}
          />
          <FlightSelect searchQueries={searchQueries} />
        </>
      );
    }
    const { getByTestId } = render(<ContainerComponent />);
    expect(fs.getTrips).toHaveBeenCalledTimes(0);
    fireEvent.click(getByTestId(buttonTestId));
    expect(fs.getTrips).toHaveBeenCalledTimes(2);
  });
});
