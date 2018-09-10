<html>
<head>
    <#--<link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">-->
</head>
<body>
<h3>OAuth2.0 Server</h3>
<div class="container">
    <form role="form" action="/logout" method="post">
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">退出</button>
    </form>
</div>
<script src="/assets/js/jquery-1.12.1.min.js"></script>
    <script>
        $.get('/oapi/ping').then(function (value) {
            console.log('the resiult is', value)
        })
        $.get('/api/test').then(function (value) {
            console.log('the resiult is', value)
        })
        $.get('/ppp').then(function (value) {
            console.log('the resiult is', value)
        })
    </script>
</body>
</html>