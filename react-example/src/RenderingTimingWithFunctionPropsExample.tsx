import React, { useState, useEffect, useCallback } from "react";

//あんまりTypeScript関係ないexampleになっちゃった

export default function RenderTimingWithFunctionPropsExample () {
  const [count, setCount] = useState(0);
  const fn = () => { /* なにもしない関数 */ };
  const fnM = useCallback(fn, []);
  console.log("--------------------------------");
  return (
    <div>
      <p><button onClick={() => setCount(count + 1)}>Update state</button></p>
      <Fn name="1. Raw Component x Raw Function" fn={fn}/>
      <FnM name="2. Memoize Component x Raw Function" fn={fn}/>
      <Fn name="3. Raw Component x Memoize Function" fn={fnM}/>
      <FnM name="4. Memoize Component x Memoize Function" fn={fnM}/>
    </div>
  );
};

const Fn: React.FC<{ name: string; fn: () => void; }> = ({ name, fn }) => {
  console.log(`${name} (${new Date()})`);
  return (
    <p>{name}</p>
  );
};

const FnM = React.memo(Fn);

