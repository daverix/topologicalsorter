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

    List<MyNode> unsorted = ...
        
    List<MyNode> sorted = TopologicalSorter.sort(unsorted, new TopologicalSorter.EdgesFactory<MyNode>() {
        @Override
        public Collection<MyNode> getEdges(MyNode currentNode, Collection<MyNode> allNodes) {
            List<MyNode> edges = new ArrayList<>();
          
            // filter out the nodes connecting currentNode by going through all nodes in allNodes
          
            return edges;
        }
    });

## License

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
