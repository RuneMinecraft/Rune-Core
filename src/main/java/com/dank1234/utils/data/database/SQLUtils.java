package com.dank1234.utils.data.database;

import java.sql.*;
import java.util.Optional;

public class SQLUtils {
    static int executeUpdate(String sql) {
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static int executeUpdate(String sql, SQLConsumer<PreparedStatement> consumer) {
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            consumer.accept(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static <T> Optional<T> executeQuery(String sql, SQLConsumer<PreparedStatement> consumer, SQLFunction<ResultSet, T> mapper) {
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            consumer.accept(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return Optional.ofNullable(mapper.apply(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @FunctionalInterface
    interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    @FunctionalInterface
    interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}
