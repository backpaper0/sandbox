package files

import (
	"embed"
	"net/http"
)

//go:embed public
var public embed.FS

func RegisterHandlers(serveMux *http.ServeMux) {
	handler := http.FileServer(http.FS(public))
	serveMux.Handle("/public/", handler)
}
