import React from "react";

type ContextType = {
  counter1: number;
  countUp1(): void;
  counter2: number;
  countUp2(): void;
};

const defaultValue = {
  counter1: 0,
  countUp1: () => {},
  counter2: 0,
  countUp2: () => {},
};

export default React.createContext<ContextType>(defaultValue);
