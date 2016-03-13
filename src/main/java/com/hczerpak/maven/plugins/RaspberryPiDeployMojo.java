package com.hczerpak.maven.plugins;


import com.hczerpak.maven.plugins.ssh.SshFunctions;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "deploy")
public class RaspberryPiDeployMojo extends AbstractMojo {

    @Parameter(property = "raspberrypi", defaultValue = "raspberrypi")
    private String raspberrypi;

    @Parameter(property = "username", defaultValue = "pi")
    private String username;

    @Parameter(property = "password", defaultValue = "raspberry")
    private String password;

    @Parameter(property = "jar-filename", required = true)
    private String jarFileName;

    public void execute() throws MojoExecutionException {
        //correct configuration required
        assert (raspberrypi != null);
        assert (username != null);
        assert (password != null);
        assert (jarFileName != null);

        SshFunctions.sendLocalFileToRemoteHost(jarFileName, username, password, raspberrypi, jarFileName);

        getLog().info("Deploy " + jarFileName + " to " + username + "@" + raspberrypi + " successful");
    }

}
