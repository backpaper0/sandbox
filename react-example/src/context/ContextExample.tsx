import React from "react";
import WithCounterContext from "./WithCounterContext";
import Counter1 from "./Counter1";
import Counter2 from "./Counter2";

export default function ContextExample() {
  return (
    <WithCounterContext>
      <h1>Context Example</h1>
      <Counter1 />
      <Counter2 />
    </WithCounterContext>
  );
}
