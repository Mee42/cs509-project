import { describe, it } from "vitest";
import { render } from "@testing-library/react";
import React from "react";
import { TripCard } from "../../../src/flightSelect/components/TripCard";

describe("TripCard", () => {
  it("Should render without an error", () => {
    const { getByTestId } = render(
      <TripCard
        id="test"
        trip={[
          {
            arriveAirport: "Atlanta (ATL)",
            arriveDateTime: "2023-01-01",
            departAirport: "Tuscon (TUS)",
            departDateTime: "2023-01-01",
            flightNumber: "ABC123",
            id: 1,
          },
        ]}
      />
    );
  });
});
