package com.evisa.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ApplicationController")
public class ApplicationController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String URL = "String url = \"jdbc:mysql://b1q6tizvtr1ekfeadloq-mysql.services.clever-cloud.com:3306/b1q6tizvtr1ekfeadloq\";";
    private static final String USER = "u19vrja67kumytyz";       
    private static final String PASSWORD = "0duqCytcpV9R4OMkdR4q";   

    // GET Method: Fetches records for the Admin Dashboard
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json");
        
        PrintWriter out = response.getWriter();
        String query = "SELECT * FROM applications ORDER BY submission_date DESC";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                
                StringBuilder json = new StringBuilder();
                json.append("[");
                boolean first = true;
                while (rs.next()) {
                    if (!first) json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("application_id")).append(",");
                    json.append("\"fullName\":\"").append(rs.getString("full_name")).append("\",");
                    json.append("\"passportNum\":\"").append(rs.getString("passport_number")).append("\",");
                    json.append("\"nationality\":\"").append(rs.getString("nationality")).append("\",");
                    json.append("\"status\":\"").append(rs.getString("visa_status")).append("\",");
                    json.append("\"date\":\"").append(rs.getString("submission_date")).append("\"");
                    json.append("}");
                    first = false;
                }
                json.append("]");
                out.print(json.toString());
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }

    // POST Method: Processes Applications, Status Updates, User Registration, and User Login
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json");
        
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                
                // 1. OPERATION: USER REGISTRATION
                if ("registerUser".equals(action)) {
                    String email = request.getParameter("email");
                    String pass = request.getParameter("password");
                    String name = request.getParameter("fullName");
                    String nationality = request.getParameter("nationality");
                    
                    String query = "INSERT INTO users (email, password, full_name, nationality) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, email);
                        ps.setString(2, pass);
                        ps.setString(3, name);
                        ps.setString(4, nationality);
                        
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                            out.print("{\"status\":\"success\", \"message\":\"User registered\"}");
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.print("{\"status\":\"error\", \"message\":\"Registration failed.\"}");
                        }
                    }
                } 
                
                // 2. OPERATION: USER LOGIN VERIFICATION
                else if ("loginUser".equals(action)) {
                    String email = request.getParameter("email");
                    String pass = request.getParameter("password");
                    
                    String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, email);
                        ps.setString(2, pass);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                // Return the user profile details back to React on valid password validation matches
                                out.print("{");
                                out.print("\"status\":\"success\",");
                                out.print("\"fullName\":\"" + rs.getString("full_name") + "\",");
                                out.print("\"nationality\":\"" + rs.getString("nationality") + "\"");
                                out.print("}");
                            } else {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                out.print("{\"status\":\"error\", \"message\":\"Invalid credentials\"}");
                            }
                        }
                    }
                } 
                
                // 3. OPERATION: ADMINISTRATIVE VISA STATUS UPDATE
                else if ("updateStatus".equals(action)) {
                    String appId = request.getParameter("id");
                    String newStatus = request.getParameter("status");
                    String query = "UPDATE applications SET visa_status = ? WHERE application_id = ?";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, newStatus);
                        ps.setInt(2, Integer.parseInt(appId));
                        ps.executeUpdate();
                        out.print("{\"status\":\"success\"}");
                    }
                } 
                
                // 4. OPERATION: LODGE NEW APPLICATION
                else {
                    String fullName = request.getParameter("fullName");
                    String passportNum = request.getParameter("passportNum");
                    String nationality = request.getParameter("nationality");
                    String query = "INSERT INTO applications (full_name, passport_number, nationality) VALUES (?, ?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setString(1, fullName);
                        ps.setString(2, passportNum);
                        ps.setString(3, nationality);
                        ps.executeUpdate();
                        out.print("{\"status\":\"success\"}");
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
