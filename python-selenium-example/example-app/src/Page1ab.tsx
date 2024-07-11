import { useParams } from "react-router-dom";

function Page1ab() {
  const { pageId } = useParams()
  return (
    <div>
      <h1>Page 1{pageId}</h1>
    </div>
  )
}

export default Page1ab
