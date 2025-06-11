<%--
  Created by IntelliJ IDEA.
  User: 25893
  Date: 2025/6/8
  Time: 00:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.ChatLog" %>

<html>
<head>
    <title>Chatbot</title>
    <link rel="stylesheet" href="css/chat.css">
</head>
<body>
    <div class="chat-container">
        <h2>Chatbot</h2>

        <div id="chatBox">
            <!-- 显示历史聊天记录 -->
            <%
                String username = (String) request.getAttribute("username");
                List<ChatLog> chatHistory = (List<ChatLog>) request.getAttribute("chatHistory");
                if (chatHistory != null) {
                    for (ChatLog record : chatHistory) {
            %>
            <div class="timestamp">
                <%= record.getCreatedAt() %>
            </div>
            <div class="message user"><strong><%= username %>:</strong> <%= record.getQuestion() %></div>
            <div class="message bot"><strong>机器人:</strong> <%= record.getAnswer() %></div>
            <%
                    }
                }
            %>
            <%
                String userQuestion = (String) request.getAttribute("userQuestion");
                String botAnswer = (String) request.getAttribute("botAnswer");
                if (userQuestion != null && botAnswer != null) {
            %>
            <%
                }
            %>
        </div>

        <form action="chat" method="post" class="input-area">
            <input type="text" name="question" id="userInput" placeholder="请输入您的问题..." style="width: 300px;" />
            <button type="submit" id="sendBtn">发送</button>
        </form>
    </div>
    <script>
        window.onload = function () {
            var chatBox = document.getElementById("chatBox");
            if (chatBox) {
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }
    </script>
</body>
</html>
