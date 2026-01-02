import React, { useState, useEffect } from "react";

import splash from "./splash.png";

export default function SplashExample() {
  const [initialized, setInitialized] = useState(false);
  const callback = () => setInitialized(true);
  if (initialized) {
    return <App />;
  }
  return <Splash callback={callback} />;
}

const App = () => (
  <div>
    <h1>HELLO WORLD</h1>
  </div>
);

const Splash: React.FC<{ callback: () => void }> = ({ callback }) => (
  <FadeOut
    init={{ opacity: 1 }}
    transition={{ opacity: 0, transition: "opacity 1500ms" }}
    delay={1500}
    callback={callback}
  >
    <img alt="Splash" src={splash} />
  </FadeOut>
);

type FadeOutProps = {
  init: any;
  transition: any;
  delay: number;
  children: any;
  callback: () => void;
};

const FadeOut: React.FC<FadeOutProps> = ({ init, transition, delay, children, callback }) => {
  const [begin, setBegin] = useState(false);
  useEffect(() => {
    const timerId = setTimeout(() => setBegin(true), delay);
    return () => {
      window.clearTimeout(timerId);
    };
  }, [delay]);
  return (
    <div style={begin ? transition : init} onTransitionEnd={callback}>
      {children}
    </div>
  );
};
