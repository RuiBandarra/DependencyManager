package dependencymanagement.resolution;

import dependencymanagement.maven.events.listeners.ConsoleRepositoryListener;
import dependencymanagement.maven.events.listeners.ConsoleTransferListener;
import dependencymanagement.maven.repositories.ManualRepositorySystemFactory;
import dependencymanagement.maven.settings.MavenSettingsReader;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.*;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * Resolves Dependencies using Aether library.
 * @since 1.0.0
 *
 */
public class DependencyResolver {
    private final Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private String privateReleasesRepositoryId;

    private String privateSnapshotsRepositoryId;

    private List<String> privateRepositoryArtifactsIds;

    private HashMap<String,String> repositories = new HashMap<>();

    private HashMap<String,Authentication> repositoriesAuthentication =new HashMap<>();

    private static Settings mavenSettings;

    public DependencyResolver(HashMap<String,String> repositories, List<String> privateRepositoryArtifactsIds, String privateReleasesRepositoryId, String privateSnapshotsRepositoryId){
        this.privateReleasesRepositoryId =privateReleasesRepositoryId;
        this.privateSnapshotsRepositoryId =privateSnapshotsRepositoryId;
        this.privateRepositoryArtifactsIds = privateRepositoryArtifactsIds;

        mavenSettings = MavenSettingsReader.getInstance().getMavenSettings();//Read Maven Settings from settings.xml/settings-security.xml to fetch credentials for the private repositories, as defined in application.conf
        this.repositories.putAll(repositories);

        //Repository Authentication.
        if (mavenSettings != null) {
            for(Entry<String,String> entry: this.repositories.entrySet()) {
                String repositoryId=entry.getKey();
                for (Server server : mavenSettings.getServers()) {
                    if (server.getId().equals(entry.getKey())) {
                        Authentication repositoryAuthentication = new AuthenticationBuilder().addUsername(server.getUsername()).addPassword(server.getPassword()).build();
                        repositoriesAuthentication.put(repositoryId,repositoryAuthentication);
                        break;
                    }
                }
            }
        }

        logger.debug("Using repositories: "+ this.repositories.values());
    }

    /**
     * Resolves dependencies transitively.
     *
     * @param dependencies
     * @return
     */
    public List<String> resolve(List<String> dependencies){
        //Create repo and open session
        RepositorySystem repositorySystem = ManualRepositorySystemFactory.newRepositorySystem();

        DefaultRepositorySystemSession repositorySystemSession = openRepositorySystemSession(repositorySystem);

        HashMap<String,RemoteRepository> repositories= getRepositories();

        //start resolving
        List<String> classpaths=new ArrayList<>();
        for(String artifactCoordinates:dependencies) {
            try {
                DefaultArtifact artifact = new DefaultArtifact(artifactCoordinates); //this constructor may throw an Unchecked Exception: IllegalArgumentException.

                DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);
                Dependency dependency=new Dependency(artifact, JavaScopes.COMPILE);

                CollectRequest collectRequest = new CollectRequest();
                collectRequest.setRoot(dependency);
                collectRequest.setRootArtifact(artifact);

                List<Dependency> dependenciesList=new ArrayList<>();
                dependenciesList.add(dependency);
                collectRequest.setDependencies(dependenciesList);

                //finds and sets the repository to use for this artifact
                List<RemoteRepository> repositoriesToSearch = new ArrayList<>();
                if(privateRepositoryArtifactsIds.contains(artifact.getArtifactId()))
                    if (artifact.isSnapshot())
                        repositoriesToSearch.add(repositories.get(privateSnapshotsRepositoryId));
                    else
                        repositoriesToSearch.add(repositories.get(privateReleasesRepositoryId));

                //Add all the other repositories
                for(Entry<String,RemoteRepository> entry:repositories.entrySet())
                    if(!entry.getValue().getId().equals(privateReleasesRepositoryId) && !entry.getValue().getId().equals(privateSnapshotsRepositoryId))
                        repositoriesToSearch.add(entry.getValue());

                collectRequest.setRepositories(repositoriesToSearch);

                DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter);

                List<ArtifactResult> artifactResults = null;
                try {
                    artifactResults = repositorySystem.resolveDependencies(repositorySystemSession, dependencyRequest).getArtifactResults();
                } catch (DependencyResolutionException e) {
                    logger.info("Failed to resolve dependency: "+artifactCoordinates);
                    logger.info(e.getMessage());
                }
                if (artifactResults != null) {
                    for (ArtifactResult artifactResult : artifactResults) {
                        logger.debug("Using " + artifactResult.getArtifact() + " in " + artifactResult.getArtifact().getFile());
                        String[] customSchemes = { "file" };
                        UrlValidator urlValidator = new UrlValidator(customSchemes);
                        String artifactClasspath="file://"+artifactResult.getArtifact().getFile();
                        if(urlValidator.isValid(artifactClasspath)){
                            classpaths.add(artifactClasspath);
                        }

                    }
                }
            }catch(IllegalArgumentException exception){
                logger.info( "Bad artifact coordinates: \"" + artifactCoordinates
                        + "\", expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>");
            }
        }
        return classpaths;
    }

    /**
     * Resolves dependencies non-transitively.
     *
     * @param dependencies
     * @return
     */
    public List<String> resolveDependenciesNonTransitively(List<String> dependencies){
        RepositorySystem repositorySystem = ManualRepositorySystemFactory.newRepositorySystem(); //Create repo and open session

        DefaultRepositorySystemSession repositorySystemSession = openRepositorySystemSession(repositorySystem);

        HashMap<String,RemoteRepository> repositories= getRepositories();

        //start resolving
        List<String> classpaths=new ArrayList<>();
        for(String artifactCoordinates:dependencies) {
            try {
                DefaultArtifact defaultArtifact = new DefaultArtifact(artifactCoordinates); //this constructor may throw an Unchecked Exception: IllegalArgumentException.
                Dependency dependency=new Dependency(defaultArtifact, JavaScopes.COMPILE);

                //finds and sets the repository to use for this artifact
                List<RemoteRepository> repositoriesToSearch = new ArrayList<>();
                if(privateRepositoryArtifactsIds.contains(defaultArtifact.getArtifactId()))
                    if(defaultArtifact.isSnapshot())
                        repositoriesToSearch.add(repositories.get(privateSnapshotsRepositoryId));
                    else
                        repositoriesToSearch.add(repositories.get(privateReleasesRepositoryId));
                //Add all the other repositories
                for(Entry<String,RemoteRepository> entry:repositories.entrySet())
                    if(!entry.getValue().getId().equals(privateReleasesRepositoryId) && !entry.getValue().getId().equals(privateSnapshotsRepositoryId))
                        repositoriesToSearch.add(entry.getValue());

                ArtifactRequest artifactRequest = new ArtifactRequest();
                ArtifactResult artifactResult;

                artifactRequest.setArtifact(defaultArtifact);
                artifactRequest.setRepositories(repositoriesToSearch);

                artifactResult = repositorySystem.resolveArtifact(repositorySystemSession, artifactRequest);

                Artifact artifact=artifactResult.getArtifact();

                logger.info(dependency+ "resolved to " + artifact.getFile().toString());

            }catch(IllegalArgumentException exception){
                logger.info( "Bad artifact coordinates: \"" + artifactCoordinates
                        + "\", expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>");
            }
            catch (ArtifactResolutionException e) {
                logger.info("Artifact Resolution exception for "+artifactCoordinates);
                logger.info(e.getMessage());
            }catch(NullPointerException e){
                logger.info("NullPointerException when resolving "+artifactCoordinates);
                logger.info(e.getMessage());
            }
        }
        return classpaths;
    }


    private DefaultRepositorySystemSession openRepositorySystemSession(RepositorySystem repositorySystem){
        /*
         *  It is assumed the session itself is not changed once initialized and being used by the repository system.
         *  See: https://maven.apache.org/resolver/apidocs/org/eclipse/aether/DefaultRepositorySystemSession.html#setReadOnly()
         */
        DefaultRepositorySystemSession repositorySystemSession=null;

        String localRepositoryFolder=mavenSettings.getLocalRepository();

        if(!(localRepositoryFolder==null || localRepositoryFolder.equals(""))){
            repositorySystemSession = MavenRepositorySystemUtils.newSession();

            LocalRepository localRepository = new LocalRepository(mavenSettings.getLocalRepository());

            repositorySystemSession.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(repositorySystemSession, localRepository));

            repositorySystemSession.setTransferListener(new ConsoleTransferListener());

            repositorySystemSession.setRepositoryListener(new ConsoleRepositoryListener());
        }
        return repositorySystemSession;
    }

    private HashMap<String,RemoteRepository> getRepositories(){
        //creating the remote repositories list to search from.
        HashMap<String,RemoteRepository> repositories=new HashMap<>();
        for(Entry<String,String> entry: this.repositories.entrySet()) {
            //Prepare repository data
            String repositoryId=entry.getKey();
            String repositoryUrl=entry.getValue();
            Authentication authentication = repositoriesAuthentication.getOrDefault(repositoryId,null);

            //Set repository policy (snapshot or releases)
            RepositoryPolicy repositoryPolicyEnabled = new RepositoryPolicy(true, "daily", "warn");
            RepositoryPolicy repositoryPolicyDisabled = new RepositoryPolicy(false, "daily", "warn");

            RemoteRepository repository;

            if(repositoryId.equals(privateReleasesRepositoryId)){
                repository = new RemoteRepository.Builder(repositoryId, "default", repositoryUrl).setReleasePolicy(repositoryPolicyEnabled).setSnapshotPolicy(repositoryPolicyDisabled).setAuthentication(authentication).build();

            } else if(repositoryId.equals(privateSnapshotsRepositoryId)){
                repository = new RemoteRepository.Builder(repositoryId, "default", repositoryUrl).setReleasePolicy(repositoryPolicyDisabled).setSnapshotPolicy(repositoryPolicyEnabled).setAuthentication(authentication).build();

            }else{
                repository = new RemoteRepository.Builder(repositoryId,"default", repositoryUrl).setAuthentication(authentication).build();
            }

            repositories.put(repositoryId,repository);
        }
        return repositories;
    }
}