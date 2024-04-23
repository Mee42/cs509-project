import { FlightSelectFilterOption } from "../../model/flightSelectFilterOption";
import "./flightSelectFilterButtons.css";

interface Props {
  filterOptions: FlightSelectFilterOption[];
  onChange: CallableFunction;
  selectedOption: FlightSelectFilterOption;
}

export function FlightSelectFilterButtons({
  filterOptions,
  onChange,
  selectedOption,
}: Props) {
  return (
    <div className="FlightSelectFilterContainer">
      <ul className="FlightSelectFilterButtons">
        {filterOptions.map((filterOption: FlightSelectFilterOption) => {
          return (
            <li key={filterOption.id}>
              <input
                type="radio"
                id={filterOption.id}
                data-testid={filterOption.id}
                name="FlightSelectFilter"
                className="FlightSelectFilterButton"
                checked={selectedOption.value === filterOption.value}
                onChange={() => {
                  onChange(filterOption);
                }}
              />
              <label htmlFor={filterOption.id}>{filterOption.label}</label>
            </li>
          );
        })}
      </ul>
    </div>
  );
}
