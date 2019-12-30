import React from 'react';
import styled from 'styled-components';

const f: (x: number, y: number, leftIsWall: boolean) => [number, number] = (x, y, leftIsWall) => {
	const isFirst = y === 2;
	const a: number = Math.floor(Math.random() * ((leftIsWall ? 2 : 3) + (isFirst ? 1 : 0))) + (isFirst ? 0 : 1);
	switch (a) {
		case 0:
			return [x, y - 1];
		case 1:
			return [x, y + 1];
		case 2:
			return[x + 1, y];
		case 3:
			return [x - 1, y];
	}
	return [x, y];
};

const App: React.FC = () => {
	const fields = [];
	const height = 29;
	const width = 49;
	for (let y = 0; y < height; y++) {
		const row = [];
		for (let x = 0; x < width; x++) {
			if (y === 0) {
				row.push(true);
			} else if (y === height - 1) {
				row.push(true);
			} else if (x === 0) {
				row.push(true);
			} else if (x === width - 1) {
				row.push(true);
			} else if (x % 2 === 0 && y % 2 === 0) {
				row.push(true);
			} else {
				row.push(false);
			}
		}
		fields.push(row);
	}
	
	for (let y = 2; y < height - 2; y += 2) {
		for (let x = 2; x < width - 2; x += 2) {
			const [x_, y_] = f(x, y, fields[y][x - 1]);
			fields[y_][x_] = true;
		}
	}
  return (
    <div>
			<Table>
				{fields.map(row => (<tr>
					{row.map(a => <Cell wall={a}/>)}
				</tr>))}
			</Table>
    </div>
  );
}

export default App;

const Table = styled.table`
	border-collapse: collapse;
`;

interface CellProps {
	wall: boolean
}
const cellLength = 10;
const Cell = styled.td<CellProps>`
	border: 0;
	width: ${cellLength}px;
	height: ${cellLength}px;
	background: ${props => props.wall ? "black" : "white"}
`;

