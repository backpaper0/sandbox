import React from 'react';
import ReactDOM from 'react-dom';

class Page1 extends React.Component {
    render() {
        return <h1>Page 1</h1>;
    }
    componentDidMount() {
        console.log('Mounted 1');
    }
}

class Lifecycle extends React.Component {
    render() {
        return <span />;
    }
    componentDidMount() {
        console.log(this.props.message);
    }
}

const Page2 = () => (
    <div>
        <h1>Page 2</h1>
        <Lifecycle message="Mounted 2" />
    </div>
);

let listeners = [];

const changePage = (event, page) => {
    event.preventDefault();
    window.location = '#' + page;
    listeners.forEach(listener => listener(page));
    return false;
};

class Route extends React.Component {
    state = { page: window.location.hash.substring(1) };
    constructor({ match, component }) {
        super();
        this.match = match;
        this.component = component;
        listeners.push(page => this.setState({ page }));
    }
    render() {
        const Page = this.component;
        return (this.state.page === this.match) ? <Page /> : null;
    }
}

const App = () => (
    <div>
        <ul>
            <li><a href="/1" onClick={event => changePage(event, "/1")}>Page 1</a></li>
            <li><a href="/2" onClick={event => changePage(event, "/2")}>Page 2</a></li>
        </ul>
        <div>
            <Route match="/1" component={Page1} />
            <Route match="/2" component={Page2} />
        </div>
    </div >
);


ReactDOM.render(<App />, document.getElementById('root'));
