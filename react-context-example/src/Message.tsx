import React, { useState, useContext } from 'react';
import Context from './context';

const Message: React.FC = () => {
  const { message, setMessage } = useContext(Context);
  const handleClick = () => setMessage("bar");
  return (
    <div>
      <button onClick={handleClick}>Click me</button>
      {message}
    </div>
  );
};

export default Message;
