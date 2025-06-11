<%--
  Created by IntelliJ IDEA.
  User: 26151
  Date: 2025/6/4
  Time: 22:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chatbot</title>
    <style>
        #chatBox {
            width: 400px;
            height: 300px;
            border: 1px solid #ccc;
            overflow-y: scroll;
            padding: 10px;
            margin-bottom: 10px;
        }
        .message {
            margin: 5px 0;
        }
        .user {
            color: blue;
        }
        .bot {
            color: green;
        }
    </style>
</head>
<body>
    <h2>Chatbot</h2>

    <div id="chatBox">
        <!-- 显示对话内容 -->
        <%
            String userQuestion = (String) request.getAttribute("userQuestion");
            String botAnswer = (String) request.getAttribute("botAnswer");
            if (userQuestion != null && botAnswer != null) {
        %>
            <div class="message user"><strong>您:</strong> <%= userQuestion %></div>
            <div class="message bot"><strong>机器人:</strong> <%= botAnswer %></div>
        <%
            }
        %>
    </div>

    <form action="chat" method="post">
        <input type="text" name="question" id="userInput" placeholder="请输入您的问题..." style="width: 300px;" />
        <button type="submit" id="sendBtn">发送</button>
    </form>
</body>
</html>
