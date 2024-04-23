import { convertAndFormatDateString } from "../../src/general/timezone_funcs";
import { describe, it, expect } from "vitest";

describe("ConvertAndFormatDateString", () => {
  it("Can convert from UTC to EST and Format", () => {
    expect(
      convertAndFormatDateString(
        "2023-02-01T19:29:00",
        "Atlanta (ATL)",
        "MM/DD/YYYY hh:mm a"
      )
    ).toEqual("02/01/2023 02:29 pm");
  });
});
