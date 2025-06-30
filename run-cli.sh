#!/bin/bash
cd StudentTracker || exit 1
mvn exec:java -Dexec.mainClass=com.laplateforme.tracker.StudentTrackerCLI 