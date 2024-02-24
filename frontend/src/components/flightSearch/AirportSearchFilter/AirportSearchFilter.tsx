import "./AirportSearchFilter.css";

interface Props {
  airports: string[];
  labelText: string;
}

function AirportSearchFilter({ airports, labelText }: Props) {
  return (
    <select className="AirportSearchFilter" defaultValue="placeholder">
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
