import React, { useState, useEffect } from 'react';
import styles from './App.module.css';

const App = () => {
  const [error, setError] = useState('');

  useEffect(() => {

    window.addEventListener('error', (event: ErrorEvent) => {
      setError(event.message);
      return false; //デフォルトのハンドリングを行わない。event.preventDefaultみたいなもの
    });

    window.addEventListener('unhandledrejection', (event: PromiseRejectionEvent) => {
      const { reason } = event;
      setError(reason.toString());
      return false; //デフォルトのハンドリングを行わない。event.preventDefaultみたいなもの
    });

  }, []);

  if (error) {
    return (
      <div>
        <h1>{error}</h1>
        <p><a href="/">home</a></p>
      </div>
    );
  }

  const throwError = () => {
    throw Error('Throw error');
  };

  const promiseReject = () => {
    Promise.reject('Promise reject');
  };

  return (
    <div>
      <p><button className={styles.button} onClick={throwError}>Throw error</button></p>
      <p><button className={styles.button} onClick={promiseReject}>Promise reject</button></p>
    </div>
  );
};

export default App;
