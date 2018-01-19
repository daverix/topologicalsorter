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
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableCollection;

/**
 * <p>This class sorts nodes in an acyclic directed graph using a depth first algorithm which can be
 * found at <a href="https://en.wikipedia.org/wiki/Topological_sorting">Wikipedia</a></p>
 */
public final class TopologicalSorter {
    /**
     * Returns a sorted list of nodes from a directed acyclic graph by using an edges factory to
     * get the edges for each node.
     *
     * @param nodes        the list of nodes to sort.
     * @param edgesFactory the factory for getting nodes that are edges on another node
     * @param <T>          the type of the node
     * @return a sorted list of nodes
     */
    public static <T> List<T> sort(Collection<T> nodes, EdgesFactory<T> edgesFactory) {
        if (nodes == null)
            throw new IllegalArgumentException("nodes is null");

        if (edgesFactory == null)
            throw new IllegalArgumentException("edgesFactory is null");

        Sorter<T> sorter = new Sorter<>(nodes, edgesFactory);
        return sorter.sort();
    }

    /**
     * Returns a sorted list of nodes from a directed acyclic graph by using a map of an edges
     *
     * @param nodes the list of nodes to sort.
     * @param edges the edges for each node in the list of nodes
     * @param <T>   the type of the node
     * @return a sorted list of nodes
     */
    public static <T> List<T> sort(Collection<T> nodes, Map<T, Set<T>> edges) {
        if (nodes == null)
            throw new IllegalArgumentException("nodes is null");

        if (edges == null)
            throw new IllegalArgumentException("edges is null");

        return sort(nodes, (node, allNodes) -> edges.getOrDefault(node, emptySet()));
    }

    /**
     * Returns a sorted list of nodes from a directed acyclic graph by using a map of nodes where
     * the key is the node and the value is a list of edges.
     *
     * @param nodes a map of nodes where key is the node and value is the edges. Use an empty set
     *              if the node does not have any edges.
     * @param <T>   the type of the node
     * @return a sorted list of nodes
     */
    public static <T> List<T> sort(Map<T, Set<T>> nodes) {
        if (nodes == null)
            throw new IllegalArgumentException("nodes is null");

        return sort(nodes.keySet(), (node, allNodes) -> nodes.getOrDefault(node, emptySet()));
    }

    private static class Sorter<T> {
        private final Collection<T> allNodes;
        private final List<T> sorted;
        private final List<T> unmarked;
        private final List<T> temporaryMarked;

        private final EdgesFactory<T> edgesFactory;

        private Sorter(Collection<T> nodes, EdgesFactory<T> edgesFactory) {
            this.allNodes = unmodifiableCollection(nodes);
            this.sorted = new ArrayList<>(nodes.size());
            this.unmarked = new ArrayList<>(nodes);
            this.temporaryMarked = new ArrayList<>();

            this.edgesFactory = edgesFactory;
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

            Collection<T> edgeNodes = edgesFactory.getEdges(node, allNodes);
            if (edgeNodes == null)
                throw new IllegalStateException("provided edgeFactory returned null");

            for (T edgeNode : edgeNodes) {
                visit(edgeNode);
            }

            unmarked.remove(node);
            temporaryMarked.remove(node);
            sorted.add(node);
        }
    }

    public interface EdgesFactory<T> {
        /**
         * Returns nodes that are edges on the specified node.
         *
         * @param node     the node to get edges from
         * @param allNodes the nodes to use to get the edges
         * @return a list of nodes that are edges to the specified node
         */
        Collection<T> getEdges(T node, Collection<T> allNodes);
    }

    private TopologicalSorter() {
    }
}
