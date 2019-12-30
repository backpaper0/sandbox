import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

//棒倒し法でダンジョン生成

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
  throw new Error("Unreachable");
};

const generateFields = (width: number, height: number) => {
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
  return fields;
};

const initialHeight = 37;
const initialWidth = 65;
const initialCellSize = 10;
const initialFields = generateFields(initialWidth, initialHeight);

type Generator = (width: number, height: number, cellSize: number) => void;
interface SettingProps {
  generate: Generator;
}

const Setting: React.FC<SettingProps> = ({ generate }) => {
  const [temporaryHeight, setTemporaryHeight] = useState(initialHeight.toString());
  const [temporaryWidth, setTemporaryWidth] = useState(initialWidth.toString());
  const [temporaryCellSize, setTemporaryCellSize] = useState(initialCellSize.toString());
  const [invalid, setInvalid] = useState(true);
  const width = Number.parseInt(temporaryWidth);
  const height = Number.parseInt(temporaryHeight);
  const cellSize = Number.parseInt(temporaryCellSize);
  if (invalid !== (Number.isNaN(width) || Number.isNaN(height) || Number.isNaN(cellSize)
    || width < 5 || height < 5 || cellSize < 2
    || width % 2 === 0 || height % 2 === 0)) {
    setInvalid(!invalid);
  }
  const handleGenerate = () => {
    generate(width, height, cellSize);
  };
  return (
    <table>
    <tbody>
    <tr>
    <th>幅</th>
    <td><input value={temporaryWidth} onChange={event => setTemporaryWidth(event.target.value)}/></td>
    <th>高さ</th>
    <td><input value={temporaryHeight} onChange={event => setTemporaryHeight(event.target.value)}/></td>
    <th>セルサイズ</th>
    <td><input value={temporaryCellSize} onChange={event => setTemporaryCellSize(event.target.value)}/></td>
    <td><button onClick={event => handleGenerate()} disabled={invalid}>生成</button></td>
    </tr>
    </tbody>
    </table>
  );
};

const App: React.FC = () => {
  useEffect(() => {
    document.title = "棒倒し法でダンジョン生成";
  }, []);
  const [cellSize, setCellSize] = useState(initialCellSize);
  const [fields, setFields] = useState(initialFields);
  const generate: Generator = (width, height, cellSize) => {
    setCellSize(cellSize);
    const fields = generateFields(width, height);
    setFields(fields);
  };
  return (
    <div>
    <Setting generate={generate}/>
    <Table>
    <tbody>
    {fields.map((row, y) => (<tr key={`${y}`}>
      {row.map((isWall, x) => <Cell key={`${x}`} wall={isWall} cellSize={cellSize} x={x} y={y}/>)}
      </tr>))}
    </tbody>
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
  cellSize: number
  x: number
  y: number
}
const Cell = styled.td<CellProps>`
  border: 0;
  width: ${props => props.cellSize / (props.x % 2 === 0 ? 2 : 1)}px;
  height: ${props => props.cellSize / (props.y % 2 === 0 ? 2 : 1)}px;
  background: ${props => props.wall ? "black" : "white"}
`;

