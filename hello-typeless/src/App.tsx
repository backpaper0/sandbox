import React from 'react';

import { createModule, useActions } from 'typeless';

const CounterSymbol = Symbol("Counter");

const [handle, Actions, getState] = createModule(CounterSymbol)
	.withActions({
		startCount: null,
		countDone: (count: number) => ({ payload: { count }}),
	})
	.withState<CounterState>();

interface CounterState {
	isLoading: boolean;
	count: number;
}

handle
	.epic()
	.on(Actions.startCount, () => {
		return new Promise(resolve => {
			setTimeout(() => {
				const ret = Actions.countDone(1);
				resolve(ret);
			}, 200);
		});
	});

const initState: CounterState = {
	isLoading: false,
	count: 0,
};

handle
	.reducer(initState)
	.on(Actions.startCount, state => {
		state.isLoading = true;
	})
	.on(Actions.countDone, (state, { count }) => {
		state.isLoading = false;
		state.count += count;
	});

const Counter: React.FC = () => {
	const { startCount } = useActions(Actions);
	const { isLoading, count } = getState.useState();
  return (
    <div>
			<button disabled={isLoading} onClick={startCount}>
				{isLoading ? "loading..." : "increase"}
			</button>
			<div>count: {count}</div>
    </div>
  );
};

const App: React.FC = () => {
	handle();
  return (
    <div>
			<Counter/>
			<Counter/>
    </div>
  );
}

export default App;
