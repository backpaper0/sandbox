import React from 'react';

export interface SetStateType {
  (state : { message: string }): void;
}

export interface ContextType {
  message: string;
  setState: SetStateType;
}

export default React.createContext<ContextType>({ message: "", setState: state => {} });
