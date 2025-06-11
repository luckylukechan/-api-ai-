package com.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.security.MessageDigest;

public class ZhuceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 获取用户从表单提交的用户名和两次密码
        String username = request.getParameter("username");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        PrintWriter out = response.getWriter();

        if (!password1.equals(password2)) {
            out.println("<script>alert('两次密码不一致'); history.back();</script>");
            return;
        }

        // 对密码进行加密（使用SHA-256哈希）
        String passwordHash = hashPassword(password1);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ksdata?useSSL=false&serverTimezone=UTC",
                    "root", "123456");

            // 检查用户名是否已经存在
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username=?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                out.println("<script>alert('用户已存在'); history.back();</script>");
                return;
            }

            // 如果用户名不存在，插入新用户记录
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users (username, password_hash) VALUES (?, ?)");
            insertStmt.setString(1, username);
            insertStmt.setString(2, passwordHash);

            int result = insertStmt.executeUpdate();
            if (result > 0) {
                out.println("<script>alert('注册成功'); window.location='index.jsp';</script>");
            } else {
                out.println("<script>alert('注册失败'); history.back();</script>");
            }

            // 关闭数据库资源
            rs.close();
            checkStmt.close();
            insertStmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('服务器错误：" + e.getMessage() + "'); history.back();</script>");
        }
    }

    // 将明文密码加密为 SHA-256 哈希字符串
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}