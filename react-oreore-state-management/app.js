'use strict';

function App() {
  console.log('App');
  return (
    <Counter>
      <CountDisplay/>
      <CountUp/>
    </Counter>
  );
}

class MyState {
  value = new Map();
  listeners = new Map();
  getState(name) {
    return this.value.get(name);
  }
  setState(name, newValue) {
    const oldValue = this.getState(name);
    if (newValue !== oldValue) {
      this.value.set(name, newValue);
      const listeners = this.listeners.get(name);
      if (listeners !== undefined) {
        listeners.forEach(listener => {
          listener();
        });
      }
    }
  }
  setInitialState(name, initialValue) {
    this.value.set(name, initialValue);
  }
  subscribe(name, listener) {
    const listeners = this.listeners.get(name);
    if (listeners !== undefined) {
      listeners.add(listener);
    } else {
      const set = new Set();
      set.add(listener);
      this.listeners.set(name, set);
    }
    return () => {
      const listeners = this.listeners.get(name);
      listeners.delete(listener);
    };
  }
}

const MyStateContext = React.createContext();

function useMyState(name, initialValue) {
  const myState = React.useContext(MyStateContext);
  const [, forceUpdate] = React.useState([]);
  React.useEffect(() => {
    const unsubscribe = myState.current.subscribe(name, () => {
      forceUpdate([]);
    });
    return unsubscribe;
  }, []);
  let value = myState.current.getState(name);
  if (value === undefined) {
    value = initialValue;
    myState.current.setInitialState(name, value);
  }
  const setter = (newValue) => {
    myState.current.setState(name, newValue);
  };
  return [value, setter];
}

function MyStateProvider({ children }) {
  const myState = React.useRef(new MyState());
  return (
    <MyStateContext.Provider value={myState}>
      {children}
    </MyStateContext.Provider>
  );
}

function Counter({ children }) {
  console.log('Counter');
  return (
    <MyStateProvider>
      {children}
    </MyStateProvider>
  );
}

function CountDisplay() {
  console.log('CountDisplay');
  const [count] = useMyState('counter', 0);
  return (
    <h1>{count}</h1>
  );
}

function CountUp() {
  console.log('CountUp');
  const [count, setCount] = useMyState('counter', 0);
  const handleCountUp = () => {
    setCount(count + 1);
  };
  return (
    <button onClick={handleCountUp}>Count up</button>
  );
}

const domContainer = document.querySelector('#root');
ReactDOM.render(<App/>, domContainer);
