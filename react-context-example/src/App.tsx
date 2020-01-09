import React, { useState } from 'react';
import Context from './context';
import Message from './Message';

const App: React.FC = () => {
  const [message, setMessage] = useState("foo");
  const state = {
    message,
    setMessage
  };
  return (
    <Context.Provider value={state}>
      <Message/>
    </Context.Provider>
  );
};

export default App;
