/*
関数fは次のコードと等価。 

function f() {
  let variable = 1
  return g(() => {
    variable = 2;
  }).then(a => variable);
}

関数gの完了を待つが、その前にvariableをキャプチャ済みなので2ではなく1が返される。
*/

async function f() {
  let variable = 1;
  await g(() => {
    variable = 2;
  });
  return variable;
}

function g() {
  return new Promise(resolve => {
    setTimeout(resolve, 100);
  });
}

f().then(a => console.log(a));
