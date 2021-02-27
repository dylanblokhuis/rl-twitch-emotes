package com.blokys;

import com.blokys.fetch.Emote;
import net.runelite.api.IndexedSprite;

import java.util.HashMap;
import java.util.List;

public class Store {
    private static List<Emote> emotes;
    private static HashMap<String, IndexedSprite> images = new HashMap<>();
    private static Integer defaultModIconsLength = -1;

    public static List<Emote> getEmotes()
    {
        return emotes;
    }

    public static void setEmotes(List<Emote> emotes)
    {
        Store.emotes = emotes;
    }

    public static HashMap<String, IndexedSprite> getImages()
    {
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
