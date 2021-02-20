package fr.florent.editor.maven.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

@Mojo(name = "editor-build")
public class PluginMojo extends AbstractMojo {

    @Parameter(property = "lib.path")
    private String libPath = null;

    @Parameter(property = "module.path")
    private String modulePath = null;

    @Parameter(defaultValue = "${project}")
    private MavenProject project = null;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        copyDependencyLib();
        copyArtifact();

    }

    private void copyArtifact() {
        File buildFile = project.getArtifact().getFile();
        File buildDestinationFile = new File(this.modulePath, buildFile.getName());

        if (buildDestinationFile.exists()) {
            buildDestinationFile.delete();
        }

        try {
            FileUtils.copyFile(buildFile, buildDestinationFile);
            getLog().info(
                    String.format("Add module editor : %s",
                            buildDestinationFile.getAbsolutePath()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format(
                            "%s can be copy to %s",
                            buildFile.getName(),
                            this.modulePath
                    ),
                    e
            );
        }
    }

    private void copyDependencyLib() {
        for (Artifact artifact : project.getDependencyArtifacts()) {
            if ("compile".equals(artifact.getScope())) {
                File artifactFile = artifact.getFile();
                File artifactDestinationFile = new File(this.libPath, artifactFile.getName());

                if (!artifactDestinationFile.exists()) {
                    try {
                        FileUtils.copyFile(artifactFile, artifactDestinationFile);
                        getLog().info(
                                String.format("Add lib editor : %s",
                                        artifactDestinationFile.getAbsolutePath()
                                )
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(String.format("%s can be copy to %s", artifactFile.getName(), this.libPath), e);
                    }
                }
            }
        }
    }
}
