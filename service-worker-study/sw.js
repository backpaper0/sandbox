console.log('Read sw.js');

const cacheName = 'v1';
const urlsToCache = [
  '/hello.txt'
];

self.addEventListener('install', event => {
  console.log('Handle install event');
  event.waitUntil(
    caches
      .open(cacheName)
      .then(cache => {
        console.log(`Open cache: ${cacheName}`);
        return cache.addAll(urlsToCache);
      })
      .then(() => console.log('Added cache successful'))
      .catch(err => console.log(err))
  );
});

self.addEventListener('fetch', event => {
  console.log('Handle fetch');
  event.respondWith(
    caches
      .match(event.request)
      .then(response => {
        if (response) {
          console.log(`Response from cache: ${event.request.url}`)
          return response;
        }
        console.log(`Do request: ${event.request.url}`);
        return fetch(event.request)
      }));
});

