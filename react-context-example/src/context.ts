import React from 'react';

interface ContextType {
  message: string;
  setMessage: (message: string) => void;
}

export default React.createContext<ContextType>({
  message: "",
  setMessage: message => {}
});
