import "./AirportSearchFilter.css";
import Select from "react-select";

interface Props {
  airports: string[];
  labelText: string;
  onSelectAirport: CallableFunction;
  inputID: string;
  defaultInputValue?: string;
}

function AirportSearchFilter({
  airports,
  labelText,
  onSelectAirport,
  inputID,
  defaultInputValue = "",
}: Props) {
  if (defaultInputValue.length > 0) onSelectAirport(defaultInputValue);

  return (
    <Select
      id={inputID}
      className="basic-single"
      classNamePrefix="select"
      placeholder={labelText}
      defaultInputValue={defaultInputValue}
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
