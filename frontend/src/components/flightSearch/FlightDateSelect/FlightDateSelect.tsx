import "./FlightDateSelect.css";

interface Props {
  labelText: string;
  onSelectDate: CallableFunction;
  inputID: string;
  defaultInputValue?: string;
}

const FlightDateSelect = ({
  labelText,
  onSelectDate,
  inputID,
  defaultInputValue = "",
}: Props) => {
  if (defaultInputValue.length > 0) onSelectDate(defaultInputValue);

  return (
    <div className="FlightDateSelect">
      <label>{labelText}</label>
      <input
        id={inputID}
        type="date"
        className="form-control"
        defaultValue={defaultInputValue}
        onChange={(event) => {
          onSelectDate(event.target.value);
          document.getElementById(inputID)!.style.outline = "";
        }}
      />
    </div>
  );
};

export default FlightDateSelect;
