import React, { useEffect, useRef } from 'react';
import { BrowserQRCodeSvgWriter } from '@zxing/library';

function App() {
  const input = 'https://zxing-js.github.io/library/';
  return (
    <div>
      <h1>React QR code example</h1>
      <QRCode input={input} width={300} height={300}/>
    </div>
  );
}

export default App;

type QRCodeProps = {
  input: string;
  width: number;
  height: number;
};

const QRCode: React.FC<QRCodeProps> = ({ input, width, height }) => {
  const ref = useRef<HTMLDivElement|null>(null);
  useEffect(() => {
    if (ref.current !== null) {
      const codeWriter = new BrowserQRCodeSvgWriter();
      codeWriter.writeToDom(ref.current, input, width, height);
    }
  }, [input, height, width]);
  return (
    <div ref={ref}></div>
  );
};

