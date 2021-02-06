<%-- 
    Document   : user
    Created on : Jan 11, 2021, 11:58:02 PM
    Author     : ASUS
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Hana Shop</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <style>
            /* Remove the navbar's default rounded borders and increase the bottom margin */ 
            .navbar {
                margin-bottom: 50px;
                border-radius: 0;
            }

            /* Remove the jumbotron's default bottom margin */ 
            .jumbotron {
                margin-bottom: 0;
            }

            /* Add a gray background color and some padding to the footer */
            footer {
                background-color: #f2f2f2;
                padding: 25px;
            }
            *{padding:0;margin:0;}
            /*dskdskd float button*/


            .myFloatButton{
                position:fixed;
                width:100px;
                height:100px;
                bottom:40px;
                right:40px;
                background-color:#0C9;
                color:#FFF;
                border-radius:50px;
                text-align:center;
                box-shadow: 2px 2px 3px #999;
            }


            /*dskdskd float button*/
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <c:url value="/admin/orderdetail" var="adminOrderDetail"></c:url>
            <form style="margin-left: 100px;margin-bottom: 30px;margin-top: 100px" onsubmit="return checkSearch()"  action="${adminOrderDetail}" method="GET">
                Tên khách hàng: <input name="nameAccount" id="searchNameAccount" value="${param.nameAccount}" type="text"/>
                Từ ngày: <input name="fromDate" onkeydown="return false" id="searchFromDate" type="date" value="${param.fromDate}"/>
                Đến ngày: <input name="toDate" onkeydown="return false" id="searchToDate" type="date" value="${param.toDate}"/>
                <button type="submit" class="btn btn-info">Tìm kiếm</button>
                <c:url value="/admin/vieworder" var="all"></c:url>
                <a type="button" href="${all}"  class="btn btn-info">Tất cả</a>
            </form>
          
                <ul class="nav navbar-nav navbar-right">
                    <c:if test="${null eq sessionScope.USER}">
                        <li><a href="<%= request.getContextPath() + "/auth/login"%>"><span class="glyphicon glyphicon-user"></span>Login</a></li>
                        </c:if>
                        <c:if test="${null != sessionScope.USER}">

                        <li>
                            <c:url value="/product" var="admin"></c:url>
                            <c:if test="${sessionScope.USER.role eq 'ADMIN'}">  
                                <a style="margin: 20px" class="btn btn-primary"  href="${admin}">Trang quản lý</a>
                            </c:if>
                        </li>
                        <li>
                            <c:url value="/auth/logout" var="logout"></c:url>
                            <form style="margin: 20px" action="${logout}" method="POST">
                                <button style="width: 100px;height: 50px" type="submit" class="btn btn-success">Logout</button>
                            </form>
                        </li>
                    </c:if>
                </ul>
            <div style="margin-left: 100px" class="col-sm-8">
                <table class="table">
                    <thead>
                        <tr>
                            <th>No</th>
                            <th>Mã hóa đơn</th>
                            <th>Ngày tạo</th>
                            <th>Tổng tiền</th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:choose>
                            <c:when test="${requestScope.listOrders.size()==0}">
                                <tr><td><p>Không có dữ liệu</p><td></tr>
                            </c:when>
                            <c:when test="${requestScope.listOrders.size()>0}">
                                <c:forEach items="${requestScope.listOrders}" var="order" varStatus="count">
                                    <tr>
                                        <td>${count.index+1}</td>
                                        <td >${pageScope.order.orderId}</td>
                                        <td>${pageScope.order.createDate}</td>
                                        <td>${pageScope.order.total}USD</td>
                                        <td><button value="${pageScope.order.orderId}" onclick="getOrderDetail(this)"  type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#myModalDetail">Chi tiết hóa đơn</button></td>
                                    </tr>

                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </tbody>
                </table>
            </div>

        </div>
        <div class="modal fade" id="myModalDetail" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Sản phẩm</h4>
                    </div>
                    <div>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th scope="col">Tên</th>
                                    <th scope="col">Số lượng</th>
                                    <th scope="col">Giá</th>

                                </tr>
                            </thead>
                            <tbody id="modalDetailBody">

                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <script>
            const checkSearch = () => {
                var fromDate = document.getElementById("searchFromDate").value.trim();
                var toDate = document.getElementById("searchToDate").value.trim();
                var nameAccount=document.getElementById("searchNameAccount").value.trim();
                if (fromDate == "" && toDate == ""&&nameAccount=="") {
                    alert("Bạn chưa điền vào field");
                    return false;
                }
                if (fromDate > toDate) {
                    alert("ngày xuất phát lớn hơn ngày đên")
                    return false;
                }

                return true;
            }
            const getOrderDetail = (button) => {
                var modaDetailBody = document.getElementById("modalDetailBody");
                var row = "";
                $.ajax({
                    url: "http://localhost:8080/HanaShop/ajax/user/order/detail?id=" + button.value,
                    method: "GET",
                    cache: false,
                    contentType: 'application/json',
                    success: function (data, textStatus, jqXHR) {
                        $.each(data.listProducts, (i, item) => {
                            row += "<tr>"
                            row += "<td>" + item.name + "</td>"
                            row += "<td>" + item.quantityInCart + "</td>"
                            row += "<td>" + item.price + " USD" + "</td>"
                            row += "</tr>"
                        })
                        row += "<tr>" + "<th scope='col'>Tổng tiền</th><td></td>" + "<td>" + data.total + " USD" + "</td>" + "</tr>"
                        modaDetailBody.innerHTML = row;
                    }
                })
            }
        </script>
    </body>
</html>
