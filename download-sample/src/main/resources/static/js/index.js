function download(event) {
  let headers = new Headers();
  headers.append(
      document.getElementsByName('_csrf_header')[0].getAttribute('content'),
      document.getElementsByName('_csrf')[0].getAttribute('content'));
  let url = event.target.getAttribute('data-url')
  let req = new Request(url, { method: 'POST', headers, credentials: 'same-origin' })
  fetch(req).then(res => res.text())
    .then(name => {
      let p = document.createElement('p');
      let a = document.createElement('a');
      a.setAttribute('href', location.origin + url + '/' + name);
      a.appendChild(document.createTextNode(a.getAttribute('href')));
      p.appendChild(a);
      document.getElementById('filename').appendChild(p);
      location.href = url + '/' + name
    })
}

document.getElementById('download').addEventListener('click', download, false)

