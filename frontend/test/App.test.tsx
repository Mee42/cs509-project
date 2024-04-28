import { describe, it } from "vitest";
import { render } from "@testing-library/react";
import React from "react";
import App from "../src/App";

describe("App", () => {
  it("Should render without an error", () => {
    const { getByTestId } = render(<App />);
  });
});
