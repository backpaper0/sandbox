import React from 'react';
import styled from 'styled-components';

const down: (x: number, y: number, leftIsWall: boolean) => [number, number] = (x, y, leftIsWall) => {
	const isFirst = y === 2;
	//左が壁なら右と下の2方向、左が通路なら左右と下の3方向へ倒せる
	//1行目なら上方向にも倒せる
	const randomSize = (leftIsWall ? 2 : 3) + (isFirst ? 1 : 0);
	const randomValue = Math.floor(Math.random() * randomSize);
	//1行目でなければcase 1〜3で処理するため+1する
	//こうすることで1行目とそれ以外でswitchを分ける必要がなくなる
	switch (randomValue + (isFirst ? 0 : 1)) {
		case 0:
			return [x, y - 1];
		case 1:
			return [x, y + 1];
		case 2:
			return[x + 1, y];
		case 3:
			return [x - 1, y];
	}
	throw "Unreachable";
};

const App: React.FC = () => {
	const height = 29;
	const width = 49;
	const fields = Array.from((function*() {
		for (let y = 0; y < height; y++) {
			yield Array.from((function*() {
				for (let x = 0; x < width; x++) {
					//外周とxyが共に偶数の座標は壁
					yield (y === 0) || (y === height - 1) || (x === 0) || (x === width - 1) || (x % 2 === 0 && y % 2 === 0);
				}
			})());
		}
	})());
	//棒倒しを行う	
	for (let y = 2; y < height - 2; y += 2) {
		for (let x = 2; x < width - 2; x += 2) {
			const [x_, y_] = down(x, y, fields[y][x - 1]);
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

