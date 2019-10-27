package dependencymanagement.loading;

import dependencymanagement.resolution.DependencyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * A cache which mapping dependency strings to it's classes.
 * @since 1.0.0
 *
 */

@Singleton
public class DependencyManagerClassLoaderCache {
  private final Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

  private static DependencyResolver dependencyResolver;

  private HashMap<String, DependencyManagerClassLoader> cache = new HashMap<>();

  private static DependencyManagerClassLoaderCache instance=null;

  public static DependencyManagerClassLoaderCache getInstance(){
    if(instance==null){
      instance=new DependencyManagerClassLoaderCache();
    }
    return instance;
  }

  public static void initialize(DependencyResolver dependencyResolver){
    DependencyManagerClassLoaderCache.dependencyResolver =dependencyResolver;
  }

  private DependencyManagerClassLoaderCache(){
  }

  public DependencyManagerClassLoader getClassLoader(List<String> dependencies) {
    //get all the class loaders
    List<DependencyManagerClassLoader> dependencyManagerClassLoaderList =new ArrayList<>();
    for(String dependency:dependencies){
        dependencyManagerClassLoaderList.add(getClassLoader(dependency));
    }

    //Concatenate class loaders
    List<URL> urls=new ArrayList<>();
    for(DependencyManagerClassLoader dependencyManagerClassLoader : dependencyManagerClassLoaderList){
      List<URL> classLoaderUrls=Arrays.asList(dependencyManagerClassLoader.getURLs());
      urls.addAll(classLoaderUrls);
    }
    URL[] urlsArray=urls.toArray(new URL[urls.size()]);
    DependencyManagerClassLoader dependencyManagerClassLoader = new DependencyManagerClassLoader(urlsArray);
    return dependencyManagerClassLoader;
  }

  public DependencyManagerClassLoader getClassLoader(String dependency) {
      if (!cache.containsKey(dependency)) {
        List<String> dependenciesClasspathList = dependencyResolver.resolve(new ArrayList<String>(){{add(dependency);}});
        List<URL> urls=new ArrayList<>();

        for(String dependencyClasspath : dependenciesClasspathList) {
          try {
            URL url = new URL(dependencyClasspath);
            urls.add(url);
          } catch (MalformedURLException e) {
            logger.error("Invalid URL in DependencyManagerClassLoader: "+ dependencyClasspath);
          }
        }
        if(!urls.isEmpty()) {
          URL[] urlsArray = urls.toArray(new URL[urls.size()]);
          DependencyManagerClassLoader dependencyManagerClassLoader = new DependencyManagerClassLoader(urlsArray);
          dependencyManagerClassLoader.setClassPathString(urls);
          cache.put(dependency, dependencyManagerClassLoader);
        }
      }
    return cache.get(dependency);
  }

  @Override
  public String toString() {
    return "Extra Dependency Class Loader";
  }
}