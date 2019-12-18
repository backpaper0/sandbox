import React, { useState, useEffect } from 'react';

const App: React.FC = () => {
  const [count, setCount] = useState(0);
  const countUp = () => setCount(count + 1);
  useEffect(() => {
    document.title = `You clicked ${count} times`;
  });
  return (
    <div>
      <button onClick={countUp}>increase</button>
      <div>count: {count}</div>
    </div>
  );
}

export default App;
