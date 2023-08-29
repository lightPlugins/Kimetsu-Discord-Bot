package de.lightplugins.kimetsu.master;

import com.zaxxer.hikari.HikariDataSource;
import de.lightplugins.kimetsu.commands.CoinsCommand;
import de.lightplugins.kimetsu.database.Connection;
import de.lightplugins.kimetsu.listener.Events;
import de.lightplugins.kimetsu.listener.RegisterCommands;
import de.lightplugins.kimetsu.listener.Startup;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


/*
 * Java Discord Bot for Kimetsu
 *
 * @author: lightPlugins™
 * @version: 1.0.0
 *
 */

public class Main {

    public static Main getInstance;

    private final ShardManager shardManager;
    private final Dotenv config;

    public static HikariDataSource sqlAccount;
    public static HikariDataSource sqlPlayer;

    public Main() {

        System.out.println("[KIMETSU] Enable dotenv ...");
        config = Dotenv.configure().load();

        System.out.println("[KIMETSU] Building shard manager ...");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN"));
        System.out.println("[KIMETSU] Set bot status to online");
        builder.setStatus(OnlineStatus.valueOf(config.get("BOT_STATUS")));
        System.out.println("[KIMETSU] Set bot activity to 'Playing "
                + config.get("BOT_ACTIVITY_NAME").toLowerCase() + "'");
        builder.setActivity(Activity.playing(config.get("BOT_ACTIVITY_NAME")));
        System.out.println("[KIMETSU] Enable intents ...");
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES
        );
        System.out.println("[KIMETSU] Enable cache policy ...");
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        System.out.println("[KIMETSU] Enable chunky filter ...");
        builder.setChunkingFilter(ChunkingFilter.ALL);
        System.out.println("[KIMETSU] Enable cache ...");
        builder.enableCache(
                CacheFlag.ONLINE_STATUS,
                CacheFlag.ROLE_TAGS
        );
        System.out.println("[KIMETSU] Finish building ...");
        shardManager = builder.build();
        System.out.println("[KIMETSU] Shard manager successfully built");

        /*  Register Events  */

        System.out.println("[KIMETSU] Register events ...");
        shardManager.addEventListener(
                new Events(),
                new Startup(),
                new RegisterCommands(),
                new CoinsCommand());

    }


    public static void main(String[] args) {
        System.out.println(" ");
        System.out.println("############# KIMETSU #############");
        System.out.println(" ");
        System.out.println("Author: lightPlugins");
        System.out.println("JDA: 5.0.0.beta.13");
        System.out.println("Database: HikariCP");
        System.out.println("Bot-version: 1.0.0");
        System.out.println("JDK: 20");
        System.out.println(" ");
        System.out.println("############# KIMETSU #############");
        System.out.println(" ");
        System.out.println("[KIMETSU] Starting bot ...");

        /*  Loading environment variables and builds the bot shard manager  */
        Main main = new Main();

        /*  Database connection for mysql  */
        sqlAccount = new Connection().connectToDataBaseViaMariaDB("account");
        sqlPlayer = new Connection().connectToDataBaseViaMariaDB("player");

        System.out.println("[KIMETSU] Database successfully connected via mysql");
        System.out.println("[KIMETSU] Starting bot successfully");
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Dotenv getConfig() {
        return config;
    }


}