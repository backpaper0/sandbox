
function fn1() {
  return new Promise(resolve => resolve('hello1'));
}

async function fn2() {
  return 'hello2';
}

async function fn3() {
  return new Promise(resolve => resolve('hello3'));
}

async function fn4() {
  return await 'hello4';
}

function fn5() {
  return new Promise(resolve => setTimeout(() => resolve('hello5'), 0));
}

const ps = [
  fn1(),
  fn2(),
  fn3(),
  fn4(),
  fn5(),
];

ps.forEach(p => {
  console.log(p);
});

(async () => {
  for (const p of ps) {
    console.log(await p);
  }
})();
