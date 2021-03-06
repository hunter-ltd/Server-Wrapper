package com.hunterltd.ssw.utilities;

import com.dablenparty.jsondata.UserDataObject;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class Settings extends UserDataObject {
    public Settings(String appName) throws IOException {
        super(appName);
    }

    public Settings(Path settingsPath) throws IOException {
        super(settingsPath);
    }

    @Override
    public void populateDefaults() {
        // Tab 1
        this.put("memory", 1024);
        this.put("extraArgs", new JSONArray());

        //Tab 2
        this.put("autoRestart", false);
        this.put("restartInterval", 1);
    }

    public int getMemory() {
        try {
            return ((Long) this.get("memory")).intValue();
        } catch (ClassCastException e) {
            return (int) this.get("memory");
        }
    }

    public void setMemory(int value) {
        this.replace("memory", value);
    }

    public boolean getRestart() {
        return (boolean) this.get("autoRestart");
    }

    public void setRestart(boolean value) {
        this.replace("autoRestart", value);
    }

    public List<String> getExtraArgs() {
        return (List<String>) this.get("extraArgs");
    }

    public void setExtraArgs(String[] newArgs) {
        JSONArray args = new JSONArray();
        args.addAll(Arrays.asList(newArgs));
        this.replace("extraArgs",  args);
    }

    public boolean hasExtraArgs() {
        List<String> list = ((List<String>) this.get("extraArgs"));
        return list.size() > 0 && !list.get(0).equalsIgnoreCase("");
    }

    public int getInterval() {
        // Sometimes it parses as a Long, sometimes it parses as an Integer
        // I haven't found a pattern to identify which it chooses (yet)
        try {
            return ((Long) this.get("restartInterval")).intValue();
        } catch (ClassCastException e) {
            return (int) this.get("restartInterval");
        }
    }

    public void setInterval(int value) {
        this.replace("restartInterval", value);
    }
}
