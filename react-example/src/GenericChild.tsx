import React from 'react';

export default function GenericChild() {
  return (
    <div>
      <Child values={[12,34,56,78,90]} fn={a => <li>{typeof(a)} {a} hex={a.toString(16)}</li>}/>
      <Child values={["foobar","hoge","helloworld"]} fn={a => <li>{typeof(a)} {a} length={a.length}</li>}/>
      <Child values={[true,false]} fn={a => <li>{typeof(a)} {a.toString()}</li>}/>
    </div>
  );
}

type ChildProps<T> = {
  values?: T[];
  fn: (value: T) => React.ReactNode;
};

function Child<T>({ values, fn }: ChildProps<T>) {
  if (!values) {
    return null;
  }
  return (
    <ul>
      {values.map(fn)}
    </ul>
  );
};
