import React from 'react';

export default React.createContext({
  message: 'foo',
  setMessage: message => this.message = message
});
