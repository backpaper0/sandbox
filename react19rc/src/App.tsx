import React, { useActionState, useOptimistic, useState } from 'react';
import { useFormStatus } from 'react-dom';

interface Message {
  id: string;
  content: string;
}

const genId: () => string = () => Math.floor((Math.random() * 0xFFFFFFFF)).toString(16);

const App: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([{
    id: genId(),
    content: "hello world",
  }])
  const [optimisticMessages, addOptimistic] = useOptimistic(
    messages,
    (currentMessages, optimisticMessage) => {
      return [{
        id: "...",
        content: optimisticMessage
      }, ...currentMessages];
    });
  const action = async (previousState, formData) => {
    const content = formData.get("content");
    addOptimistic(content);
    const message = await sendMessage(content);
    setMessages(messages => [message, ...messages]);
  }
  const [, formAction] = useActionState(action)

  return (
    <div>
      <form action={formAction}>
        <MessageForm />
      </form>
      <ul>
        {optimisticMessages.map(message => (
          <li key={message.id}>
            {message.id}: {message.content}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default App

const MessageForm: React.FC = () => {
  const status = useFormStatus();
  return (
    <div>
      <input type="text" name="content" disabled={status.pending} />
      <button type="submit" disabled={status.pending}>Submit</button>
    </div>
  );
}

const sendMessage: (content: string) => Promise<Message> = (content) => {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({
        id: genId(),
        content,
      });
    }, 1000);
  });
}