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

import com.google.common.truth.Truth;

import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.daverix.topologicalsorter.PlacementSubject.assertThat;
import static org.junit.Assert.fail;

public class TopologicalSorterTest {
    @Test
    public void sortSimpleGraph() {
        A a = new A();
        B b = new B();
        C c = new C();
        D d = new D();
        E e = new E();

        List<MyService> services = new ArrayList<>(asList(a, b, c, d, e));

        TopologicalSorter.sort(services, new MySorter());

        Truth.assertThat(services)
                .named("should have all nodes in list after sort")
                .containsExactly(a, b, c, d, e);

        assertThat(services).hasItem(b).placedAfter(a);
        assertThat(services).hasItem(c).placedBefore(a);
        assertThat(services).hasItem(e).placedAfter(c);
        assertThat(services).hasItem(e).placedBefore(a);
    }

    @Test
    public void detectCycles() {
        Dependency a = new Dependency("a");
        Dependency b = new Dependency("b");
        List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(a);
        dependencies.add(b);

        a.dependencies.add(b);
        b.dependencies.add(a);

        try {
            TopologicalSorter.sort(dependencies, (node, allNodes) -> node.dependencies);
            fail("method should throw exception about cyclic dependencies detected");
        } catch (IllegalStateException e) {
            Truth.assertThat(e).hasMessageThat().contains("cyclic dependency detected");
        }
    }

    static class Dependency {
        private final String name;
        private final List<Dependency> dependencies = new ArrayList<>();

        Dependency(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    interface MyService {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Before {
        Class<?>[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface After {
        Class<?>[] value();
    }

    class A implements MyService {
        @Override
        public String toString() {
            return "A";
        }
    }

    @After(A.class)
    class B implements MyService {
        @Override
        public String toString() {
            return "B";
        }
    }

    @Before(A.class)
    class C implements MyService {
        @Override
        public String toString() {
            return "C";
        }
    }

    class D implements MyService {
        @Override
        public String toString() {
            return "D";
        }
    }

    @After(C.class)
    @Before(A.class)
    class E implements MyService {
        @Override
        public String toString() {
            return "E";
        }
    }

    class MySorter implements TopologicalSorter.EdgesFactory<MyService> {
        @Override
        public Collection<MyService> getEdges(MyService node, List<MyService> allNodes) {
            Class<? extends MyService> nodeClass = node.getClass();

            List<MyService> edges = allNodes.stream()
                    .filter(other -> {
                        Class<? extends MyService> otherClass = other.getClass();
                        Before before = otherClass.getAnnotation(Before.class);
                        if (before != null) {
                            List<Class<?>> classesBefore = asList(before.value());
                            return classesBefore.contains(nodeClass);
                        }

                        return false;
                    }).collect(toList());

            After after = nodeClass.getAnnotation(After.class);
            if (after != null) {
                List<Class<?>> classesAfter = asList(after.value());
                edges.addAll(allNodes.stream()
                        .filter(other -> classesAfter.contains(other.getClass()))
                        .collect(toList()));
            }

            return edges;
        }
    }
}