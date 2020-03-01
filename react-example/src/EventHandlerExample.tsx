import React, { useState, MouseEvent, ChangeEvent, KeyboardEvent, FormEvent, useCallback } from 'react';

export default () => {
  const [output, setOutput] = useState("");

  const f = (a: any) => {
    const aa = Object.entries(a).map(([key, value]) => `${key}: ${value}`);
    aa.sort();
    setOutput(aa.join("\n"));
  };

  const handleClick = (event: MouseEvent<HTMLButtonElement>) => f(event);
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => f(event);
  const handleKey = (event: KeyboardEvent<HTMLInputElement>) => f(event);
  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    f(event);
  };

  //useCallbackを経由すれば型推論が効く？
  const handleClick2 = useCallback(event => {
    try {
      // と思ったけど型推論が効いているのではなく (...args: any[]) => any と解釈されているだけっぽい
      // cf. node_modules/@types/react/index.d.ts
      //     useCallbackの定義は次の通り
      //     function useCallback<T extends (...args: any[]) => any>(callback: T, deps: DependencyList): T;
      event.xxxyyyzzz();
    } catch (e) {
    }
    f(event);
  }, []);

  const handleClick3: React.MouseEventHandler<HTMLButtonElement> = event => f(event);

  return (
    <div style={{ display: "flex" }}>
      <div style={{ margin: ".5rem" }}>
        <p>Click: <button onClick={handleClick}>Click</button></p>
        <p>Change: <input type="text" onChange={handleChange}/></p>
        <p>KeyDown: <input type="text" onKeyDown={handleKey}/></p>
        <p>KeyPress: <input type="text" onKeyPress={handleKey}/></p>
        <p>KeyUp: <input type="text" onKeyUp={handleKey}/></p>
        <form onSubmit={handleSubmit}>
          Submit: 
          <input type="text"/>
          <button type="submit">Submit</button>
        </form>
        <hr/>
        <p>With useCallback: <button onClick={handleClick2}>Click</button></p>
        <hr/>
        <p>Click(Handler type): <button onClick={handleClick3}>Click</button></p>
      </div>
      <div style={{ margin: ".5rem" }}>
        <pre>{output}</pre>
      </div>
    </div>
  );
};

