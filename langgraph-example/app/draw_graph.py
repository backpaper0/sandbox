import importlib.util
from pathlib import Path
from typing import Callable, Generator
import pkgutil
import importlib
import inspect
from langgraph.graph.state import CompiledStateGraph


func_prefix = "build_"
func_suffix = "_graph"


def is_target_func(func_name: str) -> bool:
    return (
        len(func_name) > len(func_prefix) + len(func_suffix)
        and func_name.startswith(func_prefix)
        and func_name.endswith(func_suffix)
    )


def find_graph_builders() -> Generator[Callable[[], CompiledStateGraph], None, None]:
    root = "app"
    root_module = importlib.import_module(root)
    for _, name, _ in pkgutil.iter_modules(root_module.__path__):
        module_name = f"{root}.{name}"
        module = importlib.import_module(module_name)
        for func_name, _ in inspect.getmembers(module, inspect.isfunction):
            if is_target_func(func_name):
                func = getattr(module, func_name)
                yield func


def main() -> None:
    graph_builders = [func for func in find_graph_builders()]
    md_file_path = Path("graph.md")
    with md_file_path.open(mode="w", encoding="utf-8") as file:
        file.write("# Graph\n")
        for graph_builder in graph_builders:
            title = (
                graph_builder.__name__.removeprefix(func_prefix)
                .removesuffix(func_suffix)
                .replace("_", " ")
            )
            file.write(f"\n## {title}\n")

            graph = graph_builder()
            flowchart = graph.get_graph().draw_mermaid()
            file.write("\n```mermaid\n")
            file.write(flowchart)
            file.write("```\n")


if __name__ == "__main__":
    main()
