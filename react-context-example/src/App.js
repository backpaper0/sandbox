import React, { Component } from 'react';
import Context from './context';
import Message from './Message';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      message: 'foo',
      setState: this.setState.bind(this)
    };
  }

  render() {
    return (
      <Context.Provider value={this.state}>
        <Message/>
      </Context.Provider>
    );
  }
}

App.contextType = Context;

export default App;
