package com.hczerpak.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

/**
 * Created by Hubert Czerpak on 06/03/2016 using 11" MacBook
 *
 * NOTE: can't see sh*t on this screen.
 */
public class RaspberryPiCleanMojo extends AbstractMojo {

    @Parameter( property = "raspberrypi", defaultValue = "raspberrypi" )
    private String raspberrypi;

    @Parameter( property = "username", defaultValue = "pi" )
    private String username;

    @Parameter( property = "password", defaultValue = "raspberry" )
    private String password;

    @Parameter( property = "remoteDirectory", defaultValue = "~/${project.version}/target")
    private String remoteDirectory;

    public void execute() throws MojoExecutionException
    {
        //correct configuration required
        assert (raspberrypi != null);
        assert (username != null);
        assert (password != null);
        assert (remoteDirectory != null);

        StandardFileSystemManager manager = new StandardFileSystemManager();
        try {
            //Initializes the file manager
            manager.init();

            //Setup our SFTP configuration
            FileSystemOptions options = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(options, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, true);
            SftpFileSystemConfigBuilder.getInstance().setTimeout(options, 10000);

            String targetToClean = "sftp://" + username + ":" + password +  "@" + raspberrypi + "/" + remoteDirectory;

            //Create remote file object
            FileObject remoteFile = manager.resolveFile(targetToClean, options);

            //Check if the file exists
            if (remoteFile.exists()) {
                remoteFile.delete();
                getLog().info("RaspberryPi's has been cleaned.");
            } else {
                getLog().info("Nothing to clean on RaspberryPi. Target location (" + remoteDirectory + ") not found.");
            }
        } catch (Exception e) {
            getLog().error("Exception during cleaning: " + e);
        } finally {
            manager.close();
        }
    }

}
