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
  subscribe(name, listener) {
    const listeners = this.listeners.get(name);
    if (listeners !== undefined) {
      listeners.push(listener);
    } else {
      this.listeners.set(name, [listener]);
    }
  }
}

const MyStateContext = React.createContext();

function Counter({ children }) {
  console.log('Counter');
  const myState = React.useRef(new MyState());
  return (
    <MyStateContext.Provider value={myState}>
      {children}
    </MyStateContext.Provider>
  );
}

function CountDisplay() {
  console.log('CountDisplay');
  const myState = React.useContext(MyStateContext);
  const [, forceUpdate] = React.useState([]);
  myState.current.subscribe('counter', () => {
    forceUpdate([]);
  });
  return (
    <h1>{myState.current.getState('counter') || 0}</h1>
  );
}

function CountUp() {
  console.log('CountUp');
  const myState = React.useContext(MyStateContext);
  const [, forceUpdate] = React.useState([]);
  myState.current.subscribe('counter', () => {
    forceUpdate([]);
  });
  const handleCountUp = () => {
    myState.current.setState('counter', (myState.current.getState('counter') || 0) + 1);
  };
  return (
    <button onClick={handleCountUp}>Count up</button>
  );
}

const domContainer = document.querySelector('#root');
ReactDOM.render(<App/>, domContainer);
