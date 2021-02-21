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

const CounterContext = React.createContext();

function Counter({ children }) {
  console.log('Counter');
  const countState = React.useState(0);
  return (
    <CounterContext.Provider value={countState}>
      {children}
    </CounterContext.Provider>
  );
}

function CountDisplay() {
  console.log('CountDisplay');
  const [count] = React.useContext(CounterContext);
  return (
    <h1>{count}</h1>
  );
}

function CountUp() {
  console.log('CountUp');
  const [count, setCount] = React.useContext(CounterContext);
  const handleCountUp = () => {
    setCount(count + 1);
  };
  return (
    <button onClick={handleCountUp}>Count up</button>
  );
}

const domContainer = document.querySelector('#root');
ReactDOM.render(<App/>, domContainer);
