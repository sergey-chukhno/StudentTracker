#!/bin/bash
# Script to build classpath and start the StudentTracker server with all dependencies

set -e

cd StudentTracker

# Build the classpath and save to cp.txt
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt

# Start the server with the full classpath
java -cp target/classes:$(cat cp.txt) com.laplateforme.tracker.StudentTrackerServer 