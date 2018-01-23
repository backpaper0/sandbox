import React from 'react';
import ReactDOM from 'react-dom';

class Content {
    constructor(id, text) {
        this.id = id;
        this.text = text;
    }
}

const cs = [
    new Content(1, 'foo'),
    new Content(2, 'bar'),
    new Content(3, 'baz')
];

const App = ({ contents }) => (
    <div>
        <Foo contents={contents} />
    </div>
);

//FooがBarに依存している
const Foo = ({ contents }) => (
    <ul>
        {contents.map(x => <Bar key={x.id} content={x} />)}
    </ul>
);

const Bar = ({ content }) => (
    <li>
        <span id={content.id}>{content.text}</span>
    </li>
);

ReactDOM.render(<App contents={cs} />, document.getElementById('root'));
