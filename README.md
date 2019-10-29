# Dependency Resolver

## What is Dependency Resolver
Dependency Resolver programmatically retrieves Dependencies from the Maven Central, or the customized private repositories for snapshots and releases, and stores them in the local Maven repository. The classes in these Dependencies then get loaded into a Classloader and stored in cache for faster retrieval.

One application for this code is that it can be used when we need to instantiate objects from classes at runtime, which were not available during compilation time, thus not part of the application's classpath.

Once the dependency is retrieved, a URLClassLoader subtype will be available in the cache, and any class inside can be loaded and instantiated through Reflection (i.e classLoader.loadClass("org.apache.commons.io.FileUtils").newInstance())

In the interface we can order it to retrieve a Dependency transitively. We are then presented with a table containing the local Maven Repository's Path to each of the artifacts.

## What is included
Version 1.0 of Dependency Resolver provides the following features:

    - Repository Customization
    - Repository Authentication through login in clear using settings.xml
    - Repository Authentication through encrypted login using security-settings.xml
    
## How it works
Dependency Resolver was written in Java using Eclipse Aether Library, the library used by Maven's Artifact Resolver.
This web interface was created using Play Framework and Scala.
