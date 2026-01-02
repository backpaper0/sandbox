import React, { useState, useMemo } from "react";
import { useDebugLog } from "./Hooks";

export default function MemoExample1() {
  const [count1, setCount1] = useState(0);
  const [count2, setCount2] = useState(0);
  const name1 = "Count 1";
  const name2 = "Count 2";
  function countUp1() {
    setCount1((a) => a + 1);
  }
  function countUp2() {
    setCount2((a) => a + 1);
  }
  const counter1UseMemo = useMemo(
    () => <Counter name={`useMemo(${name1})`} count={count1} />,
    [count1, name1 /* warning対策 */],
  );
  const counter2UseMemo = useMemo(
    () => <Counter name={`useMemo(${name2})`} count={count2} />,
    [count2, name2 /* warning対策 */],
  );
  return (
    <div>
      <h1>Memo</h1>
      <p>
        <button onClick={countUp1}>Count up 1</button>
        <button onClick={countUp2}>Count up 2</button>
      </p>
      <h2>特に何もしない</h2>
      <Counter name={name1} count={count1} />
      <Counter name={name2} count={count2} />
      <h2>React.memo</h2>
      <CounterMemoized name={`React.memo(${name1})`} count={count1} />
      <CounterMemoized name={`React.memo(${name2})`} count={count2} />
      <h2>React.useMemo</h2>
      {counter1UseMemo}
      {counter2UseMemo}
    </div>
  );
}

const CounterMemoized = React.memo(Counter);

type PropsType = {
  name: string;
  count: number;
};

function Counter({ name, count }: PropsType) {
  const [ref] = useDebugLog(name);
  return (
    <p ref={ref}>
      {name}: {count}
    </p>
  );
}
