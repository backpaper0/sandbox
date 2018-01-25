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
            {contents.map(x => <Bar key={x.id} relayedKey={x.id} content={x} />)}
        </Foo>
    </div>
);

const Foo = ({ children }) => (
    <ul>
        {children.map(x => (
            //relayedKeyという名前で外側のコンポーネントからkeyになり得る値を引き渡すようにした
            <li key={x.props.relayedKey}>
                {x}
            </li>
        ))}
    </ul>
);

const Bar = ({ content }) => (
    <span id={content.id}>{content.text}</span>
);

ReactDOM.render(<App contents={cs} />, document.getElementById('root'));
