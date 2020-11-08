# Topological sorter
This library provides an implementation of a depth first sorting algorithm for directed acyclic 
graphs found on [Wikipedia](https://en.wikipedia.org/wiki/Topological_sorting#Depth-first_search):

    L ← Empty list that will contain the sorted nodes
    while there are unmarked nodes do
        select an unmarked node n
        visit(n)
        
    function visit(node n)
        if n has a permanent mark then return
        if n has a temporary mark then stop (not a DAG)
        mark n temporarily
        for each node m with an edge from n to m do
            visit(m)
        mark n permanently
        add n to head of L

## Usage
Let's say we have a graph where a depends on b and c depends on both a and b.

Kotlin:

```kotlin
import net.daverix.topologicalsorter.graphOf
import net.daverix.topologicalsorter.sort

fun main() {
    val a = "A"
    val b = "B"
    val c = "C"
    
    val graph = graphOf(
        a to setOf(b),      // a depends on b
        c to setOf(a, b)    // c depends on a and b 
    )
    
    val sorted = graph.sort()
    
    println(sorted) // prints [B, A, C]
}
```

Java:

```java
class Example {
    public static void main(String[] args) {
        String a = "A";
        String b = "B";
        String c = "C";
        
        Map<String, Set<String>> graph = new HashMap<String, Set<String>>();
        
        Set<String> dependenciesOfA = new HashSet<String>();
        dependenciesOfA.add(b);
        graph.put(a, dependenciesOfA);
        
        Set<String> dependenciesOfC = new HashSet<String>();
        dependenciesOfC.add(a);
        dependenciesOfC.add(b);
        graph.put(c, dependenciesOfC);
        
        List<String> sorted = TopologicalSorter.sort(graph);
        
        System.out.println(sorted); // prints [B, A, C]
    }
}
```

## License

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
