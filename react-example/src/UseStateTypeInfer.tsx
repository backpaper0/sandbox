import React, { useState } from "react";

export default function UseStateTypeInfer() {
  // useStateの戻り値は配列の要素毎に型が明示されているので
  // 代入するときに型推論されるっぽい
  // https://github.com/facebook/react/blob/v16.12.0/packages/react-reconciler/src/ReactFiberHooks.js#L65
  const [count, setCount] = useState(0);
  test1(count);
  test2(setCount);
  return (
    <div>
      <p>You clicked {count} times</p>
      <button onClick={() => setCount(count + 1)}>Click me</button>
    </div>
  );
}

function test1(_: number) {}

function test2(_: (count: number) => void) {}
