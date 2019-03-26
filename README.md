# graph-visualization
Visualizing graphs using GraphStream. 

Currently, compiling using Maven and running
```
java -jar  graph-visualization-1.0-SNAPSHOT-jar-with-dependencies.jar 
```
will yield the following command line interface:
```
what do you want to generate? type help if you're not sure.
~$ 
```
The following options are available:
```
~$ help
List of commands
1) GenerateConnectedRandomGraph <num iterations>: generates connected random graph
2) GeneratePlanarRandomGraph <num iterations>: generates Dorogovtsev-Mendes planar random graph
3) GeneratePreferentialAttachmentGraph <num iterations> <max edges per node step> <exact>: Generates a Barbasi-Albert preferential attachment graph, always planar/connected
4) GenerateGridGraph [cross] [torus] [xy]: generates grid graph randomly
5) GenerateSmallWorldGraph <n> <k> <beta>: Generates a Watts-Strogatz small world graph
6) GenerateEuclideanGraph <num iterations>: generates a random Euclidean graph
7) exit: quit this program.
~$ 
```
Running, for example, `GenerateConnectedRandomGraph` will yield a GUI display of the generated result:
![pla](https://github.com/thefangbear/graph-visualization/raw/master/graphviz.png)
