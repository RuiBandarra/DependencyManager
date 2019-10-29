package startup;

import com.typesafe.config.ConfigException;
import dependencymanagement.loading.DependencyResolverClassLoaderCache;
import dependencymanagement.resolution.DependencyResolver;
import org.apache.commons.validator.routines.UrlValidator;
import play.Logger;
import play.api.Configuration;
import play.api.Environment;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


@Singleton
public class StartupService {
  private final Environment environment;
  private final Configuration configuration;

  private final String DEPENDENCY_RESOLVER_REPOSITORIES ="dependency.resolver.repositories";
  private final String DEPENDENCY_RESOLVER_PRIVATE_REPOSITORY_ARTIFACTS_IDS ="dependency.resolver.private.repository.artifacts.ids";
  private final String DEPENDENCY_RESOLVER_PRIVATE_REPOSITORIES_RELEASES_ID ="dependency.resolver.private.repositories.releases.id";
  private final String DEPENDENCY_RESOLVER_PRIVATE_REPOSITORIES_SNAPSHOTS_ID ="dependency.resolver.private.repositories.snapshots.id";
  private static final DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  @Inject
  public StartupService(Environment environment, Configuration configuration) {
    // Assign the Injected Environment and Configuration objects
    this.environment = environment;
    this.configuration = configuration;

    //Get repositories. IDs must match maven's settings.xml Format is ID::URL (i.e. dependency.resolver.repositories=local_releases::http://localhost:8081/eDelivery;local_snapshots::http://localhost:8081/eDelivery-snapshots) .
    //Authentication happens after reading maven settings, in the DependencyResolver.
    LinkedHashMap<String,String> repositories=new LinkedHashMap<>();
    String repositoriesString="";
    try{
      repositoriesString = configuration.underlying().getString(DEPENDENCY_RESOLVER_REPOSITORIES);
    }catch(ConfigException.Missing|ConfigException.WrongType e){
      Logger.error(DEPENDENCY_RESOLVER_REPOSITORIES +" not set in application.conf. Assuming default values.");
    }
    if (!repositoriesString.isEmpty()) {
      String[] customSchemes = { "https", "http", "file" };
      UrlValidator urlValidator = new UrlValidator(customSchemes,UrlValidator.ALLOW_LOCAL_URLS);
      for (String repositoryString : repositoriesString.split(";")) {
        //Add url
        String[] repositoryData=repositoryString.split("::");
        if(repositoryData.length==2){
          String repositoryId=repositoryData[0];
          String repositoryUrl=repositoryData[1];

          if (urlValidator.isValid(repositoryUrl)) {
            repositories.put(repositoryId,repositoryUrl);
            Logger.info("Added repository "+repositoryId+": "+ repositoryUrl);
          } else {
            Logger.warn("Skipping invalid repository "+repositoryId+" "+ repositoryUrl);
          }
        }
      }
    }

    List<String> privateRepositoryArtifactsIds=new ArrayList<>();
    try {
      String privateRepositoryArtifactsIdsApplicationVariable=configuration.underlying().getString(DEPENDENCY_RESOLVER_PRIVATE_REPOSITORY_ARTIFACTS_IDS);
      if(!privateRepositoryArtifactsIdsApplicationVariable.equals("")){
        privateRepositoryArtifactsIds.addAll(Arrays.asList(privateRepositoryArtifactsIdsApplicationVariable.split(";")));
      }
    }catch(ConfigException.Missing|ConfigException.WrongType e){
      Logger.error(privateRepositoryArtifactsIds+" not set in application.conf. Assuming default values.");
    }

    String dependencyResolverRepositoriesReleasesId="";
    try {
      dependencyResolverRepositoriesReleasesId = configuration.underlying().getString(DEPENDENCY_RESOLVER_PRIVATE_REPOSITORIES_RELEASES_ID);
    }catch(ConfigException.Missing|ConfigException.WrongType e){
      Logger.error(DEPENDENCY_RESOLVER_PRIVATE_REPOSITORIES_RELEASES_ID +"not set in application.conf. Assuming default values.");
    }

    String dependencyResolverRepositoriesSnapshotsId="";
    try {
      dependencyResolverRepositoriesSnapshotsId = configuration.underlying().getString(DEPENDENCY_RESOLVER_PRIVATE_REPOSITORIES_SNAPSHOTS_ID);
    }catch(ConfigException.Missing|ConfigException.WrongType e){
      Logger.error(DEPENDENCY_RESOLVER_PRIVATE_REPOSITORIES_SNAPSHOTS_ID +" not set in application.conf. Assuming default values.");
    }

    DependencyResolver dependencyResolver = new DependencyResolver(repositories,privateRepositoryArtifactsIds,dependencyResolverRepositoriesReleasesId,dependencyResolverRepositoriesSnapshotsId);
    DependencyResolverClassLoaderCache cache= DependencyResolverClassLoaderCache.getInstance();
    cache.initialize(dependencyResolver);

  }

  public static DateFormat getDateFormat() {
    return dateFormat;
  }

}
