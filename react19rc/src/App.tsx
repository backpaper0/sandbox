import React, { useActionState, useEffect, useOptimistic, useRef, useState } from 'react';
import { useFormStatus } from 'react-dom';

interface Message {
  id: string;
  content: string;
}

const genId: () => string = () => Math.floor((Math.random() * 0xFFFFFFFF)).toString(16);

const App: React.FC = () => {
  const formRef = useRef<HTMLFormElement>();
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
    formRef.current?.reset()
    try {
      const message = await sendMessage(content);
      setMessages(messages => [message, ...messages]);
      return "";
    } catch (e) {
      return e.message;
    }
  }
  const initialState = "";
  const [actionState, formAction] = useActionState(action, initialState)

  return (
    <div>
      <p>{actionState}</p>
      <form action={formAction} ref={formRef}>
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
  const inputRef = useRef<HTMLInputElement>();
  useEffect(() => {
    if (status.pending === false) {
      inputRef.current?.focus();
    }
  }, [status.pending])
  return (
    <div>
      <input type="text" name="content" disabled={status.pending} ref={inputRef} />
      <button type="submit" disabled={status.pending}>Submit</button>
    </div>
  );
}

const sendMessage: (content: string) => Promise<Message> = (content) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (content.includes("err")) {
        reject(new Error("エラーが発生しました"))
      } else {
        resolve({
          id: genId(),
          content,
        });
      }
    }, 1000);
  });
}