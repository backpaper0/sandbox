import React, { useState } from "react";

//あんまりTypeScript関係ないexampleになっちゃった

//Counterが再レンダーされると子コンポーネントも再レンダーされるっぽい
const Counter = () => {
  console.log("Counter");
  const [count, setCount] = useState(0);
  const [overview, setOverview] = useState("Simple counter");
  return (
    <div>
      <CounterTitle/>
      <MemoizedCounterOverview overview={overview} setOverview={setOverview}/>
      <CountDisplay count={count}/>
      <CountUpper setCount={setCount}/>
    </div>
  );
};

type Setter<T> = React.Dispatch<React.SetStateAction<T>>;

const CounterTitle = () => {
  console.log("CounterTitle");
  return (
    <h1>Counter</h1>
  );
};

interface CounterOverviewProps {
  overview: string;
  setOverview: Setter<string>;
}

const CounterOverview: React.FC<CounterOverviewProps> = ({ overview, setOverview }) => {
  console.log("CounterOverview");
  return (
    <p onClick={() => setOverview(a => `${a}*`)}>{overview}</p>
  );
};

//メモ化をするとpropsが変更されるまで再レンダーされない
const MemoizedCounterOverview = React.memo(CounterOverview);

interface CountDisplayProps {
  count: number;
}

const CountDisplay: React.FC<CountDisplayProps> = ({ count }) => {
  console.log("CountDisplay");
  return (
    <div>{count}</div>
  );
};

interface CountUpperProps {
  setCount: Setter<number>;
}

let prevSetCount: Setter<number> | null = null;

const CountUpper: React.FC<CountUpperProps> = ({ setCount }) => {
  console.log("CountUpper");
  const f = () => setCount(a => a + 1);
  const g = () => {
    if (prevSetCount !== null) {
      prevSetCount(a => a + 1);
    }
  };
  console.log(setCount === prevSetCount);
  prevSetCount = setCount;
  return (
    <div>
      <button onClick={f}>Count up</button>
      <button onClick={g}>Count up2</button>
    </div>
  );
};

export default () => {
  console.log("Root");
  return (
    <div>
      <Counter/>
    </div>
  );
};
