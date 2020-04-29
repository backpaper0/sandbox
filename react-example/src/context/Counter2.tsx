import React from 'react';
import { useCounter2, useDebugLog } from './Hooks';

const Counter2: React.FC = () => {
  console.log('Render counter 2');
  const [counter, countUp] = useCounter2();
  const [ref] = useDebugLog('counter 2');
  return (
    <div>
      <h2>Counter 2</h2>
      <p><strong ref={ref}>{counter}</strong></p>
      <p><button onClick={countUp}>Count up</button></p>
    </div>
  );
};

export default Counter2;

