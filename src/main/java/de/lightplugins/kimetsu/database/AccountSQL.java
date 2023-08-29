package de.lightplugins.kimetsu.database;

import de.lightplugins.kimetsu.master.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class AccountSQL {

    private final String accountTable = "account";


    public CompletableFuture<Boolean> updateCoins(int coins, String inGameName) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                ps = connection.prepareStatement("UPDATE " + accountTable + " SET coins=? WHERE login=?");
                ps.setInt(1, coins);
                ps.setString(2, inGameName);
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

    public CompletableFuture<Integer> getCoins(String loginName) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                ps = connection.prepareStatement("SELECT * FROM " + accountTable + " WHERE login=?");


                ResultSet rs = ps.executeQuery();

                if(rs.next()) {
                    return rs.getInt("coins");
                }

                return null;

            } catch (SQLException e) {
                e.printStackTrace();
                return null;

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
