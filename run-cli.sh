#!/bin/bash
cd backend || exit 1
mvn exec:java -Dexec.mainClass=com.laplateforme.tracker.StudentTrackerCLI 