## PS4 - Kevin Bacon / Graph Paths

Graph algorithms applied to the “Kevin Bacon” dataset. This project builds a graph from movie and actor files, then supports shortest-path style queries and exploration.

### Run
- Primary entry point: `src/GraphLibraryTest.java` (class `GraphLibraryTest`)
- The program is interactive: it prints a menu and waits for commands on stdin.

### Important: update file paths
`GraphLibraryTest.java` currently uses absolute paths to input files (movies/actors/movie-actors). Update them so they point to your local copies of:
- `movies*.txt`
- `actors*.txt`
- `movie-actors*.txt`

### Commands (as implemented)
- `q` quits
