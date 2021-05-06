# Maven-Grabber

This microservice search items by artifact or group id in Maven Central.

# Usage
Clone project.
In command line use combination
java -jar maven-grabber.jar.
Then application should run.
Options:
-a <args>, <args>, ...
search elements by artifactId,
-g <args>,<args>, ...
search elements by groupId. Maven will respond with JSON. Number of arguments are unlimited.

# Options
In file env.properties you can set number of pool threads, rows for responding
or another resource.