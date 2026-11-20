<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@ include file="common.jspf" %>
<h1>Thống kê món ăn</h1><form class="panel inline" method="get"><label>Từ ngày<input type="date" name="from" value="<%=Web.safe((String)request.getAttribute("from"))%>" required></label>
<label>Đến ngày<input type="date" name="to" value="<%=Web.safe((String)request.getAttribute("to"))%>" required></label><button>Lọc</button></form>
<table><thead><tr><th>Mã món</th><th>Tên món</th><th>Số lượng bán</th><th>Doanh thu</th></tr></thead><tbody>
<% for(Map<String,Object> row:(List<Map<String,Object>>)request.getAttribute("rows")){ %>
<tr><td><%=Web.safe(String.valueOf(row.get("code")))%></td><td><%=Web.safe(String.valueOf(row.get("name")))%></td><td><%=row.get("quantity")%></td><td><%=row.get("revenue")%></td></tr><% } %>
</tbody></table><p class="muted">Số liệu chỉ tính các đơn đã có hóa đơn và lọc theo thời gian tạo đơn.</p></main></body></html>
