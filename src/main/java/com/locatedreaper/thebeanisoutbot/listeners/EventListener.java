package com.locatedreaper.thebeanisoutbot.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.pagination.BanPaginationAction;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListener extends ListenerAdapter {
//private ArrayList<String> cooldown = new ArrayList<String>();
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();


        assert user != null;
        String message = user.getAsTag()
                + " reacted to a message with "
                + emoji
                + " in the "
                + channelMention
                + " channel."
                + "\n"
                /*+ jumpLink*/;
        TextChannel textChannel = event.getGuild().getTextChannelsByName("mute-this-channel",true).get(0);
        textChannel.sendMessage(message).queue();
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        assert user != null;
        String message = user.getAsTag()
                + " removed their reaction of "
                + emoji
                + " in the "
                + channelMention
                + " channel."
                + "\n"
                + jumpLink;
        TextChannel textChannel = event.getGuild().getTextChannelsByName("mute-this-channel",true).get(0);

        textChannel.sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        User user = event.getUser();
        String message = user.getAsTag() + " has departed from the guild. Farewell my friend!";
        TextChannel textChannel = event.getGuild().getTextChannelsByName("mute-this-channel", true).get(0);
        textChannel.sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        User user = event.getUser();
        String guild = event.getGuild().getName();
        String rules = event.getGuild().getRulesChannel().getAsMention();

        String message = "Welcome to " + guild + " " + user.getAsMention() + "!" + "\nPlease read the " + rules + " channel to get started!";
        TextChannel textChannel = event.getGuild().getTextChannelsByName("mute-this-channel", true).get(0);
        textChannel.sendMessage(message).queue();
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        User user = event.getUser();
        TextChannel textChannel = event.getGuild().getTextChannelsByName("welcome", true).get(0);
        String message = user + "Has been banned from the guild.";
        textChannel.sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        User user = event.getUser();
//        String oldName = event.getOldNickname();
        String newName = event.getNewNickname();

        String message = "**" + user.getName() + "** changed their name to " + newName;
        TextChannel textChannel = event.getGuild().getTextChannelsByName("mute-this-channel", true).get(0);
//        TextChannel channelTwo = event.getGuild().getTextChannelsByName("mod_updates", true).get(0);
        textChannel.sendMessage(message).queue(); //channelTwo.sendMessage(message).queue();
    }

//    @Override
//    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
//        List<Member> members = event.getGuild().getMembers();
//        int onlineMembers = 0;
//        for (Member member : members) {
//            if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
//                onlineMembers++;
//            }
//        }
//
//        User user = event.getUser();
//        String oldStatus = event.getOldOnlineStatus().getKey();
//        String newStatus = event.getNewOnlineStatus().getKey();
//
//        if (newStatus == "dnd") {
//            newStatus = "do not disturb";
//        } else if (oldStatus == "dnd") {
//            oldStatus = "do not disturb";
//        }
//
//        String message = "**" + user.getAsTag() + "** updated their online status from " + oldStatus + " to " + newStatus + "! There are now " + onlineMembers + " online users in this guild.";
//        TextChannel textChannel = event.getGuild().getTextChannelsByName("mute-this-channel", true).get(0);
//        textChannel.sendMessage(message).queue();
//    }
/*
    @Override
    public void onReady(@NotNull ReadyEvent event ) {
        String message = "Good morning <@707370051547955303>!";
        TextChannel textChannel = event.getJDA().getGuildById(1047676216607522856L).getTextChannelsByName("mute-this-channel", true).get(0);
        textChannel.sendMessage(message).queue();
    }
*/
    //    @Override
//    public void onGenericMessage(@NotNull GenericMessageEvent event) {
//        User user = event.getJDA().getUserById(456594340148674562L);
//        event.getGuild().retrieveBan(user).queue();
//    }
//
//    @Override
//    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
//        List<Member> members = event.getGuild().getMembers();
//        User user = (User) event.getJDA();
//
//        String message =  user.getAsTag() + "has departed from the guild. Farewell my friend!";
//        TextChannel textChannel = event.getGuild().getTextChannelsByName("welcome", true).get(0);
//        textChannel.sendMessage(message).queue();
//    }
}
