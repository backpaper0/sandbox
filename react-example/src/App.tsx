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
import RenderingTimingWithFunctionPropsExample from "./RenderingTimingWithFunctionPropsExample";
import SplashExample from "./SplashExample";
import StateCapturedExample from "./StateCapturedExample";

const pages = [
  UseStateTypeInfer,
  Props,
  EventHandlerExample,
  RenderingTimingExample,
  RenderingTimingWithFunctionPropsExample,
  SplashExample,
  StateCapturedExample,
];

const App: React.FC = () => {
	return (
    <Router>
      <div style={{ display: "flex" }}>
        <nav style={{ margin: ".5rem" }}>
          <ul>
            {pages.map((page, index) => (
              <li key={index}>
                <Link to={`/${page.name}`}>{page.name}</Link>
              </li>
            ))}
          </ul>
        </nav>
        <div style={{ margin: ".5rem" }}>
          <Switch>
            {pages.map((page, index) => (
              <Route key={index} path={`/${page.name}`}>
                {React.createElement(page, [])}
              </Route>
            ))}
          </Switch>
        </div>
      </div>
    </Router>
	);
}

export default App;
