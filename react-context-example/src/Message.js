import React, { useState, useContext } from 'react';
import Context from './context';

const Message = () => {
  const { message, setState } = useContext(Context);
  const handleClick = evt => setState({ message: "bar" });
  return (
    <div>
      <button onClick={handleClick}>Click me</button>
      {message}
    </div>
  );
};

export default Message;
