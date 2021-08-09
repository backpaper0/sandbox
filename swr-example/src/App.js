import { useState } from "react";
import useSWR, { mutate } from "swr";

const fetcher = async (query, errorFlag) => {
  if (errorFlag) {
    throw new Error('This is error message');
  }
  return `${query}-${Math.random()}`;
};

function Foobar({ query }) {
  const [errorFlag, setErrorFlag] = useState(false);
  const { data, error } = useSWR([query, errorFlag], fetcher);
  if (!data && !error) {
    return (<div>Loading...</div>);
  }
  if (error) {
    return (
      <div>
        <p>Error: {error.message}</p>
        <p>
          <button onClick={evt => setErrorFlag(false)}>Reset</button>
        </p>
      </div>
    );
  }
  return (
    <div>
      <p>{data}</p>
      <p>
        <button onClick={evt => mutate([query, errorFlag])}>Update</button>
        <button onClick={evt => setErrorFlag(true)}>Error</button>
      </p>
    </div>
  );
}

function App() {
  return (
    <div>
      <Foobar query="/foo" />
      <Foobar query="/foo" />
      <Foobar query="/bar" />
      <Foobar query="/baz" />
    </div>
  );
}

export default App;
