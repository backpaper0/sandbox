import React from 'react';
import styled from 'styled-components';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import Boutaoshi from './Boutaoshi';

const Container = styled.div`
  display: flex;
`;

const Menu = styled.ul`
  flex-basis: 10%;
`;

const App: React.FC = () => {
  return (
    <Router>
      <Container>
        <Menu>
          <li><Link to="/">Home</Link></li>
          <li><Link to="/boutaoshi">棒倒し法</Link></li>
        </Menu>
        <div>
          <Switch>
            <Route path="/boutaoshi">
              <Boutaoshi/>
            </Route>
          </Switch>
        </div>
      </Container>
    </Router>
  );
}

export default App;

