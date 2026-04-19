package com.spacecolony.manager;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.spacecolony.colony.Storage;

public class GameViewModel extends ViewModel {
    private Storage storage;
    private MissionControl missionControl;

    public void init(Context context) {
        if (storage == null) {
            storage = GameDataManager.loadGame(context);
            missionControl = new MissionControl(storage);
        }
    }

    public Storage getStorage() {
        return storage;
    }

    public MissionControl getMissionControl() {
        return missionControl;
    }

    public void saveGame(Context context) {
        GameDataManager.saveGame(context, storage);
    }
}