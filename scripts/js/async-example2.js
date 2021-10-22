
async function foo() {
  console.log('Begin foo');
  await bar();
  console.log('End foo');
}

async function bar() {
  console.log('Begin bar');
  await new Promise(resolve => {
    setTimeout(() => {
      console.log('Resolve bar');
      resolve();
    }, 1000);
  });
  console.log('End bar');
}

console.log('Begin main');
foo().then(() => {
  console.log('End main');
});
