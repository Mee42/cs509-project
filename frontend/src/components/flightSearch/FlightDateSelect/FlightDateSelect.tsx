import "bootstrap/dist/css/bootstrap.min.css";

interface Props {
  labelText: string;
}

const FlightDateSelect = ({ labelText }: Props) => {
  return (
    <>
      <label>{labelText}</label>
      <input type="date" className="form-control" />
    </>
  );
};

export default FlightDateSelect;
