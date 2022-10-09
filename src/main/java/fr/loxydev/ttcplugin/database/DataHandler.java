package fr.loxydev.ttcplugin.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import org.bukkit.Bukkit;

import java.sql.*;

public abstract class DataHandler {

    protected String table;
    protected String prim_key;
    protected Object prim_key_value;

    public static MysqlDataSource connect(DbCredentials credentials) throws SQLException {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();

        assert credentials != null;
        dataSource.setServerName(credentials.getHost());
        dataSource.setPortNumber(credentials.getPort());
        dataSource.setDatabaseName(credentials.getDatabase());
        dataSource.setUser(credentials.getUser());
        dataSource.setPassword(credentials.getPassword());

        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1)) {
                throw new SQLException("Could not establish database connection.");
            }
        }

        return dataSource;
    }

    public String getString(String col) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT " + col + " FROM " + table + " WHERE " + prim_key + " = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setObject(1, prim_key_value);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.first()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve data from " + table + ": " + prim_key_value.toString());
        }
        return null;
    }

    public int getInt(String col) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT " + col + " FROM " + table + " WHERE " + prim_key + " = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setObject(1, prim_key_value);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.first()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve data from " + table + ": " + prim_key_value.toString());
        }
        return -1;
    }

    public boolean getBoolean(String col) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT " + col + " FROM " + table + " WHERE " + prim_key + " = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setObject(1, prim_key_value);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.first()) {
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve data from " + table + ": " + prim_key_value.toString());
        }

        return false; // May find a better way to exit in case of error...
    }

    public boolean pushUpdate(String field, Object value) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "UPDATE " + table + " SET " + field + " = ? WHERE " + prim_key + " = ?")) {
            stmt.setObject(1, value);
            stmt.setObject(2, prim_key_value);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not push update of " + table + ": " + prim_key_value.toString());
        }
        return false;
    }

    public boolean exists() {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM " + table + " WHERE " + prim_key + " = ?"
        )) {
            stmt.setObject(1, prim_key_value);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            Bukkit.getLogger().info(e.toString());
        }

        return false;
    }
}
