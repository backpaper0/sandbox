import React from 'react';
import { BrowserRouter, Redirect, Route, Switch, Link } from 'react-router-dom';

function App() {
  return (
    <BrowserRouter basename="/foobar/">
      <ul>
        <li><Link to="/foo">Foo</Link></li>
        <li><Link to="/bar">Bar</Link></li>
        <li><Link to="/baz">Baz</Link></li>
      </ul>
      <Switch>
        <Route exact path="/">
          <Redirect to="/foo"/>
        </Route>
        <Route exact path="/foo">
          <Foo/>
        </Route>
        <Route exact path="/bar">
          <Bar/>
        </Route>
        <Route exact path="/baz">
          <Baz/>
        </Route>
      </Switch>
    </BrowserRouter>
  );
}

export default App;

const Foo = () => <h1>Foo</h1>;
const Bar = () => <h1>Bar</h1>;
const Baz = () => <h1>Baz</h1>;

