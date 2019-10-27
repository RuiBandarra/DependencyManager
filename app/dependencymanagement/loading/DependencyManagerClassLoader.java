package dependencymanagement.loading;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;


/**
 *
 * Class loader. Will be stored in cache.
 * @since 1.0.0
 *
 */
public class DependencyManagerClassLoader extends URLClassLoader{
  private String classPathString;

  public DependencyManagerClassLoader(URL[] urls) {
    super(urls);
    classPathString=getClassPathStringFromUrls(urls);
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    return super.loadClass(name, resolve);
  }

  public void setClassPathString(String classPathString) {
    this.classPathString = classPathString;
  }

  public void setClassPathString(List<URL> urls) {
    this.classPathString = getClassPathStringFromUrls(urls);
  }

  public void setClassPathString(URL[] urls) {
    this.classPathString = getClassPathStringFromUrls(urls);
  }

  public String getClassPathString() {
    return classPathString;
  }

  public String getClassPathStringFromUrls(URL[] urls) {
    return getClassPathStringFromUrls(Arrays.asList(urls));
  }

  public String getClassPathStringFromUrls(List<URL> urls) {
    String classpath = "";

    for (URL url : urls) {
      classpath += url.getFile() + File.pathSeparator;
    }

    //Remove ending :
    if (classpath.endsWith(":")) {
      classpath = classpath.substring(0, classpath.length() - 1);
    }
    return classpath;
  }
}
