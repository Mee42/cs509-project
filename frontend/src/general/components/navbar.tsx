import cs509_wpi_logo from "../cs509_wpi_logo.png";
import "./navbar.css";

export function Navbar() {
  return (
    <div className="navbarContainer">
      <nav className="navbar">
        <h1>World Plane, Inc</h1>
        <div className="navbarItems">
          <img src={cs509_wpi_logo} alt="Logo" />
        </div>
      </nav>
    </div>
  );
}
