import { submitReservation } from "../../src/flightSelect/flightReservation";
import { describe, it, vi, expect } from "vitest";
import axios from "axios";

vi.mock("axios");

describe("submitReservation()", () => {
  it("Should run successFunc() on good response", () => {
    const successFunc = vi.fn();
    const errorFunc = vi.fn();
    vi.mocked(axios.post).mockResolvedValue({});
    submitReservation([], successFunc, errorFunc).then(() => {
      expect(errorFunc).toHaveBeenCalledTimes(0);
      expect(successFunc).toHaveBeenCalledTimes(1);
    });
  });

  it("Should run errorFunc() on good response", () => {
    const successFunc = vi.fn();
    const errorFunc = vi.fn();
    vi.mocked(axios.post).mockImplementation(async () => {
      throw new Error();
    });
    submitReservation([], successFunc, errorFunc).then(() => {
      expect(errorFunc).toHaveBeenCalledTimes(1);
      expect(successFunc).toHaveBeenCalledTimes(0);
    });
  });
});
