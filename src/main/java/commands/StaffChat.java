package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

public class StaffChat implements CommandExecutor {

    public List<String> yetkilisohbet = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {return false;}
        if (cmd.getName().equalsIgnoreCase("yetkilisohbet")) {
            if (sender.hasPermission("discord.yetkili")) {
                if (!(yetkilisohbet.contains(sender.getName()))) {
                    yetkilisohbet.add(sender.getName());
                    sender.sendMessage(ColorUtil.colorize("&6&lL &7› &aYetkili Sohbet Açık."));
                    return true;
                }else {
                    yetkilisohbet.remove(sender.getName());
                    sender.sendMessage(ColorUtil.colorize("&6&lL &7› &cYetkili Sohbet Kapalı."));
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
