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
  <FadeOut init={{ opacity: 1 }} transition={{ opacity: 0, transition: "opacity 1000ms" }} delay={1000}>
    <img src={splash}/>
  </FadeOut>
);

const FadeOut: React.FC<{ init: any, transition: any, delay: number, children: any }> = ({ init, transition, delay, children }) => {
  const [begin, setBegin] = useState(false);
  useEffect(() => {
    setTimeout(() => setBegin(true), delay);
  }, []);
  return (
    <div style={ begin ? transition : init }>
      {children}
    </div>
  );
};
