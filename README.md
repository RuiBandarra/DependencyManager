# Dependency Resolver

## What is Dependency Resolver
Dependency Resolver programmatically resolves transitively a given Dependency from the Maven Central, or the customized private repositories for snapshots and releases.

Once the dependency is resolved it is stored in the local Maven Repository. A URLClassLoader subtype is then made available in the cache, pointing at the path for the local dependency, and any class pertaining to that Dependency can then be loaded and instantiated through Reflection (i.e classLoader.loadClass("org.apache.commons.io.FileUtils").newInstance())

One application for this code is that it can be used when we need to instantiate objects from classes at runtime, which were not available during compilation time, thus not part of the application's classpath.

In the Web interface we can see the results for the Transitive Resolution of a Dependency of our choice.

This Single Page Application was written using Play, Scala, JQuery and Bootstrap, while the Dependency Resolver code was written in Java and uses Eclipse Aether Library, the same lirary used by Maven's Artifact Resolver.

## What is included
Version 1.0 of Dependency Resolver provides the following features:

    - Transitive Resolution of Dependencies from Maven Central
    - Support for Custom Repositories
    - Support for Repository Authentication through settings.xml
    - Support for Repository Authentication through settings-security.xml
  
## How it works
To resolve a Dependency use the form below and click Search. You will be presented with a table with the Search Results below.
