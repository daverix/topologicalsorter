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

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test

class TopologicalSorterTest {
    @Test
    fun sortTwoNodes() {
        val graph = graphOf("a" to setOf("b"))
        assertThat(graph.sort()).containsExactly("b", "a").inOrder()
    }

    @Test
    fun sortThreeNodes() {
        val graph = graphOf(
                "a" to setOf("b"),
                "c" to setOf("a", "b")
        )
        assertThat(graph.sort()).containsExactly("b", "a", "c").inOrder()
    }

    @Test
    fun sortFiveNodes() {
        val a = "a"
        val b = "b"
        val c = "c"
        val d = "d"
        val e = "e"
        val graph = graphOf(
                c to setOf(b),
                d to setOf(c, a),
                e to setOf(d, b)
        )

        val sorted = graph.sort()

        assertThat(sorted).containsExactly(b, c, a, d, e).inOrder()
    }

    @Test
    fun detectCycles() {
        val a = "a"
        val b = "b"

        val dependencies = graphOf(a to setOf(b), b to setOf(a))

        try {
            dependencies.sort()
            fail("method should throw exception about cyclic dependencies detected")
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat().contains("cyclic dependency detected")
        }
    }
}