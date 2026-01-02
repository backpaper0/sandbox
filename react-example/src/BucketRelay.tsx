import React from "react";

export default function BucketRelay() {
  return (
    <div className="root">
      <h1>BucketRelay</h1>
      <Component1 foo="FOO" bar="BAR" />
    </div>
  );
}

function Component1({ foo, bar }: Props1) {
  return (
    <div className="component1">
      <Component2 foo={foo} bar={bar} />
    </div>
  );
}

function Component2(props: Props1) {
  return (
    <div className="component2">
      <Component3 {...props}>Hello, world!</Component3>
    </div>
  );
}

function Component3({ foo, bar, children }: Props2) {
  return (
    <div className="component3">
      <ul>
        <li>{children}</li>
        <li>{foo}</li>
        <li>{bar}</li>
      </ul>
    </div>
  );
}

interface Props1 {
  foo: string;
  bar: string;
}

interface Props2 extends Props1 {
  children: React.ReactNode;
}
