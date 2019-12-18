import React, { useState, useEffect } from 'react';

const App: React.FC = () => {
	const [count, setCount] = useState(0);
	const [isLoading, setLoading] = useState(false);
	const countUp = () => {
		setLoading(true);
		setTimeout(() => {
			setLoading(false);
			setCount(count => count + 1);
		}, 200);
	};
	useEffect(() => {
		document.title = `You clicked ${count} times`;
	});
	return (
		<div>
			<button onClick={countUp} disabled={isLoading}>
				{isLoading ? "loading..." : "increase"}
			</button>
			<div>count: {count}</div>
		</div>
	);
}

export default App;
