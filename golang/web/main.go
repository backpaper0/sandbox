package main

import (
	_ "embed"
	"html/template"
	"net/http"
)

//go:embed hello.html
var helloHtml string

func main() {
	t, err := template.New("hello").Parse(helloHtml)
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
