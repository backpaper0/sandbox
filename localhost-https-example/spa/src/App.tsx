import React, { useState, useEffect } from 'react';

const api = process.env.REACT_APP_API || 'https://api.example.com:8080';

async function fetchOne(path: string) {
  const promise = await fetch(`${api}${path}`)
  const { value } = await promise.json();
  return value;
}

async function fetchApplePie() {
  const [apple, pie] = await Promise.all(['/apple', '/pie'].map(fetchOne));
  return apple + pie;
}

function App() {
  const [applePie, setApplePie] = useState('');
  const [count, setCount] = useState(0);
  useEffect(() => {
    fetchApplePie().then(setApplePie);
  }, []);
  const countUp = async () => {
    await fetch(`${api}/count`, {
      method: 'POST',
      mode: 'cors',
      credentials: 'include',
    });
    const promise = await fetch(`${api}/count`, {
      mode: 'cors',
      credentials: 'include',
    });
    const { value } = await promise.json();
    setCount(value);
  };
  return (
    <div>
      <h1>
        {applePie}
      </h1>
      <div>
        <button onClick={countUp}>{count}</button>
      </div>
    </div>
  );
}

export default App;
