# when we create docker image from another docker image
# we use this FROM command
FROM openjdk:8-jdk-alpine
# VOLUME /tmp
# it is not always necessary
# according to spring documentation
# this is where spring create working directory for Tomcat by default
# and this step is optional for most of the spring applications
VOLUME /tmp
# copy executable jar file
COPY target/PhotoAppApiConfigServer-0.0.1-SNAPSHOT.jar ConfigServer.jar
# the entry point is a line in which we execute the Java jar command to run
# our executable jar file
# and if you spring application needs to receive any other system properties
# or a command line arguments.
# this is the line where you include them
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "ConfigServer.jar"]

### I do not have encryptionKey ###
# we will copy the key store file from our local computer to docker container
#COPY apiEncryptionKey.jks apiEncryptionKey.jks
# we also copy java cryptography extension libraries from our local computer to docker container
# you will not need to do it in most of the spring applications
# for our spring applications, we will be able to use encryption included
# in this Java cryptography extension libraries
#COPY UnlimitedJCEPolicyJDK8/* /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/