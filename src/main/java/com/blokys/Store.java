package com.blokys;

import com.blokys.bttv.Emote;
import net.runelite.api.IndexedSprite;

import java.util.HashMap;
import java.util.List;

public class Store {
    private static List<Emote> bttvEmotes;
    private static HashMap<String, IndexedSprite> images = new HashMap<>();
    private static Integer defaultModIconsLength = -1;

    public static void setBttvEmotes(List<Emote> bttvEmotes) {
        Store.bttvEmotes = bttvEmotes;
    }

    public static List<Emote> getBttvEmotes() {
        return bttvEmotes;
    }

    public static HashMap<String, IndexedSprite> getImages() {
        return images;
    }

    public static void setImages(HashMap<String, IndexedSprite> images) {
        Store.images = images;
    }

    public static void addItemToImages(String key, IndexedSprite image) {
        Store.images.put(key, image);
    }

    public static void setDefaultModIconsLength(Integer defaultModIconsLength) {
        Store.defaultModIconsLength = defaultModIconsLength;
    }

    public static Integer getDefaultModIconsLength() {
        return defaultModIconsLength;
    }
}
