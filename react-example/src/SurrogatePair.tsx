import React, { useState } from "react";

export default function SurrogatePair() {
  const [text, setText] = useState('');
  const handleChange = (event: React.FormEvent<HTMLInputElement>) => {
    if (event.currentTarget.dataset.maxlength !== undefined) {
      const maxlength = Number.parseInt(event.currentTarget.dataset.maxlength);
      setText([...event.currentTarget.value].slice(0, maxlength).join(''));
    } else {
      setText(event.currentTarget.value);
    }
  };
  return (
    <div>
      <dl>
        <dt>単にmaxlength=5</dt>
        <dd><input type="text" maxLength={5}/></dd>
        <dt>サロゲートペアを考慮</dt>
        <dd><input type="text" value={text} onChange={handleChange} data-maxlength={5}/></dd>
      </dl>
      <dl>
        <dt>サロゲートペアのサンプル</dt>
        <dd><input type="text" value="𩸽" readOnly/></dd>
        <dd><input type="text" value="🍣" readOnly/></dd>
      </dl>
    </div>
  );
}

