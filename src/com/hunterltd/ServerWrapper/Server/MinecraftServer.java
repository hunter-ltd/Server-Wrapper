package com.hunterltd.ServerWrapper.Server;

import com.hunterltd.ServerWrapper.Utilities.UserSettings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinecraftServer {
    private Process serverProcess;
    private final ProcessBuilder pB;

    public MinecraftServer(String serverFolder, String serverFilename, int initialHeap, int maxHeap) {
        pB = new ProcessBuilder();
        pB.directory(new File(serverFolder));

        List<String> argsList = new ArrayList<>(Arrays.asList("java",
                String.format("-Xmx%dM", initialHeap),
                String.format("-Xms%dM", maxHeap),
                "-jar",
                Paths.get(serverFolder, serverFilename).toString(),
                "nogui"));

        if (UserSettings.hasExtraArgs()) argsList.addAll(3, UserSettings.getExtraArgs());

        pB.command(argsList.toArray(new String[0]));
    }

    public Process getServerProcess() {
        return serverProcess;
    }

    public void sendCommand(String cmd) throws IOException {
        if (!cmd.endsWith("\n")) {
            cmd += "\n";
        }
        OutputStream out = serverProcess.getOutputStream();
        out.write(cmd.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    public MinecraftServer run() throws IOException {
        serverProcess = pB.start();
        return this;
    }

    public void stop() throws IOException {
        sendCommand("stop");

        try {
            serverProcess.getOutputStream().close();
            serverProcess.getInputStream().close();
            serverProcess.getErrorStream().close();
        } catch (IOException ignored){}
    }

    public boolean isRunning() {
        return serverProcess.isAlive();
    }
}
