import React, { useState } from 'react';
import CounterContext from './CounterContext';

type PropsType = {
  children: React.ReactNode;
};

const WithCounterContext: React.FC<PropsType> = ({ children }) => {
  const [counter1, setCounter1] = useState(0);
  const [counter2, setCounter2] = useState(0);
  const countUp1 = () => {
    setCounter1(a => a + 1);
  };
  const countUp2 = () => {
    setCounter2(a => a + 1);
  };
  const value = {
    counter1,
    countUp1,
    counter2,
    countUp2,
  };
  return (
    <CounterContext.Provider value={value}>
      {children}
    </CounterContext.Provider>
  );
};

export default WithCounterContext;
