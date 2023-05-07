package main

import (
	"log"
	"net/http"

	"github.com/backpaper0/go-solid-example/api"
	"github.com/backpaper0/go-solid-example/ui"
)

func main() {
	serveMux := http.NewServeMux()

	api.RegisterHandler(serveMux)
	ui.RegisterHandler(serveMux)

	server := &http.Server{
		Addr:    ":8080",
		Handler: &LoggingHandler{serveMux},
	}
	if err := server.ListenAndServe(); err != nil {
		panic(err)
	}
}

type LoggingHandler struct {
	handler http.Handler
}

func (a *LoggingHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	log.Printf("%v %v", r.Method, r.URL)
	a.handler.ServeHTTP(w, r)
}
