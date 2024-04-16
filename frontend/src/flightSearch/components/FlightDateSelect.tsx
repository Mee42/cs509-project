import "./FlightDateSelect.css";

interface Props {
  labelText: string;
  onSelectDate: CallableFunction;
  onSelectStartTime: CallableFunction;
  onSelectEndTime: CallableFunction;
  startTimeDefaultValue: string;
  endTimeDefaultValue: string;
  inputID: string;
}

const FlightDateTimeSelect = ({
  labelText,
  onSelectDate,
  onSelectStartTime,
  onSelectEndTime,
  startTimeDefaultValue,
  endTimeDefaultValue,
  inputID,
}: Props) => {
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
      <div className="TimeRangeSelect">
        <input
          className="form-control"
          type="time"
          defaultValue={startTimeDefaultValue}
          onChange={(event) => {
            onSelectStartTime(event.target.value + ":00");
          }}
        ></input>
        <div>{"to"}</div>
        <input
          className="form-control"
          type="time"
          defaultValue={endTimeDefaultValue}
          onChange={(event) => {
            onSelectEndTime(event.target.value + ":00");
          }}
        ></input>
      </div>
    </div>
  );
};

export default FlightDateTimeSelect;
