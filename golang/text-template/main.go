package main

import (
	"os"
	"text/template"
)


type Data struct {
	Title string
	Name string
}

func main() {

	s := `# {{.Title}}

Hello, {{.Name}}!`

	t := template.Must(template.New("demo").Parse(s))
	data := Data{Title:"TITLE", Name: "world"}
	t.Execute(os.Stdout, data)
}
