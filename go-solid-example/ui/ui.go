package ui

import (
	"embed"
	"io/fs"
	"net/http"
)

//go:embed dist
var dist embed.FS

func RegisterHandler(serveMux *http.ServeMux) error {
	fs, err := fs.Sub(dist, "dist")
	if err != nil {
		return err
	}
	handler := http.FileServer(http.FS(fs))
	serveMux.Handle("/", handler)
	return nil
}
