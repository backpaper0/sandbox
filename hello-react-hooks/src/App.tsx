import React from 'react';

const App: React.FC = () => {
  const countUp = () => {};
  const count = 1;
  return (
    <div>
      <button onClick={countUp}>increase</button>
      <div>count: {count}</div>
    </div>
  );
}

export default App;
