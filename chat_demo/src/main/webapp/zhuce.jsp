<%--
  Created by IntelliJ IDEA.
  User: 25893
  Date: 2025/6/7
  Time: 19:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册</title>
    <link rel="stylesheet" href="css/index_css.css">
</head>
<body>
<div class="center-form">
    <form action="register" method="post" onsubmit="return checkForm();">
        <table>
            <tr>
                <td colspan="2" class="hellooo">
                    <h1>创建帐户</h1>
                </td>
            </tr>
            <tr>
                <td class="you">用户名</td >
                <td>
                    <input type="text" name="username" required>
                </td>
            </tr>
            <tr>
                <td class="you">密码</td>
                <td>
                    <input type="password" name="password1" id="password1" required>
                </td>
            </tr>
            <tr>
                <td class="you">再输一次</td>
                <td>
                    <input type="password" name="password2" id="password2" required>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="you">
                    已经有帐户了？请<a href="index.jsp">登录</a>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="确定">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
