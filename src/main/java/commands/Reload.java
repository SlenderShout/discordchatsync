package commands;

import com.slender.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import util.ColorUtil;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (!sender.isOp()) return false;
        if (!(sender instanceof Player)) {return false;}
        if (cmd.getName().equalsIgnoreCase("sreload")) {
            Main.instance.reload();
            sender.sendMessage(ColorUtil.colorize("&4&lEklenti Yenilendi!"));
            return true;
        }
        return false;
    }
}
