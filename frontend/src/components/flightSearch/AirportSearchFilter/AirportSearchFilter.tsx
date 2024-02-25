interface Props {
  airports: string[];
  labelText: string;
}

function AirportSearchFilter({ airports, labelText }: Props) {
  return (
    <label>
      {labelText}
      <select>
        {airports.map((item) => (
          <option key={item}>{item}</option>
        ))}
      </select>
    </label>
  );
}

export default AirportSearchFilter;
