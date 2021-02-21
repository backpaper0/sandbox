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

class CounterState {
  value = 0;
  listeners = [];
  getState() {
    return this.value;
  }
  setState(value) {
    if (this.value !== value) {
      this.value = value;
      this.listeners.forEach(listener => {
        listener(value);
      });
    }
  }
  subscribe(listener) {
    this.listeners.push(listener);
  }
}

const CounterContext = React.createContext();

function Counter({ children }) {
  console.log('Counter');
  const ref = React.useRef(new CounterState());
  return (
    <CounterContext.Provider value={ref}>
      {children}
    </CounterContext.Provider>
  );
}

function CountDisplay() {
  console.log('CountDisplay');
  const ref = React.useContext(CounterContext);
  const [, forceUpdate] = React.useState([]);
  ref.current.subscribe(() => {
    forceUpdate([]);
  });
  return (
    <h1>{ref.current.getState()}</h1>
  );
}

function CountUp() {
  console.log('CountUp');
  const ref = React.useContext(CounterContext);
  const [, forceUpdate] = React.useState([]);
  ref.current.subscribe(() => {
    forceUpdate([]);
  });
  const handleCountUp = () => {
    ref.current.setState(ref.current.getState() + 1);
  };
  return (
    <button onClick={handleCountUp}>Count up</button>
  );
}

const domContainer = document.querySelector('#root');
ReactDOM.render(<App/>, domContainer);
