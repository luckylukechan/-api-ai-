package com.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.security.MessageDigest;

public class DengluServlet extends HttpServlet {
    // 登录处理
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("name");
        String password = request.getParameter("mm");

        // 对输入密码进行 SHA-256 加密
        String hashedPassword = hashPassword(password);

        String url = "jdbc:mysql://localhost:3306/ksdata?useSSL=false";
        String dbUser = "root";
        String dbPassword = "123456";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

            String sql = "SELECT * FROM users WHERE username=? AND password_hash=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, hashedPassword); // 用加密后的密码进行比对

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("userId", rs.getInt("id")); // 保存用户ID
                response.getWriter().println("<script>alert('登录成功'); window.location='chat';</script>");
//                response.getWriter().println("<script>alert('登录成功'); window.location='chat.jsp';</script>");
            }
            else {
                response.getWriter().println("<script>alert('用户名或密码错误'); window.location='index.jsp';</script>");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('服务器错误'); window.location='index.jsp';</script>");
        }
    }

    // SHA-256 加密方法
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}
