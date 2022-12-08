package main

import (
	"os"
	"text/template"
)


type Data struct {
	Title string
	Name string
	Vars []string
}

func main() {

	s := `# {{.Title}}

Hello, {{.Name}}!

{{range .Vars}}- {{.}}
{{end}}
`

	t := template.Must(template.New("demo").Parse(s))
	data := Data{Title:"TITLE", Name: "world",Vars:[]string{"foo", "bar", "baz"}}
	t.Execute(os.Stdout, data)
}
