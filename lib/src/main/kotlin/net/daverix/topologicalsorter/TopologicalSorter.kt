@file:JvmName("TopologicalSorter")
/*
   Copyright 2020 David Laurell

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
package net.daverix.topologicalsorter

typealias Graph<T> = Map<T, Set<T>>

/**
 * Creates a map where
 * @param pairs variable number of nodes and their edges where [Pair.first] is the node and [Pair.second] are the
 * edges of that node
 */
fun <T> graphOf(vararg pairs: Pair<T, Set<T>>): Graph<T> = mapOf(*pairs)

/**
 * This method sorts nodes in an acyclic directed graph using a depth first algorithm which can
 * be found at [Wikipedia](https://en.wikipedia.org/wiki/Topological_sorting)
 *
 * @return a sorted list of nodes
 */
fun <T> Graph<T>.sort(): List<T> {
    val unmarked = mutableListOf<T>()
    val sorted = mutableListOf<T>()
    val temporaryMarked = mutableListOf<T>()

    fun visit(node: T) {
        if (node in sorted) return

        check(node !in temporaryMarked) {
            "cyclic dependency detected, $node already visited"
        }

        temporaryMarked.add(node)
        this[node]?.forEach { visit(it) }

        unmarked.remove(node)
        temporaryMarked.remove(node)
        sorted.add(node)
    }

    unmarked.addAll(keys)
    while (unmarked.isNotEmpty()) {
        visit(unmarked.first())
    }

    return sorted
}
