(() => {
  const container = document.querySelector('#httpMethods');
  ['GET', 'HEAD', 'POST', 'PUT', 'DELETE', 'TRACE', 'OPTIONS', 'CONNECT', 'PATCH'].forEach(async (method) => {
    const item = document.createElement('li');
    container.appendChild(item);
    let text;
    let result;
    try {
      const resp = await fetch(`/methods`, { method });
      text = await resp.text();
      result = "OK";
    } catch (e) {
      text = e.message;
      result = "NG";
    }
    item.textContent = `${method}: ${result} - ${text}`;
  });
})();

(async () => {
  const container = document.querySelector('#abort');
  const item = document.createElement('li');
  container.appendChild(item);
  const controller = new AbortController();
  const signal = controller.signal;
  try {
    promise = fetch('/abort', { signal })
    controller.abort();
    await promise;
  } catch (e) {
    item.textContent = e.message;
  }
})();

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
      return `Error - ${error.message}`;
    }).then(text => {
      item.textContent = `${status}: ${text}`;
    });
  });
})();

