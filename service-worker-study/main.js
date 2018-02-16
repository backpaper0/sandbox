navigator
    .serviceWorker
    .register('./sw.js')
    .then(registration => console.table(registration))
    .catch(err => console.log(err));

fetch('/hello.txt')
    .then(response => {
        console.log('Hendle response');
        return response.text();
    })
    .then(text => console.log(text));
