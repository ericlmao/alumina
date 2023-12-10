package games.negative.alumina.dependency;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The MavenDependency class represents a Maven dependency with group, artifact, version, and repository URL.
 */
public record MavenDependency(String group, String artifact, String version, MavenRepository repository) {

    /**
     * Returns the URL of the Maven dependency.
     *
     * @return the URL of the Maven dependency.
     * @throws MalformedURLException if the URL is malformed.
     */
    public URL getURL() throws MalformedURLException {
        MavenRepository mavenRepository = (repository == null ? DependencyLoader.CENTRAL : repository);

        String repo = mavenRepository.url();
        if (!repo.endsWith("/")) {
            repo += "/";
        }
        repo += "%s/%s/%s/%s-%s.jar";

        String url = String.format(repo, this.group.replace(".", "/"), this.artifact, this.version, this.artifact, this.version);
        return new URL(url);
    }
}
