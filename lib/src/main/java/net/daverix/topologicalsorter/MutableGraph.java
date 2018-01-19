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


import java.util.Collection;

/**
 * A @{link {@link Graph} that can be mutated with new values}
 *
 * @param <T> the type of node in the graph
 */
public interface MutableGraph<T> extends Graph<T> {
    /**
     * Adds a node to the graph with no edges
     *
     * @param node the node to add
     * @return true if the node has not been added, false otherwise.
     */
    boolean add(T node);

    /**
     * Adds a node to the graph with an edge to another node
     *
     * @param node the node to add
     * @param edge the edge to another node
     * @return true if the node has not been added, false otherwise.
     */
    boolean add(T node, T edge);

    /**
     * Adds a node to the graph with a collection of edges to other nodes
     *
     * @param node  the node to add
     * @param edges the edges to other nodes
     * @return true if the node has not been added, false otherwise.
     */
    boolean add(T node, Collection<T> edges);
}
