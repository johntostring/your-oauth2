<html>
<head>
    <title>统一认证系统</title>
    <link rel="shortcut icon" href="favicon.ico">
    <link type="text/css" href="/assets/css/login.css" rel="stylesheet">
</head>
<body>
<div class="cont">
    <div class="demo">
        <div class="login">
            <div class="login_title">
                <img src="https://millinch.com/assets/3028c9f0ef192a0051e4b0f8b3c9d78c.png" alt="Millinch" width="100" height="100">
                <h3>统一认证系统</h3>
            </div>
            <div class="login__form">
                <form id="loginForm" role="form" action="login" method="post">
                    <div class="login__row">
                        <svg class="login__icon name svg-icon" viewBox="0 0 20 20">
                            <path d="M0,20 a10,8 0 0,1 20,0z M10,0 a4,4 0 0,1 0,8 a4,4 0 0,1 0,-8" />
                        </svg>
                        <input type="text" class="login__input name" placeholder="用户名" name="username" value="johntostring"/>
                    </div>
                    <div class="login__row">
                        <svg class="login__icon pass svg-icon" viewBox="0 0 20 20">
                            <path d="M0,20 20,20 20,8 0,8z M10,13 10,16z M4,8 a6,8 0 0,1 12,0" />
                        </svg>
                        <input type="password" class="login__input pass" placeholder="密码" name="password" value="123456"/>
                    </div>
                    <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <#if RequestParameters['error']??>
                    <div class="login-alert">
                        ${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
                    </div>
                    </#if>
                    <button type="button" class="login__submit">登&nbsp;&nbsp;录</button>
                </form>
                <p class="login__signup">还没有账号？ &nbsp;<a>注册</a></p>
            </div>
        </div>
        <div class="app">
            <div class="app__top">
                <div class="login__check"></div>
            </div>
            <div class="app__bot"></div>
            <div class="app__logout">
                <svg class="app__logout-icon svg-icon" viewBox="0 0 20 20">
                    <path d="M6,3 a8,8 0 1,0 8,0 M10,0 10,12"/>
                </svg>
            </div>
        </div>
    </div>
</div>
<div class="container">
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

        function ripple(elem, e) {
            $(".ripple").remove();
            var elTop = elem.offset().top,
                elLeft = elem.offset().left,
                x = e.pageX - elLeft,
                y = e.pageY - elTop;
            var $ripple = $("<div class='ripple'></div>");
            $ripple.css({top: y, left: x});
            elem.append($ripple);
        }

        $(document).on("click", ".login__submit", function(e) {
            if (animating) return;
            animating = true;
            var that = this;
            ripple($(that), e);
            $(that).addClass("processing");
            setTimeout(function() {
                $('#loginForm').trigger('submit');
            }, submitPhase1);

            // setTimeout(function() {
            //     $(that).addClass("success");
            //     setTimeout(function() {
            //         $app.show();
            //         $app.css("top");
            //         $app.addClass("active");
            //     }, submitPhase2 - 70);
            //     setTimeout(function() {
            //         $login.hide();
            //         $login.addClass("inactive");
            //         animating = false;
            //         $(that).removeClass("success processing");
            //     }, submitPhase2);
            // }, submitPhase1);
        });

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
            }, logoutPhase1);
        });

    });
</script>
</body>
</html>