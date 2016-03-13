package com.hczerpak.maven.plugins.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.*;
import java.util.Objects;

/**
 * A facade before JSch complicated API. Why is it so complex?
 * <p>
 * Created by Hubert Czerpak on 13/03/2016
 * using 11" MacBook (can't see sh*t on this screen).
 */
public class SshSession implements AutoCloseable {

    private final Session session;

    public SshSession(String user, String password, String host) throws MojoExecutionException {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            throw new MojoExecutionException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
        }

    }

    @Override
    public void close() throws MojoExecutionException {
        session.disconnect();
    }

    public void runCommand(String command) throws MojoExecutionException {
        Objects.requireNonNull(command);

        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            channel.connect();

            try (InputStream in = channel.getInputStream()) {
                if (in.available() > 0 && in.read() != 0)
                    throw new MojoExecutionException("Command execution failed when tried to run: " + command);
            }

            channel.disconnect();
        } catch (IOException | JSchException e) {
            throw new MojoExecutionException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
        }
    }

    public void sendFile(String localFile, String remoteFile) throws MojoExecutionException {
        Objects.requireNonNull(localFile);
        Objects.requireNonNull(remoteFile);

        try {
            // exec 'scp -t rfile' remotely
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            String command = "scp " + " -t " + remoteFile;
            channel.setCommand(command);

            // get I/O streams for remote scp
            try (InputStream in = channel.getInputStream(); OutputStream out = channel.getOutputStream()) {
                channel.connect();

                if (in.available() > 0 && in.read() != 0)
                    throw new MojoExecutionException("Command execution failed when tried to run: " + command);

                File file = new File(localFile);

                if (!file.exists())
                    throw new MojoExecutionException("File not found: " + file.getAbsolutePath());

                // send "C0644 filesize filename", where filename should not include '/'
                command = "C0644 " + file.length() + " " + (localFile.lastIndexOf('/') > 0 ? localFile.substring(localFile.lastIndexOf('/') + 1) : localFile) + "\n";

                out.write(command.getBytes());
                out.flush();

                // send a content of localFile
                try (FileInputStream fileInputStream = new FileInputStream(localFile)) {
                    byte[] buf = new byte[1024];
                    while (true) {
                        int len = fileInputStream.read(buf, 0, buf.length);
                        if (len <= 0) break;
                        out.write(buf, 0, len);
                    }
                    // send '\0'
                    buf[0] = 0;
                    out.write(buf, 0, 1);
                    out.flush();

                    if (in.available() > 0 && in.read() != 0)
                        throw new MojoExecutionException("Upload failed: " + localFile);
                }
                Thread.sleep(1000); //FIXME WHY. IS. THIS. LINE. REQUIRED. TO. MAKE. IT. WORK??????
            }
            channel.disconnect();
        } catch (InterruptedException | IOException | JSchException e) {
            throw new MojoExecutionException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
        }
    }
}
