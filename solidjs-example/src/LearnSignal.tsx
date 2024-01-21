import { Component, createSignal } from "solid-js";

export const LearnSignal: Component = () => {
  const [count, setCount] = createSignal(0);
  const countUp = () => setCount(count() + 1);
  return (
    <div>
      <p><button onClick={() => countUp()}>Count up</button></p>

      <p>1) {count()}</p>
      <p>2) <View1 count={count()} /></p>
      <p>3) <View2 count={count()} /></p>

      <p>3はコンポーネントの引数で props を分割代入しているため、リアクティビティを損なっており count の値を変更しても表示が変わらない。</p>
      <figure>
        <blockquote>
          <p>他のフレームワークとは異なり、コンポーネントの props に対してオブジェクトの分割代入を使用することはできません。舞台裏では、props オブジェクトが Object のゲッターに依存して、値を遅延取得するためです。分割代入を使用すると、props のリアクティビティが損なわれます。</p>
        </blockquote>
        <figcaption>
          <cite>
            <a href="https://www.solidjs.com/guides/rendering#props">
              Solid公式ガイド：Props
            </a>
          </cite>
        </figcaption>
      </figure>
    </div>
  );
};

const View1: Component<{ count: number }> = (props) => {
  return (
    <span>{props.count}</span>
  );
};

const View2: Component<{ count: number }> = ({ count }) => {
  return (
    <span>{count}</span>
  );
};
