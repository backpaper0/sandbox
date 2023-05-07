import {
  Component,
  createSignal,
} from 'solid-js';

const App: Component = () => {
  const [name, setName] = createSignal('World');
  const [message, setMessage] = createSignal('...');

  const getMessage = async (e: SubmitEvent) => {
    e.preventDefault();
    const body = new URLSearchParams();
    body.append('name', name());
    const resp = await fetch('/api/greeting', {
      method: 'POST',
      body,
    });
    const { message } = await resp.json();
    setMessage(message);
    setName('');
  };
  return (
    <div>
      <form onSubmit={(e) => getMessage(e)}>
        <p>
          <input type="text" value={name()} onInput={e => setName(e.currentTarget.value)} autofocus />
          <button>送信</button>
        </p>
      </form>
      <p>{message()}</p>
    </div>
  );
};

export default App;
