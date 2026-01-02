import React, { useRef, useEffect } from "react";

export function useDebugLog(name: string): [React.Ref<any>] {
  console.log(`${name}: render`);
  const ref = useRef(null);
  useEffect(() => {
    console.log(`(${name}: connect)`);
    const observer = new MutationObserver((mutations) => {
      mutations.forEach((mutation) => {
        switch (mutation.type) {
          case "attributes":
            break;
          case "characterData":
            console.log(`(${name}: update DOM)`);
            break;
          case "childList":
            break;
        }
      });
    });
    const config = { attributes: true, childList: true, characterData: true, subtree: true };
    observer.observe(ref.current as any, config);
    return () => {
      console.log(`(${name}: disconnect)`);
      observer.disconnect();
    };
  }, [name]);
  return [ref];
}
