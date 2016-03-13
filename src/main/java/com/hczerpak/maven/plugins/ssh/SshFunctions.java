package com.hczerpak.maven.plugins.ssh;

import com.jcraft.jsch.JSchException;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;

public class SshFunctions {

    public static void deleteRemoteFile(String remoteFile, String user, String password, String host) throws MojoExecutionException {
        try (SshSession session = new SshSession(user, password, host)) {
            // exec 'rm remoteFile' remotely
            String command = "rm " + remoteFile;
            session.runCommand(command);
        }
    }

    /**
     * @param localFile
     * @param user
     * @param password
     * @param host       "raspberrypi" works fine, ip address is ok too
     * @param remoteFile Remote file wil land in home directory of the user
     * @throws JSchException
     * @throws IOException
     * @throws MojoExecutionException
     */
    public static void sendLocalFileToRemoteHost(String localFile, String user, String password, String host, String remoteFile) throws MojoExecutionException {
        try (SshSession session = new SshSession(user, password, host)) {
            session.sendFile(localFile, remoteFile);
        }
    }
}
