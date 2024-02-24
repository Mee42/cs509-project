import "bootstrap/dist/css/bootstrap.min.css";

interface Props {
  labelText: string;
  onSelectDate: CallableFunction;
}

const FlightDateSelect = ({ labelText, onSelectDate }: Props) => {
  return (
    <div id="FlightDateSelect">
      <label>{labelText}</label>
      <input
        type="date"
        className="form-control"
        onChange={(event) => {
          onSelectDate(event.target.value);
        }}
      />
    </div>
  );
};

export default FlightDateSelect;
