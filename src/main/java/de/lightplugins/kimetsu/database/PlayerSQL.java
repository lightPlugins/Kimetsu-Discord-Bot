package de.lightplugins.kimetsu.database;

import de.lightplugins.kimetsu.master.Main;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class PlayerSQL {

    private final String accountTable = "player";



    public CompletableFuture<Boolean> debugChar(String charName) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            Dotenv config = Dotenv.configure().load();

            String mapIndex = config.get("MAP_INDEX");
            int exitX = Integer.parseInt(config.get("EXIT_X"));
            int exitY = Integer.parseInt(config.get("EXIT_Y"));



            try {

                connection = Main.sqlPlayer.getConnection();

                ps = connection.prepareStatement(
                        "UPDATE " + accountTable + " SET map_index=?, exit_x=?, exit_y=? WHERE name=?");
                ps.setString(1, mapIndex);
                ps.setInt(2, exitX);
                ps.setInt(3, exitY);
                ps.setString(4, charName);
                ps.execute();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;

            } finally {
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
