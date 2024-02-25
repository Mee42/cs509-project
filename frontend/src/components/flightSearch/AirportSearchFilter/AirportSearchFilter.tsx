import "./AirportSearchFilter.css";

interface Props {
  airports: string[];
  labelText: string;
  onSelectAirport: CallableFunction;
}

function AirportSearchFilter({ airports, labelText, onSelectAirport }: Props) {
  return (
    <select
      className="AirportSearchFilter"
      defaultValue="placeholder"
      onChange={(event) => {
        onSelectAirport(event.target.value);
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
