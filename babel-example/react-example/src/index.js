const React = require('react');
const ReactDOMServer = require('react-dom/server');
const TestRenderer = require('react-test-renderer');

const App = ({ name }) => (
  <h1>Hello, {name}!</h1>
);

const html = ReactDOMServer.renderToString(<App name="world"/>);
console.log(html);
