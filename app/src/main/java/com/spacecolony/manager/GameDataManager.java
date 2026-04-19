package com.spacecolony.manager;

import android.content.Context;
import com.spacecolony.colony.Storage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameDataManager {
    public static void saveGame(Context context, Storage storage) {
        try {
            FileOutputStream fos = context.openFileOutput("game_save.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storage);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Storage loadGame(Context context) {
        try {
            FileInputStream fis = context.openFileInput("game_save.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Storage storage = (Storage) ois.readObject();
            ois.close();
            fis.close();
            return storage;
        } catch (Exception e) {
            return new Storage();
        }
    }
}