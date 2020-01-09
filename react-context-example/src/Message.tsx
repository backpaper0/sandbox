import React, { useState, useContext } from 'react';
import Context from './context';

const Message: React.FC = () => {
  const { message, setState } = useContext(Context);
  const handleClick = () => setState({ message: "bar" });
  return (
    <div>
      <button onClick={handleClick}>Click me</button>
      {message}
    </div>
  );
};

export default Message;
