import React, { useState, useCallback } from "react";
import { useDebugLog } from "./Hooks";

export default function MemoExample2() {
  const [count1, setCount1] = useState(0);
  const [count2, setCount2] = useState(0);
  const countUp1 = () => setCount1((a) => a + 1);
  const countUp2 = () => setCount2((a) => a + 1);
  const callback1 = useCallback(countUp1, []);
  const callback2 = useCallback(countUp2, []);
  return (
    <div>
      <h1>Memo (function)</h1>
      <p>
        Count 1: <strong>{count1}</strong>
      </p>
      <p>
        Count 2: <strong>{count2}</strong>
      </p>
      <p>
        <CountUpperMemoized name="Standard 1" countUp={countUp1} />
        <CountUpperMemoized name="Standard 2" countUp={countUp2} />
      </p>
      <p>
        <CountUpperMemoized name="useCallback 1" countUp={callback1} />
        <CountUpperMemoized name="useCallback 2" countUp={callback2} />
      </p>
    </div>
  );
}

type PropsType = {
  name: string;
  countUp: () => void;
};

function CountUpper({ name, countUp }: PropsType) {
  const [ref] = useDebugLog(name);
  return (
    <button ref={ref} onClick={countUp}>
      Count up: {name}
    </button>
  );
}

const CountUpperMemoized = React.memo(CountUpper);
