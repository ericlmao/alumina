/*
 *  MIT License
 *
 * Copyright (C) 2024 Negative Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
