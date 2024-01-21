/* @refresh reload */
import { Route, Router } from '@solidjs/router';
import { Component } from 'solid-js';
import { render } from 'solid-js/web';
import App from './App';
import { LearnSignal } from './LearnSignal';

const root = document.getElementById('root');

if (import.meta.env.DEV && !(root instanceof HTMLElement)) {
  throw new Error(
    'Root element not found. Did you forget to add it to your index.html? Or maybe the id attribute got mispelled?',
  );
}

const Home: Component = () => {
  return (
    <ul>
      <li><a href="/app">App</a></li>
      <li><a href="/learn-signal">LearnSignal</a></li>
    </ul>
  );
}

render(() => (
  <Router>
    <Route path="/app" component={App} />
    <Route path="/learn-signal" component={LearnSignal} />
    <Route path="/" component={Home} />
  </Router>
), root!);
