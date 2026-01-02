import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";

import Page1 from "./UseStateTypeInfer";
import Page2 from "./Props";
import Page3 from "./EventHandlerExample";
import Page4 from "./RenderingTimingExample";
import Page5 from "./RenderingTimingWithFunctionPropsExample";
import Page6 from "./SplashExample";
import Page7 from "./StateCapturedExample";
import Page8 from "./FormExample";
import Page9 from "./BucketRelayJs";
import Page10 from "./BucketRelay";
import Page11 from "./Misc";
import Page12 from "./context/ContextExample";
import Page13 from "./memo/MemoExample1";
import Page14 from "./memo/MemoExample2";
import Page15 from "./ScrollExample";
import Page16 from "./Initialize";
import Page17 from "./SurrogatePair";
import Page18 from "./GenericChild";
import Page19 from "./ForceUpdate";

const pages = [
  Page1,
  Page2,
  Page3,
  Page4,
  Page5,
  Page6,
  Page7,
  Page8,
  Page9,
  Page10,
  Page11,
  Page12,
  Page13,
  Page14,
  Page15,
  Page16,
  Page17,
  Page18,
  Page19,
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
};

export default App;
