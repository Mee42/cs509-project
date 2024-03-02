import "./AirportSearchFilter.css";

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
    <select
      id={inputID}
      className="AirportSearchFilter"
      defaultValue="placeholder"
      onChange={(event) => {
        onSelectAirport(event.target.value);
        document.getElementById(inputID)!.style.outline = "";
      }}
    >
      <option value="placeholder" disabled hidden>
        {labelText}
      </option>
      {airports.map((item) => (
        <option key={item}>{item}</option>
      ))}
    </select>
  );
}

export default AirportSearchFilter;
