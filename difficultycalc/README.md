# Difficulty Calculator

> A simple encounter difficulty calculator based on the standard 5e suggested calculations.

> **WARNING:** This is **NOT** production-quality software - this is meant for my personal use - USE AT YOUR OWN RISK.

## Build

It's a gradle project, simply checkout or download the source and run:

    ./gradlew clean build shadowJar

## Run

After building the project you will find the executable jar (`difficultycalc-all.jar`) in the `build/libs/` directory.
You can double-click the jar or run:

    java -jar difficultycalc-all.jar
