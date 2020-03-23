import React from 'react';

export default function Misc() {
  const a: A2 = ["abc", 123]
  return (
    <div>
      <h1>Misc</h1>
      <p>{a[0]} {a[1]}</p>
    </div>
  );
}


interface A1 {
  foo: string;
  bar: number;
}

type A2 = [A1["foo"], A1["bar"]];


