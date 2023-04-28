package main

import (
	"html/template"
	"net/http"
)

func main() {
	t, err := template.New("hello").Parse(`
	<!DOCTYPE html>
	<html>
	<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Hello World</title>
	</head>
	<body>
	<h1>Hello World</h1>
	</body>
	</html>
	`)

	if err != nil {
		panic(err)
	}

	handler := http.NewServeMux()
	handler.HandleFunc("/hello", func(w http.ResponseWriter, r *http.Request) {
		t.Execute(w, nil)
	})

	server := &http.Server{
		Addr:    "0.0.0.0:8080",
		Handler: handler,
	}
	defer server.Close()

	err = server.ListenAndServe()
	if err != nil {
		panic(err)
	}
}
