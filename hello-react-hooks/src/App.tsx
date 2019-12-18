import React, { useState } from 'react';

const App: React.FC = () => {
  const [count, setCount] = useState(0);
  const countUp = () => setCount(count + 1);
  return (
    <div>
      <button onClick={countUp}>increase</button>
      <div>count: {count}</div>
    </div>
  );
}

export default App;
