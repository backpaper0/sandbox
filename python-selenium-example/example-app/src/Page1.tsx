import { Link } from "react-router-dom";

function Page1() {
  return (
    <div>
      <h1>Page 1</h1>
      <p><Link to={'/page1/a'}>Page 1a</Link></p>
      <p><Link to={'/page1/b'}>Page 1b</Link></p>
    </div>
  )
}

export default Page1
