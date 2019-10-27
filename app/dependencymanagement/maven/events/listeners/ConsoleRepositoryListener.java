package dependencymanagement.maven.events.listeners;

import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Repository listener which logs events to the console.
 * @since 1.0.0
 *
 */
public class ConsoleRepositoryListener extends AbstractRepositoryListener {
    private final Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    public void artifactDeployed(RepositoryEvent event){
        logger.debug("Deployed Artifact" + event.getArtifact() + " to " + event.getRepository());
    }

    public void artifactDeploying(RepositoryEvent event){
        logger.debug("Deploying Artifact" + event.getArtifact() + " to " + event.getRepository());
    }

    public void artifactDescriptorInvalid(RepositoryEvent event){
        logger.debug("Invalid artifact descriptor for " + event.getArtifact() + ": " + event.getException().getMessage());
    }

    public void artifactDescriptorMissing(RepositoryEvent event){
        logger.debug("Missing artifact Descriptor for " + event.getArtifact());
    }

    public void artifactInstalled(RepositoryEvent event){
        logger.debug("Installed Artifact " + event.getArtifact() + " to " + event.getFile());
    }

    public void artifactInstalling(RepositoryEvent event){
        logger.debug("Installing Artifact " + event.getArtifact() + " to " + event.getFile());
    }

    public void artifactResolved(RepositoryEvent event){
        logger.debug("Resolved Artifact " + event.getArtifact() + " from " + event.getRepository());
    }

    public void artifactDownloading(RepositoryEvent event){
        logger.debug("Downloading Artifact " + event.getArtifact() + " from " + event.getRepository());
    }
    public void artifactDownloaded(RepositoryEvent event) {
        logger.debug("Downloaded Artifact " + event.getArtifact() + " from " + event.getRepository());
    }

    public void artifactResolving(RepositoryEvent event){
        logger.debug("Resolving Artifact "+event.getArtifact().toString());
    }

    public void metadataDeployed(RepositoryEvent event){
        logger.debug("Deployed Metadata" + event.getMetadata() + " to " + event.getRepository());
    }

    public void metadataDeploying(RepositoryEvent event){
        logger.debug("Deploying Metadata " + event.getMetadata() + " to " + event.getRepository());
    }

    public void metadataInstalled(RepositoryEvent event){
        logger.debug("Installed Metadata" + event.getMetadata() + " to " + event.getFile());
    }

    public void metadataInstalling(RepositoryEvent event){
        logger.debug("Installing Metadata" + event.getMetadata() + " to " + event.getFile());
    }

    public void metadataInvalid(RepositoryEvent event){
        logger.debug("Invalid Metadata " + event.getMetadata().toString());
    }

    public void metadataResolved(RepositoryEvent event){
        logger.debug("Resolved Metadata " + event.getMetadata() + " from " + event.getRepository());
    }

    public void metadataResolving(RepositoryEvent event){
        logger.debug("Resolving Metadata " + event.getMetadata() + " from " + event.getRepository());
    }
}
