import React, { useState } from 'react';
import Context, { SetStateType } from './context';
import Message from './Message';

const App: React.FC = () => {
  const [message, setMessage] = useState("foo");
  const setState: SetStateType = ({ message }) => setMessage(message);
  const state = {
    message,
    setState
  };
  return (
    <Context.Provider value={state}>
      <Message/>
    </Context.Provider>
  );
};

export default App;
