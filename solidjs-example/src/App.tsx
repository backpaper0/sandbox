import {
  Component,
  ResourceReturn,
  createEffect,
  createMemo,
  createResource,
  createSignal,
  onCleanup
} from 'solid-js';

const App: Component = () => {
  console.log("component")

  const [count1, setCount1] = createSignal(0)
  const [count2, setCount2] = createSignal(0)
  const sum = createMemo(() => {
    console.log("memo", count1(), count2())
    return count1() + count2()
  })

  // ResourceReturnの型を明記しないとResourceReturn<number, unknown>になってしまう。
  // 型推論してほしい。
  const [data, { mutate, refetch }]: ResourceReturn<number, number> = createResource(0, k => {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve(k + 1)
      }, 3000)
    })
  })

  createEffect(() => {
    console.log("effect", count1(), count2(), sum())
    document.title = `${count1()} + ${count2()} = ${sum()}`
    onCleanup(() => {
      console.log('cleanup', count1(), count2(), sum())
    })
  })

  return (
    <div>
      <h1>{count1()} + {count2()} = {sum()}</h1>
      <p><button onClick={() => setCount1(count1() + 1)}>Count up</button></p>
      <p><button onClick={() => setCount2(prev => prev + 1)}>Count up</button></p>
      <hr />
      <p>{data.loading ? "Loading..." : `${data()}`}</p>
      <p><button onClick={() => refetch()}>Refetch</button></p>
      <p><button onClick={() => mutate(k => k === undefined ? 0 : k + 1)}>Mutate</button></p>
    </div>
  );
};

export default App;
