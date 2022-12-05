package com.locatedreaper.thebeanisoutbot;

import com.locatedreaper.thebeanisoutbot.commands.CommandManager;
import com.locatedreaper.thebeanisoutbot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


import javax.security.auth.login.LoginException;

public class BeanBot {

    private final ShardManager shardManager;
    private final Dotenv config;

    public Dotenv getConfig() {
        return config;
    }

    public BeanBot() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        String psw = config.get("PW");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        builder.setStatus(OnlineStatus.INVISIBLE);
        builder.setActivity(Activity.watching("AnthiosLive"));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ACTIVITY,CacheFlag.ONLINE_STATUS,CacheFlag.CLIENT_STATUS);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListener(), new CommandManager());
    }



    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
       try {
           BeanBot bot = new BeanBot();
       } catch (LoginException e) {
           System.out.println("ERROR: Provided bot token is invalid!");
       }
    }
}
