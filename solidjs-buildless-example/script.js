import {
    createSignal,
    onCleanup,
} from "https://cdn.skypack.dev/solid-js";
import h from "https://cdn.skypack.dev/solid-js/h";
import { render } from "https://cdn.skypack.dev/solid-js/web";

const App = () => {

    const [count, setCount] = createSignal(0);
    const timer = setInterval(() => setCount(count() + 1), 1000);

    onCleanup(() => clearInterval(timer));

    return h("div", {}, () => count());
};

render(App, document.body);
