<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@ include file="common.jspf" %><h1>Quản lý món ăn</h1>
<form class="panel gridform" method="post"><input type="hidden" name="csrf" value="<%=Web.safe((String)session.getAttribute("csrf"))%>">
<label>Mã món<input name="code" required></label><label>Tên món<input name="name" required></label><label>Đơn giá<input name="price" type="number" min="0" step=".01" required></label>
<label>Mô tả<input name="description"></label><button name="action" value="save">Thêm / cập nhật</button></form>
<table><tr><th>Mã</th><th>Tên</th><th>Giá</th><th>Trạng thái</th><th>Thao tác</th></tr>
<% for(Map<String,Object> row:(List<Map<String,Object>>)request.getAttribute("rows")){ %>
<tr><td><%=Web.safe(String.valueOf(row.get("code")))%></td><td><%=Web.safe(String.valueOf(row.get("name")))%></td><td><%=row.get("price")%></td><td><%=Web.safe(String.valueOf(row.get("status")))%></td>
<td><form method="post"><input type="hidden" name="csrf" value="<%=Web.safe((String)session.getAttribute("csrf"))%>"><input type="hidden" name="code" value="<%=Web.safe(String.valueOf(row.get("code")))%>"><button name="action" value="stop">Ngừng bán</button></form></td></tr><% } %></table></main></body></html>
