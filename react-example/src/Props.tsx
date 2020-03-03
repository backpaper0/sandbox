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

const Demo2: React.FC<Demo2Props> = ({ foo, bar, baz }) => {
  return (
    <li>{`${foo} ${bar} ${baz}`}</li>
  );
};

//Type Aliasでもよさそう
type Demo2Props = {
  foo: string;
  bar: number;
  baz: boolean;
};

export default () => (
  <ul>
    <Demo foo="abc" bar={123} baz={true}/>
    <Demo foo="def" bar={456} baz={false}/>
    <Demo2 foo="ghi" bar={789} baz={true}/>
  </ul>
);