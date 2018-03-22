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

let CurrentPage = Page1;

const Routing = () => {
    return <CurrentPage />
};

const changePage = Page => {
    CurrentPage = Page;
    ReactDOM.render(<App />, document.getElementById('root'));
};

const App = () => (
    <div>
        <ul>
            <li><a href="#" onClick={() => changePage(Page1)}>Page 1</a></li>
            <li><a href="#" onClick={() => changePage(Page2)}>Page 2</a></li>
        </ul>
        <Routing />
    </div>
);


ReactDOM.render(<App />, document.getElementById('root'));
