package com.slender;

import commands.Reload;
import commands.StaffChat;
import listener.BukkitListener;
import listener.DiscordListener;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import util.ColorUtil;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {
    FileConfiguration config = getConfig();
    File msgfolder = new File(this.getDataFolder() + File.separator+ "Messages");
    File msgyml = new File(msgfolder + File.separator +"messages.yml");
    FileConfiguration msgconfig = YamlConfiguration.loadConfiguration(msgyml);
    File discordfolder = new File(this.getDataFolder() + File.separator+ "Discord");
    File discordyml = new File(discordfolder + File.separator +"discord.yml");
    FileConfiguration discordConfig = YamlConfiguration.loadConfiguration(discordyml);
    public static Main instance;
    private JDA jda;
    public StaffChat sc;
    public Reload rl;
    private final long channelId = Long.parseLong(String.valueOf(discordConfig.getLong("Discord.Bot.ChatId")));
    private final long staffChannelId = Long.parseLong(String.valueOf(discordConfig.getLong("Discord.Bot.StaffChatId")));
    private final static Map<String, String> colors = new HashMap<>();
    public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Config() {
        config.options().copyDefaults(true);
        config.addDefault("DiscordBot", true);
        config.addDefault("DateFormat", "dd/MM/yyyy HH.mm");
        colorList();
        config.createSection("Colors", colors);
        saveConfig();
        discordConfig.options().header("#Discord Botu ile alakalı tüm ayarların bulunduğu yml dosyası");
        discordConfig.addDefault("Discord.Bot.Token", "");
        discordConfig.addDefault("Discord.Bot.StaffChatId", "");
        discordConfig.addDefault("Discord.Bot.ChatId", "");
        discordConfig.options().copyDefaults(true);
        saveCustomYml(discordConfig, discordyml);
        msgconfig.options().copyDefaults(true);
        msgconfig.options().header("#Mesajların bulunduğu dosya");
        msgconfig.addDefault("Messages.sendMessageToDiscord", "(%date) **%name**: %message");
        saveCustomYml(msgconfig, msgyml);
    }
    @Override
    public void onLoad() {
        if (!(getConfig().getBoolean("DiscordBot"))) return;
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(discordConfig.getString("Discord.Bot.Token"))
                    .addEventListeners(new DiscordListener(this))
                    .build();
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
        }catch(LoginException e) {
            e.printStackTrace();
        }

        super.onLoad();
    }
    @Override
    public void onEnable() {
        instance = this;
        Config();
        commands();
        Bukkit.getPluginManager().registerEvents(new BukkitListener(this), this);
    }

    public void reload() {
        reloadConfig();
        saveConfig();
    }

    private void commands(){
        sc = new StaffChat();
        rl = new Reload();
        getCommand("yetkilisohbet").setExecutor(sc);
        //getCommand("sreload").setExecutor(rl);
    }

    @Override
    public void onDisable() {
        jda.shutdown();
    }

    public JDA getJda() {
        return jda;
    }
    public long channelId() {
        return channelId;
    }
    public long staffChannelId() {
        return staffChannelId;
    }
    private void colorList() {
        colors.put("99AAB5", "&f");
        colors.put("1ABC9C", "&a");
        colors.put("2ECC71", "&a");
        colors.put("3498DB", "&3");
        colors.put("9B59B6", "&5");
        colors.put("E91E63", "&d");
        colors.put("F1C40F", "&e");
        colors.put("E67E22", "&6");
        colors.put("E74C3C", "&c");
        colors.put("95A5A6", "&7");
        colors.put("607D8B", "&8");
        colors.put("11806A", "&2");
        colors.put("1F8B4C", "&2");
        colors.put("206694", "&1");
        colors.put("71368A", "&5");
        colors.put("AD1457", "&d");
        colors.put("C27C0E", "&6");
        colors.put("A84300", "&6");
        colors.put("992D22", "&4");
        colors.put("979C9F", "&7");
        colors.put("546E7A", "&8");
    }

    public static Role getTopRole(Member member) {
        return member.getRoles().size() != 0 ? member.getRoles().get(0) : null;
    }

    public static Role getTopRoleWithCustomColor(Member member) {
        for (Role role : member.getRoles()) if (role.getColor() != null) return role;
        return null;
    }

    public static String convertRoleToMinecraftColor(Role role) {
        if (role == null) {
            return "";
        }

        String hex = role.getColor() != null ? Integer.toHexString(role.getColor().getRGB()).toUpperCase() : "99AAB5";
        if (hex.length() == 8) hex = hex.substring(2);
        String translatedColor = colors.get(hex);

        if (translatedColor == null) {
            translatedColor = "";
        }

        return translatedColor;
    }
    public void sendMessageToMinecraft(Message message) {
        String member = message.getMember().getEffectiveName();
        if (getTopRole(message.getMember()) != null) {
            Bukkit.broadcastMessage(ColorUtil.colorize("&b[Discord] › "+convertRoleToMinecraftColor(getTopRoleWithCustomColor(message.getMember()))+getTopRole(message.getMember()).getName()+"&r &6"+member+ " › &7"+message.getContentDisplay()));
        }else{
            Bukkit.broadcastMessage(ColorUtil.colorize("&b[Discord] › &6"+member+ " › &7"+message.getContentDisplay()));
        }
    }

    public void staffChatToMinecraft(Message message) {
        String member = message.getMember().getEffectiveName();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (sc.yetkilisohbet.contains(player.getName())) {
                if (getTopRole(message.getMember()) != null) {
                    player.sendMessage(ColorUtil.colorize("&b[Discord] &cYetkili Sohbet › "+convertRoleToMinecraftColor(getTopRoleWithCustomColor(message.getMember()))+getTopRole(message.getMember()).getName()+"&r &6"+member+ " › &7"+message.getContentDisplay()));
                }else
                    player.sendMessage(ColorUtil.colorize("&b[Discord] &cYetkili Sohbet › &6"+member+ " › &7"+message.getContentDisplay()));
            }
        }

    }


    public static Main getInstance() {
        return instance;
    }

    public void staffChatToDiscord(Player player, String message) {
        if (message.startsWith("@")) return;
        String p = player.getDisplayName();
        jda.getTextChannelById(staffChannelId).sendMessage("Yetkili › **"+p+ "** **› ** " + message).queue();
    }



    public void sendMessageToDiscord(Player player, String message) {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat(getConfig().getString("DateFormat"));
        if (message.startsWith("@")) return;
        String p = player.getDisplayName();
        String text = getConfig().getString("Messages.sendMessageToDiscord");
        text = text.replaceAll("%date", String.valueOf(format));
        text =text.replaceAll("%name", p);
        text= text.replaceAll("%message", message);
        jda.getTextChannelById(channelId).sendMessage(text).queue();
    }

}