package main

import (
	"example/web/files"
	"example/web/hello"
	"net/http"
)

func main() {

	handler := http.NewServeMux()
	hello.RegisterHandlers(handler)
	files.RegisterHandlers(handler)

	server := &http.Server{
		Addr:    ":8080",
		Handler: handler,
	}
	defer server.Close()

	err := server.ListenAndServe()
	if err != nil {
		panic(err)
	}
}
