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

let controller;
function sendAbortExample() {
  controller = new AbortController();
  const signal = controller.signal;
  fetch('/abort', { signal })
    .then(a => a.json())
    .then(a => console.log('[abort]', a))
    .catch(e => console.log('[abort]', e.message));
}

function abortAbortExample() {
  if (controller) {
    controller.abort();
  }
}

(() => {
  const container = document.querySelector('#whatstatuscodeisrejected');
  [200, 302, 400, 401, 403, 404, 500, 503].forEach(status => {
    const item = document.createElement('li');
    container.appendChild(item);
    fetch(`/status/${status}`).then(resp => {
      if (resp.ok) {
        return `OK - ${resp.status} ${resp.statusText}`;
      }
      return `Not OK - ${resp.status} ${resp.statusText}`;
    }).catch(error => {
      return `${error.message}`;
    }).then(text => {
      item.textContent = `${status}: ${text}`;
    });
  });
})();

