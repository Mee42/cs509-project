import "./AirportSearchFilter.css";
import Select from "react-select";

interface Props {
  airports: string[];
  labelText: string;
  onSelectAirport: CallableFunction;
  inputID: string;
}

function AirportSearchFilter({
  airports,
  labelText,
  onSelectAirport,
  inputID,
}: Props) {
  return (
    <Select
      id={inputID}
      className="basic-single"
      classNamePrefix="select"
      placeholder={labelText}
      isSearchable={true}
      name="color"
      options={airports.map((airport) => ({ value: airport, label: airport }))}
      onChange={(selectedOption) => {
        onSelectAirport(selectedOption?.value);
        document.getElementById(inputID)!.style.outline = "";
      }}
    ></Select>
  );
}

export default AirportSearchFilter;
