<html>
<head>
    <head>
        <title>统一认证系统</title>
        <link rel="shortcut icon" href="favicon.ico">
        <link type="text/css" href="/assets/css/login.css" rel="stylesheet">
    </head>
</head>
<body>
<div class="cont">
    <div class="demo">
        <div class="app">
            <div class="app__top">
                <div class="login__check"></div>
            </div>
            <div class="app__bot">
                <form id="logout-form" role="form" action="/logout" method="post">
                    <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
            <div class="app__logout">
                <svg class="app__logout-icon svg-icon" viewBox="0 0 20 20">
                    <path d="M6,3 a8,8 0 1,0 8,0 M10,0 10,12"/>
                </svg>
            </div>
        </div>
    </div>
</div>
<script src="/assets/js/jquery-1.12.1.min.js"></script>
<script type="text/javascript">
    $(function() {
        var animating = false,
            submitPhase1 = 1100,
            submitPhase2 = 400,
            logoutPhase1 = 800,
            $login = $(".login"),
            $app = $(".app");

        function init() {
            if (animating) return;
            animating = true;
            var that = this;
            $(that).addClass("success");
            setTimeout(function() {
                $app.show();
                $app.css("top");
                $app.addClass("active");
            }, submitPhase2 - 70);
            setTimeout(function() {
                $login.hide();
                $login.addClass("inactive");
                animating = false;
                $(that).removeClass("success processing");
            }, submitPhase2);
        }

        $(document).on("click", ".app__logout", function(e) {
            if (animating) return;
            $(".ripple").remove();
            animating = true;
            var that = this;
            $(that).addClass("clicked");
            setTimeout(function() {
                $app.removeClass("active");
                $login.show();
                $login.css("top");
                $login.removeClass("inactive");
            }, logoutPhase1 - 120);
            setTimeout(function() {
                $app.hide();
                animating = false;
                $(that).removeClass("clicked");
                $('#logout-form').trigger('submit');
            }, logoutPhase1);
        });
        init();
    });
</script>
</body>
</html>