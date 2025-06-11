package com.servlets;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.model.ChatLog;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/chat")
public class ChatServlet extends HttpServlet {
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String API_KEY = "sk-"; // 替换为你的 API Key

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ksdata?useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String question = req.getParameter("question");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            res.sendRedirect("denglu.jsp"); // 未登录重定向
            return;
        }

        String answer;

        try {
            // 构造请求体
            JSONObject jsonObjectMessages = new JSONObject();
            jsonObjectMessages.put("role", "user");
            jsonObjectMessages.put("content", question);

            JSONArray jsonArrayMessages = new JSONArray();
            jsonArrayMessages.add(jsonObjectMessages);

            JSONObject jsonSend = new JSONObject();
            jsonSend.put("model", "qwen-plus");
            jsonSend.put("messages", jsonArrayMessages);

            String jsonBody = jsonSend.toJSONString();

            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    responseBuilder.append(line.trim());
                }
            }

            JSONObject jsonObjectResponse = JSONObject.parseObject(responseBuilder.toString());
            answer = jsonObjectResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            // 保存聊天记录
            saveChatLog(session, question, answer);

        } catch (Exception e) {
            e.printStackTrace();
            answer = "调用大模型出错，请稍后再试。";
        }

        // 获取聊天记录并传递给 JSP
        List<ChatLog> chatHistory = loadChatHistory(session);
        req.setAttribute("chatHistory", chatHistory);
        req.setAttribute("userQuestion", question);
        req.setAttribute("botAnswer", answer);
        req.setAttribute("username", session.getAttribute("username"));

        req.getRequestDispatcher("/chat.jsp").forward(req, res);
    }

    // ✅ 新增：处理首次打开 chat.jsp 时加载聊天记录
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            res.sendRedirect("denglu.jsp");
            return;
        }

        List<ChatLog> chatHistory = loadChatHistory(session);
        req.setAttribute("chatHistory", chatHistory);
        req.setAttribute("username", session.getAttribute("username"));
        req.getRequestDispatcher("/chat.jsp").forward(req, res);
    }

    private void saveChatLog(HttpSession session, String question, String answer) {
        String username = (String) session.getAttribute("username");
        if (username == null) return;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String getUserIdSql = "SELECT id FROM users WHERE username=?";
                try (PreparedStatement ps1 = conn.prepareStatement(getUserIdSql)) {
                    ps1.setString(1, username);
                    try (ResultSet rs = ps1.executeQuery()) {
                        if (rs.next()) {
                            int userId = rs.getInt("id");
                            String insertLogSql = "INSERT INTO chat_logs (user_id, question, answer) VALUES (?, ?, ?)";
                            try (PreparedStatement ps2 = conn.prepareStatement(insertLogSql)) {
                                ps2.setInt(1, userId);
                                ps2.setString(2, question);
                                ps2.setString(3, answer);
                                ps2.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ChatLog> loadChatHistory(HttpSession session) {
        List<ChatLog> chatHistory = new ArrayList<>();
        String username = (String) session.getAttribute("username");

        if (username == null) return chatHistory;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT cl.question, cl.answer, cl.created_at FROM chat_logs cl " +
                        "JOIN users u ON cl.user_id = u.id WHERE u.username=? ORDER BY cl.created_at";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, username);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            ChatLog log = new ChatLog();
                            log.setQuestion(rs.getString("question"));
                            log.setAnswer(rs.getString("answer"));
                            log.setCreatedAt(rs.getTimestamp("created_at").toString());
                            chatHistory.add(log);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatHistory;
    }
}
