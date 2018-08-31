<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
    <ul>
        <li><a target="_blank" href="/ums/uapi/user-detail">获取UserDetail</a></li>
        <li><a target="_blank" id="load-user-detail">AJAX获取UserDetail</a></li>
        <li><a target="_blank" href="/ums/hi">测试/hi</a></li>
        <li><a target="_blank" href="/ums/check">测试用户角色</a></li>
        <li>
            <a id="logout-all" href="javascript:void(0)">退出</a>
        </li>
        <#--<li>
            <form action="/ums/logout" method="post">
                <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button>退出</button>
            </form>
        </li>-->
    </ul>
    <pre id="print-panel" style="margin: 20px;"></pre>
    <script src="http://cdn.bootcss.com/jquery/1.12.1/jquery.min.js"></script>
    <script type="text/javascript">
        function getCookie(cookiePrefix) {
            var name = cookiePrefix + "=";
            var decodedCookie = decodeURIComponent(document.cookie);
            var ca = decodedCookie.split(';');
            for(var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        }
        $(function () {
            $.get('/ums/uapi/user-detail').then(function (value) {
                if (!value) {
                    console.log('nothing value');
                    return;
                }
                $('#print-panel').html(JSON.stringify(value));
            })


            $('#logout-all').on('click', function (e) {
                $.ajax({
                    url: '/ums/logout',
                    type: 'POST',
                    headers: {
                        'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
                    }
                }).then(function (value) {
                    console.log('ums logout');
                    location.replace('/ums')
                    // $.post('http://sso.example.org:9999/logout', {withCredentials:true}).then(function (value) {
                    //     console.log('sso logout');
                    // })
                })
            })
        })

    </script>
</body>
</html>