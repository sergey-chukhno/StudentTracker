#!/bin/bash
# Switch to Java 17 (required for Mockito)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

# Run all tests with detailed output, forcing Maven to use Java 17 for compilation
mvn clean test -Dsurefire.printSummary=true -Dsurefire.useFile=false -Dmaven.compiler.source=17 -Dmaven.compiler.target=17 -f backend/pom.xml 