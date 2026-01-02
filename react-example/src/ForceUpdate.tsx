import React, { useState } from "react";

export default function ForceUpdate() {
  const [, forceUpdate] = useState([]);
  const [, noUpdate1] = useState(0);
  const [, noUpdate2] = useState("xxx");
  return (
    <div>
      <h1>ForceUpdate: {Math.random()}</h1>
      <ul>
        <li>
          <button onClick={() => forceUpdate([])}>
            <code>[]</code>は<code>render</code>される
          </button>
        </li>
        <li>
          <button onClick={() => noUpdate1(0)}>
            <code>0</code>は<code>render</code>されない
          </button>
        </li>
        <li>
          <button onClick={() => noUpdate2("xxx")}>
            <code>'xxx'</code>は<code>render</code>されない
          </button>
        </li>
      </ul>
      <p>
        <code>setter([])</code>とすることで<code>render</code>させることができるっぽい。
      </p>
      <p>
        <code>setter(0)</code>や<code>setter('xxx')</code>だと<code>render</code>されない。
      </p>
      <p>
        <code>0 === 0</code>や<code>'xxx' === 'xxx'</code>は<code>true</code>で、
        <code>[] === []</code>は<code>false</code>だからかな。
      </p>
    </div>
  );
}
