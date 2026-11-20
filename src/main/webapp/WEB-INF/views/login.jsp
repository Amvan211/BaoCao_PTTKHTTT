<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@ include file="common.jspf" %><script>document.body.className='login-page'</script>
<section class="login-card"><div class="login-logo">♨</div><h1>Chào mừng trở lại</h1><p class="subtitle">Đăng nhập để quản lý nhà hàng RestMan</p><form method="post" action="<%=ctx%>/login">
<label>Tên đăng nhập<input name="username" required autocomplete="username"></label>
<label>Mật khẩu<input name="password" type="password" required autocomplete="current-password"></label>
<button>Đăng nhập vào hệ thống</button></form><p class="muted" style="text-align:center;margin-top:20px">Tài khoản demo: quanly hoặc thukho · Mật khẩu: 123456</p></section></main></body></html>
