package moe.luoluo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.sql.*;

public class Database {
    public static void initialize() {
        String url = "jdbc:sqlite:data/moe.luoluo/luohyp.db";
        File dbFile = new File("data/moe.luoluo/luohyp.db");
        File parentDir = dbFile.getAbsoluteFile().getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            // 创建所有缺失的父目录
            parentDir.mkdirs();
        }

        try (Connection conn = DriverManager.getConnection(url)) {
            // 创建表
            String createSQL = "CREATE TABLE IF NOT EXISTS players ("
                    + "uuid TEXT PRIMARY KEY,"
                    + "time INTEGER NOT NULL,"
                    + "data TEXT NOT NULL)";
            executeStatement(conn, createSQL);

            createSQL = "CREATE TABLE IF NOT EXISTS guilds ("
                    + "id TEXT PRIMARY KEY,"
                    + "time INTEGER NOT NULL,"
                    + "data TEXT NOT NULL)";
            executeStatement(conn, createSQL);

            createSQL = "CREATE TABLE IF NOT EXISTS status ("
                    + "uuid TEXT PRIMARY KEY,"
                    + "time INTEGER NOT NULL,"
                    + "data TEXT NOT NULL)";
            executeStatement(conn, createSQL);

            createSQL = "CREATE TABLE IF NOT EXISTS others ("
                    + "info TEXT PRIMARY KEY,"
                    + "time INTEGER NOT NULL,"
                    + "data TEXT NOT NULL)";
            executeStatement(conn, createSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeStatement(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void upsert(String type, String uuid, String data) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:data/moe.luoluo/luohyp.db");
        String sql = switch (type) {
            case "player" -> "INSERT INTO players (uuid, time, data) VALUES (?, ?, ?) " +
                    "ON CONFLICT(uuid) DO UPDATE SET time = excluded.time, data = excluded.data";
            case "guild" -> "INSERT INTO guilds (id, time, data) VALUES (?, ?, ?) " +
                    "ON CONFLICT(id) DO UPDATE SET time = excluded.time, data = excluded.data";
            case "status" -> "INSERT INTO status (uuid, time, data) VALUES (?, ?, ?) " +
                    "ON CONFLICT(uuid) DO UPDATE SET time = excluded.time, data = excluded.data";
            case "leaderboards", "counts" -> "INSERT INTO others (info, time, data) VALUES (?, ?, ?) " +
                    "ON CONFLICT(info) DO UPDATE SET time = excluded.time, data = excluded.data";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setLong(2, System.currentTimeMillis());
            pstmt.setString(3, data);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ApiResult queryData(String type, String uuid) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:data/moe.luoluo/luohyp.db");
        PreparedStatement pstmt = conn.prepareStatement(switch (type) {
            case "player" -> "SELECT * FROM players WHERE uuid = ?";
            case "guild" -> "SELECT * FROM guilds WHERE id = ?";
            case "status" -> "SELECT * FROM status WHERE uuid = ?";
            case "leaderboards", "counts" -> "SELECT * FROM others WHERE info = ?";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        });
        try {
            // 设置查询参数
            pstmt.setString(1, uuid);

            // 执行查询并处理结果
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    JsonObject result = new Gson().fromJson(rs.getString("data"), JsonObject.class);
                    return new ApiResult(result, Long.parseLong(rs.getString("time")));
                } else throw new SQLException("not found");
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
