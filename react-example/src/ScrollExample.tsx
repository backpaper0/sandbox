import React, { useState, useRef, useEffect, useCallback } from 'react';

export default function ScrollExample() {
  const [messages, setMessages] = useState(initialValue);
  const [loading, setLoading] = useState(false);
  const ref = useRef(null);
  useEffect(() => {
    const element = ref.current as any;
    element.scrollTo({
      top: element.lastElementChild.getBoundingClientRect().y
    });
  }, []);
  const tryUpdateMessages = useCallback(() => {
    if (loading) {
      return;
    }
    const element = ref.current as any;
    const rect = element.getBoundingClientRect();
    if (element.scrollTop < rect.height) {
      if (messages.isFirst()) {
        return;
      }
      setLoading(true);
      setTimeout(() => {
        setLoading(false);
        setMessages(messages.prev());
      }, 100);
    }
  }, [loading, messages]);
  useEffect(() => {
    tryUpdateMessages();
  }, [tryUpdateMessages]);
  const handleScroll: React.UIEventHandler = (event) => {
    tryUpdateMessages();
  }
  return (
    <div>
      <h1>Scroll</h1>
      <div ref={ref} style={css} onScroll={handleScroll}>
        {messages.map(a => <p key={a}>{a}</p>)}
      </div>
    </div>
  );
}

class Messages {
  private size: number = 500;
  private begin: number;
  private endExcludes: number;
  private array: string[];
  constructor(begin: number, endExcludes: number) {
    this.begin = begin;
    this.endExcludes = endExcludes;
    this.array = [];
    for (let i = begin, j = 0; i < endExcludes && j < this.size; i++, j++) {
      this.array.push(`Message ${i}`);
    };
  }
  isFirst(): boolean {
    return this.begin <= 1;
  }
  prev(): Messages {
    const begin = Math.max(1, this.begin - 100);
    return new Messages(begin, this.endExcludes);
  }
  map<T>(fn: (a: string) => T): T[] {
    return this.array.map(fn);
  }
}

const css = {
  border: '1px solid silver',
  width: '480px',
  height: '640px',
  overflowY: 'scroll' as 'scroll',
};

const initialValue: Messages = new Messages(401, 501);

