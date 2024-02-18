import { useEffect, useState } from "react"
import axios from "axios";

function Flight() {
    // react hook to re-render component on update
    const [response, setResponse] = useState([]);
    
    // update component with backend data
    const getData = async() => {
      try {
        const res = await axios.get('http://localhost:8080/api/flight')
        console.log(res.data)
        setResponse(res.data);
      } catch(err) {
        console.log("error: failed to access backend flight api")
      }
    }
  
    // run getData on the first render
    // https://www.w3schools.com/react/react_useeffect.asp
    useEffect( () => {
      getData();
    }, []);
    
    // jsx to render
    return (<h1> Flight: {JSON.stringify(response)}</h1>);
  }

export default Flight