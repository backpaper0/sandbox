
const obj = {
  then: function(resolve) {
    resolve("hello");
  }
};

const obj2 = {
  then: function(resolve, reject) {
    reject("error");
  }
};


(async () => {


  const ret = await obj;
  console.log(ret);


  try {
    await obj2;
  } catch (e) {
    console.log(e);
  }
})();

