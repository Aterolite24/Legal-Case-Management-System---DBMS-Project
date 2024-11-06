package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

@Repository
public class CommonDAO {
    private final DataSource dataSource;

    public CommonDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean checkEmailExistsInAllTables(String email) {
        return checkEmailInClient(email) || 
               checkEmailInLawyer(email) || 
               checkEmailInParalegal(email);
    }

    private boolean checkEmailInClient(String email) {
        String query = "SELECT COUNT(*) FROM ClientEmail WHERE emailAddress = ?";
        return executeCountQuery(query, email);
    }

    private boolean checkEmailInLawyer(String email) {
        String query = "SELECT COUNT(*) FROM LawyerEmail WHERE emailAddress = ?";
        return executeCountQuery(query, email);
    }

    private boolean checkEmailInParalegal(String email) {
        String query = "SELECT COUNT(*) FROM ParalegalEmail WHERE emailAddress = ?";
        return executeCountQuery(query, email);
    }

    public boolean checkPhoneExistsInAllTables(String phoneNumber) {
        return checkPhoneInClient(phoneNumber) || 
               checkPhoneInLawyer(phoneNumber) || 
               checkPhoneInParalegal(phoneNumber);
    }

    private boolean checkPhoneInClient(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM ClientPhone WHERE phoneNumber = ?";
        return executeCountQuery(query, phoneNumber);
    }

    private boolean checkPhoneInLawyer(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM LawyerPhone WHERE phoneNumber = ?";
        return executeCountQuery(query, phoneNumber);
    }

    private boolean checkPhoneInParalegal(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM ParalegalPhone WHERE phoneNumber = ?";
        return executeCountQuery(query, phoneNumber);
    }

    // Helper method to execute count queries
    private boolean executeCountQuery(String query, String parameter) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, parameter);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0; // Return true if count > 0
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error executing query: " + query, e);
        }
    }
    
    // Custom runtime exception for data access issues
    public static class DataAccessException extends RuntimeException {
        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
