import React, { useState, useEffect } from "react";

export default function StateCapturedExample() {
  const [count1, setCount1] = useState(0);
  const [count2, setCount2] = useState(0);
  const countUp = () => {
    setCount1(count1 + 1);
    setCount2(c => c + 1);
  };
  useEffect(() => {
    setInterval(countUp, 2000);
  }, []);
  return (
    <div>
      <p>count1: {count1}</p>
      <p>count2: {count2}</p>
      <p><button onClick={countUp}>Count up</button></p>
    </div>
  );
};

