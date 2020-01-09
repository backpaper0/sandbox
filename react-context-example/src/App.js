import React, { useState } from 'react';
import Context from './context';
import Message from './Message';

const App = () => {
  const [message, setMessage] = useState("foo");
  const state = {
    message,
    setState: ({ message }) => setMessage(message)
  };
  return (
    <Context.Provider value={state}>
      <Message/>
    </Context.Provider>
  );
};

export default App;
