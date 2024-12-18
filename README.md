# PlantGen
## Team 141
- ESSDAA001
- OMRANE005
- SLMSAA010

## Description
The code uses pink noise sampling to generate the desired number of points. These points represent possible plant placements. The viability at each point is then calculated for each plant, and a roulette wheel determines whether not a plant is placed (including what plant it is). These are stored in the Ecosystem. The attributes of the plant are calculated and the abiotic data of the ecosystem is updated for each plant placement.

## Visuals Explanation
The output visuals are 2 plots. The first plot is a pink noise sampling plot, this displays the possible plant placements according to the number of desired input. The second plot is a plant placement plot which displays each plant placed in the canopy and undergrowth.

## Installation and Usage
- Java version: 22.0.2
- To install the project dependencies:  
```bash
./gradlew build --refresh-dependencies
```
- To compile the project:  
```bash
./gradlew build
```
Before running the project, ensure that the input file contains the file paths to the data, including the species data(note: the full file paths).  
Furthermore, ensure that the file path to the inputs.txt is also copied correctly (again, full not relative). If using a Windows machine, use Winputs.txt, if using a Linux machine, use Linputs.txt.  
The Run function in an IDE should run the project with relative file paths, however using the gradle run command will require full file paths.

- To run the project:
```bash
./gradlew run
```

## Roadmap/Future of the Project
- Optimisation of generation (i.e. make run fast)
- Output JSON file of ecosystem
- Render ecosystm in Unity