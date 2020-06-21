import React, { useState, useEffect } from "react";

export default function Initialize() {
  //setIntervalでstateを変更して子コンポーネントをrenderさせる
  //renderされることで余計に初期処理が実行されないか確認する
  const [count, setCount] = useState(0);
  useEffect(() => {
    const timerID = window.setInterval(() => setCount(a => a + 1), 1000);
    return () => {
      window.clearInterval(timerID);
    };
  }, []);
  console.log('[root]', count);
  return (
    <React.Fragment>
      <p>{count}</p>
      <UseEffect/>
      <UseState/>
      <CustomHook/>
    </React.Fragment>
  );
}

function UseEffect() {
  const [origin, setOrigin] = useState('');
  console.log('useEffect', origin);
  useEffect(() => {
    fetch('https://httpbin.org/delay/1').then(a => a.json()).then(({ origin }) => {
      setOrigin(origin);
    });

  }, []);
  if (origin === '') {
    return <p>Loading...</p>;
  }
  return (
    <p>useEffect: {origin}</p>
  );
}

function UseState() {
  const [initialized, setInitialized] = useState(false);
  const [initializing, setInitializing] = useState(false);
  const [origin, setOrigin] = useState('');
  console.log('useState', initialized, initializing, origin);
  if (initialized === false) {
    if (initializing === false) {
      setInitializing(true);
      fetch('https://httpbin.org/delay/1').then(a => a.json()).then(({ origin }) => {
        setOrigin(origin);
      }).finally(() => {
        setInitialized(true);
      });
    }
  }
  if (initialized === false) {
    return <p>Loading...</p>;
  }
  return (
    <p>useState: {origin}</p>
  );
}

function CustomHook() {
  const [origin, setOrigin] = useState('');
  const initialized = useInit(done => {
    fetch('https://httpbin.org/delay/1').then(a => a.json()).then(({ origin }) => {
      setOrigin(origin);
    }).finally(done);
  });
  console.log('custom hook', initialized, origin);
  if (initialized === false) {
    return <p>Loading...</p>;
  }
  return (
    <p>custom hook: {origin}</p>
  );
}

function useInit(fn: (done: () => void) => void) {
  const [initialized, setInitialized] = useState(false);
  const [initializing, setInitializing] = useState(false);
  if (initialized === false) {
    if (initializing === false) {
      setInitializing(true);
      fn(() => setInitialized(true));
    }
  }
  return initialized;
}

