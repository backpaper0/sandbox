import { createModule } from 'typeless';
import { CounterSymbol } from './symbol';

export const [handle, Actions, getState] = createModule(CounterSymbol)
	.withActions({
		startCount: null,
		countDone: (count: number) => ({ payload: { count }}),
	})
	.withState<CounterState>();

export interface CounterState {
	isLoading: boolean;
	count: number;
}
