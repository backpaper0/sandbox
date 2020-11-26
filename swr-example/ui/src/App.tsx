import React, { useRef } from 'react';
import useSWR, { mutate } from 'swr';

type Message = {
  id: string;
  content: string;
}

function App() {
  const contentRef = useRef<HTMLInputElement>(null);
  const { data: messages } = useSWR<Message[]>('/api/messages');
  if (messages === undefined) {
    return (<div>Loading...</div>);
  }
  const handleSubmit: React.FormEventHandler<HTMLFormElement> = event => {
    (async () => {
      event.preventDefault();
      if (! contentRef.current) {
        return;
      }
      const content = contentRef.current.value;
      if (! content) {
        return;
      }
      contentRef.current.value = '';
      contentRef.current.focus();
      const method = 'POST';
      const body = new URLSearchParams();
      body.append('content', content);
      await fetch('/api/messages', { method, body, });
      mutate('/api/messages');
    })();
  };
  return (
    <div>
      <form onSubmit={handleSubmit}>
        <p>
          <input type="text" ref={contentRef} autoFocus/>
          <button>Post</button>
        </p>
      </form>
      <ul>
        {messages.map(a => (<li key={a.id}>{a.content}</li>))}
      </ul>
    </div>
  );
}

export default App;

