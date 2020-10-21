package listener;

import com.slender.Main;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {

    private final Main main;

    public DiscordListener(Main main) {
        this.main = main;
    }
    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
        event.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        System.out.println("Bot Aktif!");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getGuild() == null) return;
        if (event.getTextChannel().getIdLong() == main.channelId()) {main.sendMessageToMinecraft(event.getMessage());}
        if (event.getTextChannel().getIdLong() == main.staffChannelId()) {main.staffChatToMinecraft(event.getMessage());}
        super.onMessageReceived(event);
    }


}
