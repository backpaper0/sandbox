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
        <Foo contents={contents}>
            {contents.map(x => <Bar key={x.id} content={x} />)}
        </Foo>
    </div>
);

const Foo = ({ children }) => (
    <ul>
        {children}
    </ul>
);

//Barがli要素を含んでいる、つまり繰り返されることを想定しておりポータビリティが低い。
const Bar = ({ content }) => (
    <li>
        <span id={content.id}>{content.text}</span>
    </li>
);

ReactDOM.render(<App contents={cs} />, document.getElementById('root'));
