
const fn = async (success, message) => new Promise((resolve, reject) => setTimeout(() => success ? resolve(message) : reject(message), 100));

const run1 = () => {
  fn(true, '1').then(a => console.log(a));
};
run1();

const run2 = async () => {
  const a = await fn(true, '2');
  console.log(a);
};
run2();

const run3 = () => {
  fn(false, '3').catch(a => console.log(a));
};
run3();

const run4 = async () => {
  try {
    await fn(false, '4')
  } catch(e) {
    console.log(e);
  }
};
run4();
