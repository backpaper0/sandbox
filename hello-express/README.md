# hello express

Build container image.

```sh
pack build urgm/hello-express:0.1 --builder cloudfoundry/cnb:bionic
```

Run container.

```sh
docker run -p 3000:3000 urgm/hello-express:0.1
```

```sh
curl localhost:3000
curl localhost:3000/users/123/books/456
```
