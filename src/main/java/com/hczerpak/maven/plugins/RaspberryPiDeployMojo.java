package com.hczerpak.maven.plugins;


import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@Mojo( name = "deploy" )
public class RaspberryPiDeployMojo extends AbstractMojo {

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
            manager.init();

            //Setup our SFTP configuration
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
            SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

            //Create the SFTP URI using the host name, userid, password,  remote path and file name

            String sftpUri = "sftp://" + username + ":" + password +  "@" + raspberrypi + "/" + remoteDirectory + "/raspberrypideploymojo.txt";

            File file = new File("testfile.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append("raspberry");
            fileWriter.close();

            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(sftpUri, opts);

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);

            getLog().info("Deploy to " + sftpUri + " successful");

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            manager.close();
        }
    }

}
