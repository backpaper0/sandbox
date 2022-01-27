
function fn([name, sleep]) {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve(name);
    }, sleep);
  });
}

const xs = [
  ["foo", 400],
  ["bar", 300],
  ["baz", 200],
  ["qux", 100],
];

// 非同期のジェネレーター
async function* gen() {
  for (const x of xs) {
    yield fn(x);
  }
}

async function run() {
  // 順番にsleepしつつ出力される
  console.log("[1]");
  for (const x of xs) {
    console.log(await fn(x));
  }
  console.log("");

  // 一斉にsleepして一気に出力される
  console.log("[2]");
  for (const x of xs.map(fn)) {
    console.log(await x);
  }
  console.log("");

  // 一斉にsleepして一気に出力される
  console.log("[3]");
  for await (const x of xs.map(fn)) {
    console.log(x);
  }
  console.log("");

  // 順番にsleepしつつ出力される
  console.log("[4]");
  for await (const x of gen()) {
    console.log(x);
  }
  console.log("");

  // forEach全体はawaitしない
  console.log("[5]");
  xs.forEach(async x => console.log(await fn(x)));
  console.log("");

  // forEach全体はawaitしない
  console.log("[6]");
  xs.map(fn).forEach(async x => console.log(await x));
  console.log("");
}

run();

