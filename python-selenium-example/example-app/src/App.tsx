import { Link } from "react-router-dom";

function App() {
  return (
    <div>
      <h1>Root</h1>
      <p><Link to={'/page1'}>Page 1</Link></p>
      <p><Link to={'/page2'}>Page 2</Link></p>
      <p><Link to={'/page3'}>Page 3</Link></p>
    </div>
  )
}

export default App
