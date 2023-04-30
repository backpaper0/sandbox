package hello

import (
	_ "embed"
	"html/template"
	"net/http"
)

//go:embed hello.html
var helloHtml string
var tmpl *template.Template

func init() {
	tmpl = template.Must(template.New("hello").Parse(helloHtml))
}

func RegisterHandlers(serveMux *http.ServeMux) {
	serveMux.HandleFunc("/hello", func(w http.ResponseWriter, r *http.Request) {
		tmpl.Execute(w, nil)
	})
}
