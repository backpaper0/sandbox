import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

import UseStateTypeInfer from "./UseStateTypeInfer";
import Props from "./Props";
import EventHandlerExample from "./EventHandlerExample";
import RenderingTimingExample from "./RenderingTimingExample";

const App: React.FC = () => {
	return (
    <Router>
      <div style={{ display: "flex" }}>
        <nav style={{ margin: ".5rem" }}>
          <ul>
            <li>
              <Link to="/UseStateTypeInfer">UseStateTypeInfer</Link>
            </li>
            <li>
              <Link to="/Props">Props</Link>
            </li>
            <li>
              <Link to="/EventHandlerExample">EventHandlerExample</Link>
            </li>
            <li>
              <Link to="/RenderingTimingExample">RenderingTimingExample</Link>
            </li>
          </ul>
        </nav>
        <div style={{ margin: ".5rem" }}>
          <Switch>
            <Route path="/UseStateTypeInfer">
              <UseStateTypeInfer />
            </Route>
            <Route path="/Props">
              <Props />
            </Route>
            <Route path="/EventHandlerExample">
              <EventHandlerExample />
            </Route>
            <Route path="/RenderingTimingExample">
              <RenderingTimingExample />
            </Route>
          </Switch>
        </div>
      </div>
    </Router>
	);
}

export default App;
