package com.smartcomplaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String URL = "jdbc:sqlite:complaints.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // ================= INITIALIZE DATABASE =================

    public static void initialize() {

        String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    name TEXT NOT NULL,
                    role TEXT NOT NULL,
                    department TEXT
                )
                """;

        String complaintsTable = """
                CREATE TABLE IF NOT EXISTS complaints (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    complaint_id TEXT UNIQUE NOT NULL,
                    student TEXT NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT NOT NULL,
                    department TEXT DEFAULT 'Not Assigned',
                    worker TEXT DEFAULT 'Not Assigned',
                    status TEXT NOT NULL,
                    resolution TEXT DEFAULT '',
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement()
        ) {

            statement.execute(usersTable);
            statement.execute(complaintsTable);

            // Upgrade old users table if department column does not exist
            try {
                statement.execute(
                        "ALTER TABLE users ADD COLUMN department TEXT"
                );
            } catch (SQLException ignored) {
                // Column already exists
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {

            System.out.println(
                    "Database initialization failed: "
                            + e.getMessage()
            );
        }
    }

    // ================= REGISTER USER =================

    public static boolean registerUser(
            String username,
            String password,
            String name,
            String role,
            String department
    ) {

        String sql = """
                INSERT INTO users
                (username, password, name, role, department)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = connect();
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, role);

            if (department == null ||
                    department.trim().isEmpty()) {

                ps.setNull(5, Types.VARCHAR);

            } else {

                ps.setString(5, department);
            }

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Registration failed: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // ================= LOGIN =================

    // Returns user's registered name if login is valid.
    // Returns null if login fails.

    public static String authenticateUser(
            String username,
            String password,
            String role,
            String department
    ) {

        String sql;

        if (role.equals("Worker")) {

            sql = """
                    SELECT name
                    FROM users
                    WHERE username = ?
                    AND password = ?
                    AND role = ?
                    AND department = ?
                    """;

        } else {

            sql = """
                    SELECT name
                    FROM users
                    WHERE username = ?
                    AND password = ?
                    AND role = ?
                    """;
        }

        try (
                Connection connection = connect();
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            if (role.equals("Worker")) {
                ps.setString(4, department);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getString("name");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Login error: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // ================= GET WORKERS BY DEPARTMENT =================

    public static List<String> getWorkersByDepartment(
            String department
    ) {

        List<String> workers = new ArrayList<>();

        String sql = """
                SELECT name
                FROM users
                WHERE role = 'Worker'
                AND department = ?
                ORDER BY name
                """;

        try (
                Connection connection = connect();
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, department);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                workers.add(
                        rs.getString("name")
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error getting workers: "
                            + e.getMessage()
            );
        }

        return workers;
    }

    // ================= SAVE NEW COMPLAINT =================

    public static boolean addComplaint(
            String complaintId,
            String student,
            String category,
            String description
    ) {

        String sql = """
                INSERT INTO complaints
                (complaint_id, student, category, description, status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = connect();
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, complaintId);
            ps.setString(2, student);
            ps.setString(3, category);
            ps.setString(4, description);
            ps.setString(5, "Submitted");

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding complaint: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // ================= GET ALL COMPLAINTS =================

    public static List<Complaint> getAllComplaints() {

        List<Complaint> complaints =
                new ArrayList<>();

        String sql =
                "SELECT * FROM complaints ORDER BY id DESC";

        try (
                Connection connection = connect();
                Statement statement =
                        connection.createStatement();
                ResultSet rs =
                        statement.executeQuery(sql)
        ) {

            while (rs.next()) {

                complaints.add(
                        new Complaint(
                                rs.getInt("id"),
                                rs.getString("complaint_id"),
                                rs.getString("student"),
                                rs.getString("category"),
                                rs.getString("description"),
                                rs.getString("department"),
                                rs.getString("worker"),
                                rs.getString("status"),
                                rs.getString("resolution")
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error reading complaints: "
                            + e.getMessage()
            );
        }

        return complaints;
    }

    // ================= ASSIGN DEPARTMENT + WORKER =================

    public static boolean assignComplaint(
            String complaintId,
            String department,
            String worker
    ) {

        String sql = """
                UPDATE complaints
                SET department = ?, worker = ?, status = ?
                WHERE complaint_id = ?
                """;

        try (
                Connection connection = connect();
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, department);
            ps.setString(2, worker);
            ps.setString(3, "Assigned");
            ps.setString(4, complaintId);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error assigning complaint: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // ================= UPDATE STATUS =================

    public static boolean updateStatus(
            String complaintId,
            String status
    ) {

        String sql = """
                UPDATE complaints
                SET status = ?
                WHERE complaint_id = ?
                """;

        try (
                Connection connection = connect();
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, status);
            ps.setString(2, complaintId);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error updating status: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // ================= SAVE RESOLUTION =================

    public static boolean updateResolution(
            String complaintId,
            String resolution
    ) {

        String sql = """
                UPDATE complaints
                SET resolution = ?, status = ?
                WHERE complaint_id = ?
                """;

        try (
                Connection connection = connect();
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, resolution);
            ps.setString(2, "Resolved");
            ps.setString(3, complaintId);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error saving resolution: "
                            + e.getMessage()
            );

            return false;
        }
    }
}