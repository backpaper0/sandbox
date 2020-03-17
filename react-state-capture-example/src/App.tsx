import React, { useState, useEffect, useRef } from 'react';

function App() {
  const [page, setPage] = useState(1);
  const Page = page === 1 ? Foo :
               page === 2 ? Bar : Baz;
  return (
    <div>
      <p>
        <button onClick={() => setPage(1)}>Foo</button>
        <button onClick={() => setPage(2)}>Bar</button>
        <button onClick={() => setPage(3)}>Baz</button>
      </p>
      <Page/>
    </div>
  );
}

export default App;

type F = React.MouseEventHandler<HTMLButtonElement>;

//useEffectに渡す関数がmessageをキャプチャするのでsetMessageで
//値を変えてもコンソールへ出力される値は変更されない
function Foo() {
  const [message, setMessage] = useState("foo1");
  const changeMessage: F = event => {
    setMessage("foo2");
  };
  useEffect(() => {
    const timerID = setInterval(() => {
      console.log(message);
    }, 1000);
    return () => {
      console.log("foo unmount");
      clearInterval(timerID);
    };
  }, []);
  return (
    <div>
      <p>{message}</p>
      <p><button onClick={changeMessage}>Change message</button></p>
    </div>
  );
}

//useEffectの第2引数にmessageを渡すことでmessageに変化があれば
//useEffectに渡した関数が再度実行されるようになる
//このときunmount用の関数が呼ばれる
function Bar() {
  const [message, setMessage] = useState("bar1");
  const changeMessage: F = event => {
    setMessage("bar2");
  };
  useEffect(() => {
    const timerID = setInterval(() => {
      console.log(message);
    }, 1000);
    return () => {
      console.log("bar unmount");
      clearInterval(timerID);
    };
  }, [message]);
  return (
    <div>
      <p>{message}</p>
      <p><button onClick={changeMessage}>Change message</button></p>
    </div>
  );
}

//useRefで値を受け渡すという手もあるか
function Baz() {
  const [message, setMessage] = useState("baz1");
  const messageRef = useRef("");
  messageRef.current = message;
  const changeMessage: F = event => {
    setMessage("baz2");
  };
  useEffect(() => {
    const timerID = setInterval(() => {
      console.log(messageRef.current);
    }, 1000);
    return () => {
      console.log("baz unmount");
      clearInterval(timerID);
    };
  }, []);
  return (
    <div>
      <p>{message}</p>
      <p><button onClick={changeMessage}>Change message</button></p>
    </div>
  );
}
