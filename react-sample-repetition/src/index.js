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
        {children.map(x => (
            //子に渡されるpropsの詳細を知った上でkeyを設定しており依存を断ち切れていない
            <li key={x.props.content.id}>
                {x}
            </li>
        ))}
    </ul>
);

const Bar = ({ content }) => (
    <span id={content.id}>{content.text}</span>
);

ReactDOM.render(<App contents={cs} />, document.getElementById('root'));
