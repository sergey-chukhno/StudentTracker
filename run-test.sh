#!/bin/bash

# Switch to Java 17 for this script only
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo "[INFO] Using JAVA_HOME: $JAVA_HOME"

# Move to the Maven project directory
cd StudentTracker

# Run Maven tests with detailed output
mvn test -Dsurefire.printSummary=true -Dsurefire.useFile=false 