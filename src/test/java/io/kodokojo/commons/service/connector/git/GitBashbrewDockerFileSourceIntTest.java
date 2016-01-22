package io.kodokojo.commons.service.connector.git;

/*
 * #%L
 * commons-image-manager
 * %%
 * Copyright (C) 2016 Kodo-kojo
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import io.kodokojo.commons.model.StringToImageNameConverter;
import io.kodokojo.commons.service.connector.DockerFileSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class GitBashbrewDockerFileSourceIntTest {

    private String gitUrl = "git://github.com/kodokojo/acme";

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void fetch_kodokojo_busybox_dockerfile() throws IOException {
        String libraryPath = "bashbrew/library";
        File workspace = tmpFolder.newFolder();
        GitDockerFileProjectFetcher gitDockerFileProjectFetcher = new GitDockerFileProjectFetcher(workspace.getAbsolutePath());
        DockerFileSource dockerFileSource = new GitBashbrewDockerFileSource(workspace.getAbsolutePath(), null, gitUrl, libraryPath, gitDockerFileProjectFetcher);

        Set res = dockerFileSource.fetchDockerFile(StringToImageNameConverter.convert("kodokojo/busybox"));

        assertThat(res).isNotNull();
    }

    @Test
    public void fetch_kodokojo_busybox_specific_tag_dockerfile() throws IOException {
        String libraryPath = "bashbrew/library";
        File workspace = tmpFolder.newFolder();
        GitDockerFileProjectFetcher gitDockerFileProjectFetcher = new GitDockerFileProjectFetcher(workspace.getAbsolutePath());
        DockerFileSource dockerFileSource = new GitBashbrewDockerFileSource(workspace.getAbsolutePath(), null, gitUrl, libraryPath, gitDockerFileProjectFetcher);

        Set res = dockerFileSource.fetchDockerFile(StringToImageNameConverter.convert("kodokojo/busybox:1.0.0"));

        assertThat(res).isNotNull();
    }

    @Test
    public void fetch_all_dockerfile() throws IOException {
        String libraryPath = "bashbrew/library";

        File workspace = tmpFolder.newFolder();
        GitDockerFileProjectFetcher gitDockerFileProjectFetcher = new GitDockerFileProjectFetcher(workspace.getAbsolutePath());
        DockerFileSource dockerFileSource = new GitBashbrewDockerFileSource(workspace.getAbsolutePath(), null, gitUrl, libraryPath, gitDockerFileProjectFetcher);

        Set res = dockerFileSource.fetchAllDockerFile();

        assertThat(res).isNotNull();
    }

}