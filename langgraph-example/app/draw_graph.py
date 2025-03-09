from pathlib import Path
import app.simple


def main() -> None:
    graph_builders = [
        app.simple.build_simple_graph,
    ]
    md_file_path = Path("graph.md")
    with md_file_path.open(mode="w", encoding="utf-8") as file:
        file.write("# Graph\n")
        for graph_builder in graph_builders:
            title = graph_builder.__name__.removeprefix("build_").replace("_", " ")
            file.write(f"\n## {title}\n")

            graph = graph_builder()
            flowchart = graph.get_graph().draw_mermaid()
            file.write("\n```mermaid\n")
            file.write(flowchart)
            file.write("```\n")


if __name__ == "__main__":
    main()
