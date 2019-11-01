package ca.fireball1725.mods.firelib2.util;

import ca.fireball1725.mods.firelib2.FireLib2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import sun.rmi.log.LogHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerHelper {
  private static HashMap<UUID, vipLevel> uuidVip = new HashMap<>();
  private static final Gson GSON = new Gson();

  public static void loadVipPlayers() {
    uuidModel[] playerData = getVIPList("https://gist.githubusercontent.com/FireBall1725/a77b4378dfda7ca4d03a999168065a05/raw/fb2e9f9d3f15833a000d5e8b3df71efaa8094a00/supporters.json");
    for (int i = 0; i < playerData.length; i++) {
      uuidVip.put(playerData[i].uuid, playerData[i].level);
    }
  }

  private static uuidModel[] getVIPList(String url) {
    try {
      URLConnection connection = new URL(url).openConnection();
      InputStream inputStream = connection.getInputStream();
      String encoding = connection.getContentEncoding();
      String body = IOUtils.toString(inputStream, encoding);
      return GSON.fromJson(body, uuidModel[].class);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return new uuidModel[]{};
  }

  private static class uuidModel {
    private UUID uuid;
    private vipLevel level;

    public UUID getPlayerUUID() {
      return uuid;
    }

    public vipLevel getPlayerLevel() {
      return level;
    }
  }

  public static enum vipLevel {
    VIP,
    SUBSCRIBER,
    MODERATOR,
    ADMIN
  }

  public static vipLevel getPlayer(UUID playerUUID) {
    return uuidVip.get(playerUUID);
  }

  public static boolean isPlayerVIP(UUID playerUUID) {
    return uuidVip.containsKey(playerUUID);
  }
}
