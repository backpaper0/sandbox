package algorithm.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepthFirstSearchGraph {

	public static void main(final String[] args) {

		var ns = new Node<>("s");
		var n1 = new Node<>("1");
		var n2 = new Node<>("2");
		var n3 = new Node<>("3");
		var n4 = new Node<>("4");
		var n5 = new Node<>("5");
		var n6 = new Node<>("6");
		var n7 = new Node<>("7");
		var n8 = new Node<>("8");
		var n9 = new Node<>("9");
		var n10 = new Node<>("10");
		var n11 = new Node<>("11");
		var n12 = new Node<>("12");
		var n13 = new Node<>("13");
		var n14 = new Node<>("14");
		var nt = new Node<>("t");

		ns.addNeighbors(n1, n6, n8);
		n1.addNeighbors(ns, n2, n3);
		n2.addNeighbors(n1, n10, n11);
		n3.addNeighbors(n1, n4, n12);
		n4.addNeighbors(n3, n5, n13);
		n5.addNeighbors(n4, n6, n9);
		n6.addNeighbors(ns, n5, n7);
		n7.addNeighbors(n6, n8, n9);
		n8.addNeighbors(ns, n7, n14);
		n9.addNeighbors(n5, n7, nt);
		n10.addNeighbors(n2);
		n11.addNeighbors(n2);
		n12.addNeighbors(n3);
		n13.addNeighbors(n4);
		n14.addNeighbors(n8);
		nt.addNeighbors(n9);

		var g = new Graph<String>();
		g.add(ns);
		g.add(n1);
		g.add(n2);
		g.add(n3);
		g.add(n4);
		g.add(n5);
		g.add(n6);
		g.add(n7);
		g.add(n8);
		g.add(n9);
		g.add(n10);
		g.add(n11);
		g.add(n12);
		g.add(n13);
		g.add(n14);
		g.add(nt);

		g.search();

		System.out.println(g);
	}

	static class Graph<T> {
		final List<Node<T>> nodes = new ArrayList<>();
		int counter;

		void add(final Node<T> node) {
			nodes.add(node);
		}

		void search() {
			visit(nodes.get(0));
			for (final Node<T> node : nodes) {
				if (node.color == Color.WHITE) {
					visit(node);
				}
			}
		}

		void visit(final Node<T> node) {
			node.color = Color.GRAY;
			node.discovered = ++counter;
			for (final Node<T> neighbor : node.neighbors) {
				if (neighbor.color == Color.WHITE) {
					neighbor.pred = node;
					visit(neighbor);
				}
			}
			node.color = Color.BLACK;
			node.finished = ++counter;
		}

		@Override
		public String toString() {
			var s1 = Stream.<String> builder()
					.add("node pred discovered finished")
					.add("---- ---- ---------- --------")
					.build();
			var s2 = nodes
					.stream()
					.map(Objects::toString);
			return Stream.concat(s1, s2).collect(Collectors.joining(System.lineSeparator()));
		}
	}

	static class Node<T> {
		final T value;
		final Set<Node<T>> neighbors = new HashSet<>();
		Color color = Color.WHITE;
		Node<T> pred;
		int discovered;
		int finished;

		public Node(final T value) {
			this.value = value;
		}

		@SafeVarargs
		final void addNeighbors(final Node<T>... neighbors) {
			this.neighbors.addAll(Arrays.asList(neighbors));
		}

		@Override
		public String toString() {
			return String.format("%4s %4s %10s %8s", value, pred != null ? pred.value : "",
					discovered, finished);
		}
	}

	enum Color {
		WHITE, GRAY, BLACK
	}
}
