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

class Page2 extends React.Component {
    render() {
        return <h1>Page 2</h1>;
    }
    componentDidMount() {
        console.log('Mounted 2');
    }
}

let listeners = [];

const changePage = x => {
    listeners.forEach(listener => {
        listener(x);
    });
};

class Route extends React.Component {
    state = { page: 1 };
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
            <li><a href="#" onClick={() => changePage(1)}>Page 1</a></li>
            <li><a href="#" onClick={() => changePage(2)}>Page 2</a></li>
        </ul>
        <div>
            <Route match={1} component={Page1} />
            <Route match={2} component={Page2} />
        </div>
    </div >
);


ReactDOM.render(<App />, document.getElementById('root'));
