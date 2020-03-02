import React, { useState, useEffect } from "react";

import splash from "./splash.png";

export default () => {
  const [initialized, setInitialized] = useState(false);
  useEffect(() => {
    setTimeout(() => setInitialized(true), 2000);
  }, []);
  if (initialized) {
    return (
      <App/>
    );
  }
  return (
    <Splash/>
  );
};

const App = () => (
  <div>
    <h1>HELLO WORLD</h1>
  </div>
);

const Splash = () => (
  <div>
    <img src={splash}/>
  </div>
);
