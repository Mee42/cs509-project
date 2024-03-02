import "./FlightDateSelect.css";

interface Props {
  labelText: string;
  onSelectDate: CallableFunction;
  inputID: string;
}

const FlightDateSelect = ({ labelText, onSelectDate, inputID }: Props) => {
  return (
    <div className="FlightDateSelect">
      <label>{labelText}</label>
      <input
        id={inputID}
        type="date"
        className="form-control"
        onChange={(event) => {
          onSelectDate(event.target.value);
          document.getElementById(inputID)!.style.outline = "";
        }}
      />
    </div>
  );
};

export default FlightDateSelect;
