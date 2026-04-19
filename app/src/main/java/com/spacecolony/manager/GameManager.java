package com.spacecolony.manager;

import com.spacecolony.colony.Storage;

public class GameManager {

    private static GameManager instance;

    private Storage storage;
    private MissionControl missionControl;

    private GameManager() {
        storage = new Storage();
        missionControl = new MissionControl(storage);
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }

    public MissionControl getMissionControl() {
        return missionControl;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
        this.missionControl = new MissionControl(storage);
    }

    public void saveGame(android.content.Context context) {
        storage.saveToFile(context);
    }

    public void loadGame(android.content.Context context) {
        Storage loaded = Storage.loadFromFile(context);
        setStorage(loaded);
    }

    public boolean hasSave(android.content.Context context) {
        try {
            context.openFileInput("save.json").close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}