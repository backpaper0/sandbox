import React from "react";

const Demo: React.FC<DemoProps> = ({ foo, bar, baz }) => {
  return (
    <li>{`${foo} ${bar} ${baz}`}</li>
  );
};

interface DemoProps {
  foo: string;
  bar: number;
  baz: boolean;
}

export default () => (
  <ul>
    <Demo foo="abc" bar={123} baz={true}/>
    <Demo foo="def" bar={456} baz={false}/>
  </ul>
);
