import { Link } from "react-router-dom";

function Page2() {
  return (
    <div>
      <h1>Page 2</h1>
      <p><Link to={'/'}>Home</Link></p>
      <p><Link to={'/page1'}>Page 1</Link></p>
    </div>
  )
}

export default Page2