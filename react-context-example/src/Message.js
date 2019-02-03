import React, { Component } from 'react';
import Context from './context';

class App extends Component {

  handleClick() {
    const { setState } = this.context;
    setState({ message: 'bar' });
  }

  render() {
    return (
      <Context.Consumer>{ ({ message }) =>
        <div>
          <button onClick={this.handleClick.bind(this)}>Click me</button>
          {message}
        </div>
      }</Context.Consumer>
    );
  }
}

App.contextType = Context;

export default App;
