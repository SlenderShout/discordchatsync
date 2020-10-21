package listener;

import com.slender.Main;
import commands.StaffChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import util.ColorUtil;

public class BukkitListener implements Listener {
    StaffChat sc = Main.getInstance().sc;
    private final Main main;
    public BukkitListener(Main main) {
        this.main = main;
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (sc.yetkilisohbet.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
            main.staffChatToDiscord(event.getPlayer(), event.getMessage());
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("laternad.yetkili")) {
                    player.sendMessage(ColorUtil.colorize("&cYetkili Sohbet &7› &6"+event.getPlayer().getName()+"&a "+event.getMessage()));
                }
            }
        }else {
            main.sendMessageToDiscord(event.getPlayer(), event.getMessage());
        }
    }

}
