function setErrorMessage(errorMessage) {
  document.querySelector('#errorMessage').textContent = errorMessage;
}

function clearErrorMessage() {
  setErrorMessage('');
}

window.onerror = function(message, source, lineno, colno, error) {
  setErrorMessage(error.message);
}; 

window.onunhandledrejection = function(event) {
  const { promise, reason } = event;
  setErrorMessage(reason);
};

function npe() {
  clearErrorMessage();
  const a = null;
  const b = a.foobar;
}

function throwError() {
  clearErrorMessage();
  throw Error('Throw error');
}

function promiseReject() {
  clearErrorMessage();
  Promise.reject('Promise reject');
}

function tryCatch() {
  clearErrorMessage();
  try {
    const a = null;
    const b = a.foobar;
  } catch (e) {
  }
}

function promiseRejectCatch() {
  clearErrorMessage();
  Promise.reject('Promise reject').catch(error => {});
}

