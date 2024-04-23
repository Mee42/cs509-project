import { describe, it, expect, vi } from "vitest";
import {
  render,
  fireEvent,
  getByText,
  cleanup,
  screen,
  waitFor,
} from "@testing-library/react";
import React from "react";
import matchers from "@testing-library/jest-dom/matchers";

import { FlightSearchFilters } from "../../../src/flightSearch/components/FlightSearchFilters";
import * as fs from "../../../src/flightSearch/flightSearch";

expect.extend(matchers);
vi.mock("../../../src/flightSearch/flightSearch");

describe("FlightSearchFilters", () => {
  const setSearchQueriesFunc = vi.fn();
  const mock_airport_list = ["airport1", "airport2"];
  const expectedDate = "2023-01-01";
  const expectedStartTime = "01:00:00";
  const expectedEndTime = "02:00:00";
  vi.mocked(fs.getDepartureAirports).mockImplementation(
    async (
      setDepartureAirportList: CallableFunction,
      errorFunc: CallableFunction
    ) => {
      setDepartureAirportList(mock_airport_list);
    }
  );

  vi.mocked(fs.getArrivalAirports).mockImplementation(
    async (
      setArrivalAirportList: CallableFunction,
      errorFunc: CallableFunction
    ) => {
      setArrivalAirportList(mock_airport_list);
    }
  );

  const selectOptionByText = (
    getByTestId: CallableFunction,
    testId: string,
    optionText: string
  ) => {
    const selectElement = getByTestId(testId).children[0];
    fireEvent.keyDown(selectElement, {
      key: "ArrowDown",
    });
    waitFor(() => getByText(selectElement, optionText));
    fireEvent.click(getByText(selectElement, optionText));
  };

  it("Should allow users to select departure airport from a list of available airports", () => {
    const { getByTestId } = render(
      <FlightSearchFilters setSearchQueries={setSearchQueriesFunc} />
    );
    const selectElement = getByTestId("DepartAirportSearchFilter").children[1];
    fireEvent.keyDown(selectElement, {
      key: "ArrowDown",
    });
    waitFor(() => getByText(selectElement, mock_airport_list[0]));
    fireEvent.click(getByText(selectElement, mock_airport_list[0]));
    cleanup();
  });

  it("Should allow users to select arrival airport from a list of available airports", () => {
    const { getByTestId } = render(
      <FlightSearchFilters setSearchQueries={setSearchQueriesFunc} />
    );
    const selectElement = getByTestId("ArriveAirportSearchFilter").children[1];
    fireEvent.keyDown(selectElement, {
      key: "ArrowDown",
    });
    waitFor(() => getByText(selectElement, mock_airport_list[0]));
    fireEvent.click(getByText(selectElement, mock_airport_list[0]));
    cleanup();
  });

  it("Should allow users to enter a departure date and time window", () => {
    const { getByTestId } = render(
      <FlightSearchFilters setSearchQueries={setSearchQueriesFunc} />
    );
    const dateInputElement = getByTestId("DepartureDateFilter");
    fireEvent.mouseDown(dateInputElement);
    fireEvent.change(dateInputElement, { target: { value: expectedDate } });
    expect(dateInputElement.value).toEqual(expectedDate);
    cleanup();
  });

  it("Should allow users to submit a departure time window", () => {
    const { getByTestId } = render(
      <FlightSearchFilters setSearchQueries={setSearchQueriesFunc} />
    );
    const startTimeInputElement = getByTestId("DepartureDateFilterTimeStart");
    console.log(startTimeInputElement.outerHTML);
    fireEvent.mouseDown(startTimeInputElement);
    fireEvent.change(startTimeInputElement, {
      target: { value: expectedStartTime },
    });
    expect(startTimeInputElement.value).toEqual(expectedStartTime);

    const endTimeInputElement = getByTestId("DepartureDateFilterTimeEnd");
    fireEvent.mouseDown(endTimeInputElement);
    fireEvent.change(endTimeInputElement, {
      target: { value: expectedEndTime },
    });
    expect(endTimeInputElement.value).toEqual(expectedEndTime);
    cleanup();
  });

  it("Should allow users to submit a return date when round-trip is selected", () => {
    const { getByTestId } = render(
      <FlightSearchFilters setSearchQueries={setSearchQueriesFunc} />
    );
    expect(screen.queryByTestId("ReturnDateFilter")).toBe(null);
    selectOptionByText(getByTestId, "RoundTripSelect", "Round-Trip");

    const dateInputElement = getByTestId("ReturnDateFilter");
    fireEvent.mouseDown(dateInputElement);
    fireEvent.change(dateInputElement, { target: { value: expectedDate } });
    expect(dateInputElement.value).toEqual(expectedDate);
    cleanup();
  });

  it("Should allow users to submit a return time window when round-trip is selected", () => {
    const { getByTestId } = render(
      <FlightSearchFilters setSearchQueries={setSearchQueriesFunc} />
    );
    expect(screen.queryByTestId("ReturnDateFilterTimeStart")).toBe(null);
    expect(screen.queryByTestId("ReturnDateFilterTimeEnd")).toBe(null);
    selectOptionByText(getByTestId, "RoundTripSelect", "Round-Trip");

    const startTimeInputElement = getByTestId("ReturnDateFilterTimeStart");
    console.log(startTimeInputElement.outerHTML);
    fireEvent.mouseDown(startTimeInputElement);
    fireEvent.change(startTimeInputElement, {
      target: { value: expectedStartTime },
    });
    expect(startTimeInputElement.value).toEqual(expectedStartTime);

    const endTimeInputElement = getByTestId("ReturnDateFilterTimeEnd");
    fireEvent.mouseDown(endTimeInputElement);
    fireEvent.change(endTimeInputElement, {
      target: { value: expectedEndTime },
    });
    expect(endTimeInputElement.value).toEqual(expectedEndTime);
    cleanup();
  });

  it("Should have 0, 1, or 2 connections to select from", () => {
    const { getByTestId } = render(
      <FlightSearchFilters setSearchQueries={setSearchQueriesFunc} />
    );
    const connectionNumSelectTestId = "ConnectionNumSelect";
    const connectionNumInputElement = getByTestId(connectionNumSelectTestId)
      .children[0];
    selectOptionByText(getByTestId, connectionNumSelectTestId, "1 Connection");
    selectOptionByText(getByTestId, connectionNumSelectTestId, "2 Connections");
    selectOptionByText(getByTestId, connectionNumSelectTestId, "0 Connections");
    cleanup();
  });
});
