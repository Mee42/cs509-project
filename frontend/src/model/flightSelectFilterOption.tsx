export class FlightSelectFilterOption {
  label: string;
  value: string;
  id: string;

  constructor(label: string, value: string) {
    this.label = label;
    this.value = value;
    this.id = "FlightSelect" + label.replace(" ", "");
  }
}
