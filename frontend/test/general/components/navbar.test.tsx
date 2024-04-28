import { describe, it } from "vitest";
import { render } from "@testing-library/react";
import React from "react";

import { Navbar } from "../../../src/general/components/navbar";

describe("Navbar", () => {
  it("Should render without an error", () => {
    const { getByTestId } = render(<Navbar />);
  });
});
