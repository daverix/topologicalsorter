# Topological sorter
Implementation of depth first search algorithm found on https://en.wikipedia.org/wiki/Topological_sorting.

## Usage

    List<MyNode> nodes = ...
        
    TopologicalSorter.sort(nodes, new TopologicalSorter.EdgesFactory<MyNode>() {
        @Override
        public Collection<MyNode> getEdges(MyNode currentNode, List<MyNode> allNodes) {
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
