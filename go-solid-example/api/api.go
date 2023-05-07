package api

import (
	"encoding/json"
	"fmt"
	"net/http"
)

func RegisterHandler(serveMux *http.ServeMux) {
	serveMux.HandleFunc("/api/greeting", greeting)
}

func greeting(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	name := r.PostForm.Get("name")
	greeting := &Greeting{
		Message: fmt.Sprintf("Hello %v", name),
	}
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	w.WriteHeader(http.StatusOK)
	e := json.NewEncoder(w)
	e.Encode(greeting)
}

type Greeting struct {
	Message string `json:"message"`
}
