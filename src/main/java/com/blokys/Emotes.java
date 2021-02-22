package com.blokys;

import com.blokys.bttv.Emote;
import net.runelite.api.Client;
import net.runelite.api.IndexedSprite;
import net.runelite.client.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Emotes {
    public static Integer findModIconKey(String needle, Client client) {
        List<Emote> emotes = Store.getBttvEmotes();

        for (int i = 0; i < emotes.size(); i++) {
            Emote emote = emotes.get(i);

            boolean doesEmoteMatch = needle.toLowerCase().contains(
                    emote.getCode().toLowerCase()
            );

            if (!doesEmoteMatch) continue;

            IndexedSprite sprite;

            if (Store.getImages().get(emote.getCode()) != null) {
                sprite = Store.getImages().get(emote.getCode());
            } else {
                try {
                    URL url = new URL("https://cdn.betterttv.net/emote/" + emote.getId() + "/1x." + emote.getImageType());
                    BufferedImage img = ImageUtil.toARGB(ImageIO.read(url));
                    sprite = ImageUtil.getImageIndexedSprite(ImageUtil.resizeImage(img, 14, 14), client);
                    Store.addItemToImages(emote.getCode(), sprite);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            if (sprite == null) continue;

            IndexedSprite[] modIcons = client.getModIcons();
            final IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, Store.getDefaultModIconsLength() + emotes.size());

            Integer newKey = Store.getDefaultModIconsLength() + i;
            newModIcons[newKey] = sprite;
            client.setModIcons(newModIcons);

            return newKey;
        }

        return null;
    }
}
