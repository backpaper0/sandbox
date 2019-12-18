import React from 'react';

import { useActions } from 'typeless';

import { handle, Actions, getState, CounterState } from "./interface";

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

export const CounterModule: React.FC = () => {
  handle();
  return (
    <div>
      <Counter/>
      <Counter/>
    </div>
  );
};

