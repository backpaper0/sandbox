import React, { useState, useRef, useEffect, useCallback } from 'react';

export default function ScrollExample() {
  const delay = 100;
  const [messages, setMessages] = useState(initialValue);
  const [loading, setLoading] = useState(false);
  const ref = useRef(null);
  useEffect(() => {
    setLoading(a => {
      if (a === false) {
        return a;
      }
      const element = (ref.current as any);
      const rect = element.getBoundingClientRect();
      if (element.scrollTop >= rect.height) {
        return false;
      }
      element.scrollTo({ top: rect.height + 1 });
      setTimeout(() => setLoading(false));
      return a;
    });
  }, [messages]);
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
        setMessages(messages.prev());
      }, delay);
    } else if (element.lastElementChild.getBoundingClientRect().y < (rect.height * 2)) {
      if (messages.isLast()) {
        return;
      }
      setLoading(true);
      setTimeout(() => {
        setMessages(messages.next());
      }, delay);
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
      <div className="scrollable" ref={ref} style={css} onScroll={handleScroll}>
        {messages.map(a => <p key={a}>{a}</p>)}
      </div>
    </div>
  );
}

class Messages {
  private static fetchSize = 100;
  private static size = 300;
  private static minBegin = 1;
  private static maxEndExcluded = 10001;
  private begin: number;
  private endExcludes: number;
  private array: string[];
  constructor(begin: number, endExcludes: number) {
    this.begin = begin;
    this.endExcludes = endExcludes;
    this.array = [];
    for (let i = begin; i < endExcludes; i++) {
      this.array.push(`Message ${i}`);
    };
  }
  isFirst(): boolean {
    return this.begin <= Messages.minBegin;
  }
  isLast(): boolean {
    return this.endExcludes >= Messages.maxEndExcluded;
  }
  prev(): Messages {
    const begin = Math.max(Messages.minBegin, this.begin - Messages.fetchSize);
    const endExcludes = Math.min(Messages.maxEndExcluded, begin + Messages.size);
    return new Messages(begin, endExcludes);
  }
  next(): Messages {
    const begin = Math.min(Messages.maxEndExcluded - Messages.fetchSize, this.begin + Messages.fetchSize);
    const endExcludes = Math.min(Messages.maxEndExcluded, begin + Messages.size);
    return new Messages(begin, endExcludes);
  }
  map<T>(fn: (a: string) => T): T[] {
    return this.array.map(fn);
  }
  static initialValue() {
    const begin = Messages.maxEndExcluded - Messages.fetchSize;
    const endExcludes = Messages.maxEndExcluded;
    return new Messages(begin, endExcludes);
  }
}

const css = {
  border: '1px solid silver',
  width: '480px',
  height: '640px',
  overflowY: 'scroll' as 'scroll',
};

const initialValue: Messages = Messages.initialValue();

