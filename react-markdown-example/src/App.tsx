
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

const text = `
- [foo](https://example.com/)
- [bar](https://example.com/foo bar baz)
- [baz](https://example.com/foo%20bar%20baz)
`

function App() {
  return (
    <div>
      <p>スペースを含んでいるとURLエンコードしないとリンクがぶっ壊れるっぽい。</p>
      <ReactMarkdown remarkPlugins={[remarkGfm]}>{text}</ReactMarkdown>
    </div>
  )
}

export default App
