import { useContext, useEffect, useRef } from 'react';
import CounterContext from './CounterContext';

export function useCounter1(): [number, () => void] {
  const { counter1, countUp1 } = useContext(CounterContext);
  return [counter1, countUp1];
};

export function useCounter2(): [number, () => void] {
  const { counter2, countUp2 } = useContext(CounterContext);
  return [counter2, countUp2];
};

export function useDebugLog(name: string): [React.Ref<any>] {
  const ref = useRef(null);
  useEffect(() => {
    console.log(`    [Init debug log ${name}]`);
    if (ref.current !== null) {
      const observer = new MutationObserver(mutations => {
        mutations.forEach(mutation => {
          switch (mutation.type) {
            case 'attributes':
              break;
            case 'characterData':
              console.log(`  [Update ${name}] ${mutation.type}: ${(mutation.target as Text).data}`);
              break;
            case 'childList':
              break;
          }
        });
      });
      const config = { attributes: true, childList: true, characterData: true, subtree: true };
      observer.observe(ref.current as any, config);
      return () => {
        console.log(`    [destroy debug log ${name}]`);
        observer.disconnect();
      };
    }
  }, [name]);
  return [ref];
};

