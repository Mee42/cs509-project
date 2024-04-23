import "./AirportSearchFilter.css";
import Select from "react-select";

interface Props {
  airports: string[];
  outerLabelText: string;
  innerLabelText: string;
  onSelectAirport: CallableFunction;
  inputID: string;
  defaultInputValue?: string;
}

function AirportSearchFilter({
  airports,
  outerLabelText,
  innerLabelText,
  onSelectAirport,
  inputID,
}: Props) {
  return (
    <form className="FlightAirportSelect" data-testid={inputID}>
      <label htmlFor={inputID}>{outerLabelText}</label>
      <Select
        id={inputID}
        className="basic-single"
        classNamePrefix="select"
        placeholder={innerLabelText}
        isSearchable={true}
        name="color"
        options={airports.map((airport) => ({
          value: airport,
          label: airport,
        }))}
        onChange={(selectedOption) => {
          onSelectAirport(selectedOption?.value);
          document.getElementById(inputID)!.style.outline = "";
        }}
      ></Select>
    </form>
  );
}

export default AirportSearchFilter;
