import React from "react";
import { useCounter1, useDebugLog } from "./Hooks";

const Counter1: React.FC = () => {
  console.log("Render counter 1");
  const [counter, countUp] = useCounter1();
  const [ref] = useDebugLog("counter 1");
  return (
    <div>
      <h2>Counter 1</h2>
      <p>
        <strong ref={ref}>{counter}</strong>
      </p>
      <p>
        <button onClick={countUp}>Count up</button>
      </p>
    </div>
  );
};

export default Counter1;
