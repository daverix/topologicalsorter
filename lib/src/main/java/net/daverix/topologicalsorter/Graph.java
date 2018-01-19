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


import java.util.Set;

/**
 * Represents a graph. It can be iterated to go through each node and it has a method to get
 * the edges for a specific node.
 *
 * @param <T> the type of node in the graph
 */
public interface Graph<T> extends Iterable<T> {
    /**
     * Get the edges for the provided node
     * @param node the node to get edges from
     *
     * @return a set of edges
     */
    Set<T> getEdges(T node);
}
