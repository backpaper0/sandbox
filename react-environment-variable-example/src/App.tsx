import React from 'react';

function App() {
  const title = process.env.REACT_APP_TITLE;
  return (
    <div className="App">
      <h1>{title}</h1>
    </div>
  );
}

export default App;
