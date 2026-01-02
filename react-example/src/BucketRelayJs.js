import React from "react";

export default function BucketRelayJs() {
  return (
    <div className="root">
      <h1>BucketRelayJs</h1>
      <Component1 foo="FOO" bar="BAR" />
    </div>
  );
}

function Component1({ foo, bar }) {
  return (
    <div className="component1">
      <Component2 foo={foo} bar={bar} />
    </div>
  );
}

function Component2(props) {
  return (
    <div className="component2">
      <Component3 {...props}>Hello, world!</Component3>
    </div>
  );
}

function Component3({ foo, bar, children }) {
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
