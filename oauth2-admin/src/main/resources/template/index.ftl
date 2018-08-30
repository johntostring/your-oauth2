<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
    <ul>
        <li><a href="/ums/login">登录</a></li>
        <li><a target="_blank" href="/ums/uapi/user-detail">获取UserDetail</a></li>
        <li><a target="_blank" href="/ums/hi">测试/hi</a></li>
        <li><a target="_blank" href="/ums/check">测试用户角色</a></li>
        <li>
            <form action="/ums/logout" method="post">
                <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button>退出</button>
            </form>
        </li>
    </ul>
</body>
</html>