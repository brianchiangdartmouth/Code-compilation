## Summary
This is a folder programming for the Kevin Bacon game. An actor/actress's Kevin Bacon number is determined by their degrees of separation from Kevin Bacon, i.e. the number of social connections (defined in this case by social connections).

## What can I do?
Run BaconatorDriver:
```java
  "1: test on hard-coded simpler graph\n" +
  "2: test on graph from simpler test file\n" +
  "3: test on default actor graph\n" +
  "c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\n" +
  "d <low> <high>: list actors sorted by degree, with degree between low and high\n" +
  "i: list actors with infinite separation from the current center\n" +
  "p <name>: find path from <name> to current center of the universe\n" +
  "s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\n" +
  "u <name>: make <name> the center of the universe\n" +
  "q: quit game"

```

## File summary
*bacon*: folder containing actor-movie data.

*DataReader*: processes data files and compiles graph based on actors (nodes) and shared movies (edges) into graph

*Baconator*: BFS to find shortest path from other actors to center of universe

*BaconatorDriver*: where the code actually runs

*BaconatorDriverTest*: hard-coded graph of individuals for easy testing

*AdjacencyMapGraph*: implementation of graph with maps of outNeihg

*Graph*: basic graph interface with params V (type of vertices) and E (type of edge labels)
