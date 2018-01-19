/*
   Copyright 2018 David Laurell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.daverix.topologicalsorter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>This class sorts nodes in an acyclic directed graph using a depth first algorithm which can be
 * found at <a href="https://en.wikipedia.org/wiki/Topological_sorting">Wikipedia</a></p>
 */
public final class TopologicalSorter {
    /**
     * Returns a sorted list of nodes from a directed acyclic graph
     *
     * @param graph the directed acyclc graph to sort nodes from
     * @param <T>   the type of the node
     * @return a sorted list of nodes
     */
    public static <T> List<T> sort(Graph<T> graph) {
        if (graph == null)
            throw new IllegalArgumentException("graph is null");

        return new Sorter<>(graph).sort();
    }

    private static class Sorter<T> {
        private final Graph<T> graph;

        private final List<T> unmarked;
        private final List<T> sorted;
        private final List<T> temporaryMarked;


        private Sorter(Graph<T> graph) {
            this.graph = graph;
            this.unmarked = new ArrayList<>();
            for (T node : graph) {
                unmarked.add(node);
            }

            this.sorted = new ArrayList<>(unmarked.size());
            this.temporaryMarked = new ArrayList<>();
        }

        List<T> sort() {
            while (!unmarked.isEmpty()) {
                visit(unmarked.get(0));
            }

            return sorted;
        }

        private void visit(T node) {
            if (sorted.contains(node))
                return;

            if (temporaryMarked.contains(node))
                throw new IllegalStateException("cyclic dependency detected, " + node + " already visited");

            temporaryMarked.add(node);

            Collection<T> edgeNodes = graph.getEdges(node);
            if (edgeNodes == null)
                throw new IllegalStateException("edges from node " + node + " is null in the provided graph");

            for (T edgeNode : edgeNodes) {
                visit(edgeNode);
            }

            unmarked.remove(node);
            temporaryMarked.remove(node);
            sorted.add(node);
        }
    }

    private TopologicalSorter() {
    }
}
