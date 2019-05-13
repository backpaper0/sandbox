import React from 'react';
import I18nContext from './i18n';
import Message from './Message';

function App() {
  return (
    <div>
      <Message msg="hello"/>
      <I18nContext.Provider value={{ hello: "こんにちは世界！" }}>
        <Message msg="hello"/>
      </I18nContext.Provider>
      <I18nContext.Provider value={{ hello: "Ciao mondo!" }}>
        <Message msg="hello"/>
      </I18nContext.Provider>
    </div>
  );
}

export default App;
