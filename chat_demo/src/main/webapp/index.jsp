<%--
  Created by IntelliJ IDEA.
  User: 25893
  Date: 2025/6/7
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MyChatbot</title>
    <link rel="stylesheet" href="css/index_css.css">
</head>
<body>
    <div class="center-form">
        <form action="login" method="post">
            <table>
                <tr>
                    <td colspan="2" class="hellooo">
                        <h1>欢迎回来</h1>
                    </td>
                </tr>
                <tr>
                    <td class="you">用户名</td>
                    <td>
                        <input type="text" name="name">
                    </td>
                </tr>
                <tr>
                    <td class="you">密码</td>
                    <td>
                        <input type="password" name="mm">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="you">
                        还没有账户？请<a href="zhuce.jsp">注册</a>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="登录">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
