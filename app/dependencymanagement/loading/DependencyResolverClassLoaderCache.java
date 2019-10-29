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
public class DependencyResolverClassLoaderCache {
  private final Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

  private static DependencyResolver dependencyResolver;

  private HashMap<String, DependencyResolverClassLoader> cache = new HashMap<>();

  private static DependencyResolverClassLoaderCache instance=null;

  public static DependencyResolverClassLoaderCache getInstance(){
    if(instance==null){
      instance=new DependencyResolverClassLoaderCache();
    }
    return instance;
  }

  public static void initialize(DependencyResolver dependencyResolver){
    DependencyResolverClassLoaderCache.dependencyResolver =dependencyResolver;
  }

  private DependencyResolverClassLoaderCache(){
  }

  public DependencyResolverClassLoader getClassLoader(List<String> dependencies) {
    //get all the class loaders
    List<DependencyResolverClassLoader> DependencyResolverClassLoaderList =new ArrayList<>();
    for(String dependency:dependencies){
        DependencyResolverClassLoaderList.add(getClassLoader(dependency));
    }

    //Concatenate class loaders
    List<URL> urls=new ArrayList<>();
    for(DependencyResolverClassLoader DependencyResolverClassLoader : DependencyResolverClassLoaderList){
      List<URL> classLoaderUrls=Arrays.asList(DependencyResolverClassLoader.getURLs());
      urls.addAll(classLoaderUrls);
    }
    URL[] urlsArray=urls.toArray(new URL[urls.size()]);
    DependencyResolverClassLoader DependencyResolverClassLoader = new DependencyResolverClassLoader(urlsArray);
    return DependencyResolverClassLoader;
  }

  public DependencyResolverClassLoader getClassLoader(String dependency) {
      if (!cache.containsKey(dependency)) {
        List<String> dependenciesClasspathList = dependencyResolver.resolve(new ArrayList<String>(){{add(dependency);}});
        List<URL> urls=new ArrayList<>();

        for(String dependencyClasspath : dependenciesClasspathList) {
          try {
            URL url = new URL(dependencyClasspath);
            urls.add(url);
          } catch (MalformedURLException e) {
            logger.error("Invalid URL in DependencyResolverClassLoader: "+ dependencyClasspath);
          }
        }
        if(!urls.isEmpty()) {
          URL[] urlsArray = urls.toArray(new URL[urls.size()]);
          DependencyResolverClassLoader DependencyResolverClassLoader = new DependencyResolverClassLoader(urlsArray);
          DependencyResolverClassLoader.setClassPathString(urls);
          cache.put(dependency, DependencyResolverClassLoader);
        }
      }
    return cache.get(dependency);
  }

  @Override
  public String toString() {
    return "Extra Dependency Class Loader";
  }
}