package de.lightplugins.kimetsu.database;

import de.lightplugins.kimetsu.master.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;


public class AccountSQL {

    private final String accountTable = "account";
    private final String hwidTable = "hwid_bans";


    public CompletableFuture<Boolean> updateCoins(int coins, String loginName) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                ps = connection.prepareStatement("UPDATE " + accountTable + " SET coins=? WHERE login=?");
                ps.setInt(1, coins);
                ps.setString(2, loginName);
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
                ps.setString(1, loginName);


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

    public CompletableFuture<Boolean> userExist(String loginName) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                ps = connection.prepareStatement("SELECT login FROM " + accountTable + " WHERE login=?");
                ps.setString(1, loginName);


                ResultSet rs = ps.executeQuery();

                return rs.next();

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

    public CompletableFuture<Boolean> hwidAlreadyExist(String hwid) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                ps = connection.prepareStatement("SELECT * FROM " + hwidTable + " WHERE hwid=?");
                ps.setString(1, hwid);


                ResultSet rs = ps.executeQuery();

                return rs.next();

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

    public CompletableFuture<String> getUserHwid(String loginName) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                ps = connection.prepareStatement("SELECT * FROM " + accountTable + " WHERE login=?");
                ps.setString(1, loginName);


                ResultSet rs = ps.executeQuery();

                if(rs.next()) {
                    return rs.getString("hwid");
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

    public CompletableFuture<Boolean> createHwidBan(String hwid) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                // Maybe im Statement noch IGNORE hinzufügen
                ps = connection.prepareStatement("INSERT INTO " + hwidTable + " (hwid) VALUES (?)");
                ps.setString(1, hwid);
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

    public CompletableFuture<Boolean> updateStatus(String newStatus, String loginName) {

        return CompletableFuture.supplyAsync(() -> {

            Connection connection = null;
            PreparedStatement ps = null;

            try {

                connection = Main.sqlAccount.getConnection();

                ps = connection.prepareStatement("UPDATE " + accountTable + " SET status=? WHERE login=?");
                ps.setString(1, newStatus);
                ps.setString(2, loginName);
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
