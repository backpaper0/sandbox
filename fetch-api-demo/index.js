function httpMethods() {
  const methods = ['get', 'head', 'post', 'put', 'delete', 'trace', 'options', 'connect', 'patch'];
  methods.forEach(method => {
    const options = {
      method: method.toUpperCase()
    };
    const label = `[${method}]`;
    fetch(`/methods/${method}`, options)
      .then(a => a.text())
      .then(a => console.log(label, a))
      .catch(e => console.log(label, e.message));
  });
}

