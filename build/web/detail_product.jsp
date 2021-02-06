<%-- 
    Document   : login
    Created on : Jan 6, 2021, 12:56:32 AM
    Author     : ASUS
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Hana Shop</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
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

        <div class="jumbotron">
            <div class="container text-center">
                <h1>Hana Shop</h1>      
                <p>Mission, Vission & Values</p>
            </div>
        </div>

        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">HanaShop</a>
                </div>
                <ul class="nav navbar-nav">
                    <li>
                        <c:url value="/home" var="home"></c:url>
                        <a style="margin: 20px;color: white" href="${home}" class='btn btn-info' ><span class="glyphicon glyphicon-home"></span> Home</a>
                    </li>
                    <c:if test="${null eq sessionScope.USER}">
                        <c:url value="/auth/login" var="login"></c:url>
                        <li><a style="margin: 20px;color: white" class="btn btn-info" style="color: white" href="${login}"><span class="glyphicon glyphicon glyphicon-log-in"></span>Login</a></li>
                        </c:if>
                        <c:if test="${null != sessionScope.USER}">
                        <li>
                            <c:url value="/user/detail" var="orderDetailLink"></c:url>
                            <c:if test="${sessionScope.USER.role != 'ADMIN'}"> 
                                <a style="margin: 20px;color: white" href="${orderDetailLink}" class='btn btn-info'><span class="glyphicon glyphicon-user"></span> Quản lý đơn hàng</a>
                            </c:if>
                        </li>
                        <c:url value="/auth/logout" var="logout"></c:url>
                        <li><form style="margin: 20px" action="${logout}" method="POST">
                                <button style="width: 100px;height: 50px" type="submit" class="btn btn-success "><span class="glyphicon glyphicon-log-out"></span> Logout</button>
                            </form></li>
                        </c:if>
                </ul>
            </div>
        </nav>         
        <c:if test="${sessionScope.USER.role!='ADMIN'}">
            <button onclick="getInforCart()" data-toggle="modal" data-target="#cartDetailModal" class="btn btn-info myFloatButton btn-lg" style="margin-bottom:20px">
                <span class="glyphicon glyphicon-shopping-cart"/>
                <c:if test="${null != sessionScope.sizeCart}">
                    <span id="badgeCart" class="badge">
                        ${sessionScope.sizeCart}
                    </span>
                </c:if>
                <c:if test="${null == sessionScope.sizeCart}">
                    <span id="badgeCart" class="badge"></span>
                </c:if>
            </button>
            <input id="sessionId" type="hidden" value="${pageContext.session.id}"/>
        </c:if>
        <div class="container">
            <div class="row">
                <div class="col-sm-4">
                    <div class="panel-body"><img src="http://localhost:8080/HanaShop/${productDetail.images.get(0).sourceAddress}" class="img-responsive" style="width:300px;height: 300px;margin-left: 100px" alt="Image"></div>
                </div>
                <div class="col-sm-4"></div>
                <div class="col-sm-4">
                    <table class="table">
                        <tr>
                            <th>Tên</th>
                            <td>${productDetail.name}</td>
                        </tr>
                        <tr>
                            <th>Giá</th>
                            <td>${productDetail.price}</td>
                        </tr>
                        <tr>
                            <th>Số lượng</th>
                            <td>${productDetail.quantity}</td>
                        </tr>
                        <tr>
                            <th>Chi tiết</th>
                            <td style="width: 200px"><text>${productDetail.description}</text></td>
                        </tr>
                        <tr>
                            <td>
                                <c:url value="/user/product/add_to_cart" var="addToCartAction"></c:url>
                                <form onsubmit="return checkAddToCart(${productDetail.quantity})" action="${addToCartAction}" method="POST">
                                    <input name="idProduct" type="hidden" value="${productDetail.id}"/>
                                    <label>Chọn số lượng (số nguyên dương)</label>
                                    <input type="hidden" id="productQuantity" value="${productDetail.quantity}"/>
                                    <input pattern="[0-9]+" id="productQuantityCart" name="productQuantityCart" type="number" min="1" max="${prouductDetail.quantity}"/>
                                    <button style="margin-top: 20px" class="btn btn-danger">Thêm vào giỏ hàng</button>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <c:if test="${sessionScope.message!=null}">
                                    <c:choose>
                                        <c:when test="${sessionScope.message.status==false}">
                                            <p style="color: red">${sessionScope.message.content}</p>
                                        </c:when>
                                        <c:when test="${sessionScope.message.status==true}">
                                            <p style="color: green">${sessionScope.message.content}</p>
                                        </c:when>
                                    </c:choose>
                                    <c:remove var="message" scope="session" /> 
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="cartDetailModal" class="modal fade" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Modal Header</h4>
                        </div>
                        <div>
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th scope="col">Tên</th>
                                        <th scope="col">Số lượng</th>
                                        <th scope="col">Giá</th>
                                        <th scope="col">Cập nhật</th>
                                        <th scope="col">
                                            <text style="margin-left: 15px">Xóa</text>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody id="bodyModalInfoCart">


                                </tbody>    
                            </table>
                        </div>
                        <div class="modal-footer">
                            <c:url value="/user/order" var="makeOder"></c:url>
                            <form style="margin: 20px" onsubmit="return checkCart();" method="GET" action="${makeOder}">
                                <button type="submit" class="btn btn-success">Mua hàng thanh toán khi nhận hàng</button>
                            </form>
                            <c:url value="/user/paypal" var="makeOderPayPal"></c:url>
                            <form style="margin: 20px" onsubmit="return checkCart();" method="GET" action="${makeOderPayPal}">
                                <button type="submit" class="btn btn-primary">Thanh toán qua PayPal</button>
                            </form>
                            <button style="margin: 20px" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>                        
            <h3>Các sản phẩm hay được mua kèm</h3>
            <div class="container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>No</th>
                            <th></th>
                            <th>Tên</th>
                            <th>Số lượng</th>
                            <th>Giá</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${requestScope.listRecommend.values()}" var="product" varStatus="count">
                            <tr>
                                <td>${count.index+1}</td>
                                <td>
                                    <image style="width:100px;height: 100px;margin: 10px" alt="Image" src="http://localhost:8080/HanaShop/${product.images.get(0).sourceAddress}" />
                                </td>
                                <td>${product.name}</td>
                                <td>${product.quantity}</td>
                                <td>${product.price}</td>

                                <td>
                                    <c:url value="/user/product/detail" var="producDetailAction"></c:url>
                                    <form method="GET" action="${producDetailAction}">
                                        <input type="hidden" name="idProductDetail" value="${product.id}"/>
                                        <button class="btn btn-info">Xem chi tiết</button>
                                    </form>
                                </td>

                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </div>
        </div>

        <footer class="container-fluid text-center">
            <p>Online Store Copyright</p>  
            <form class="form-inline">Get deals:
                <input type="email" class="form-control" size="50" placeholder="Email Address">
                <button type="button" class="btn btn-danger">Sign Up</button>
            </form>
        </footer>
        <script>
            const checkAddToCart = (button) => {
                var numToCart = document.getElementById("productQuantityCart").value;
                var quantity = document.getElementById("productQuantity").value;
                if (parseInt(numToCart) > parseInt(quantity)) {
                    alert("Vượt quá số lượng");
                    return false;
                }
                if (numToCart == "") {
                    document.getElementById("productQuantityCart").value = 1;
                    numToCart=1
                }
                alert("Đã thêm vào giỏ hàng");
                
                return true;
            }
             const getInforCart = () => {
                var sessionId = document.getElementById("sessionId").value;
                $.ajax({
                    url: "http://localhost:8080/HanaShop/ajax/product/getCart?sessionId=" + sessionId,
                    cache: false,
                    dataType: 'json',
                    contentType: 'application/json',
                    method: "GET",
                    success: function (data, textStatus, jqXHR) {
                        generateCartInfo(data);
                    }

                })
            }
            const checkCart = () => {

                var num = document.getElementById("badgeCart").innerHTML;
                if (num === "" || num == 0) {
                    alert("Không có hàng trong giỏ")
                    return false;
                }
                return true;
            }
            const updateCart = (button) => {
                var numBox = document.getElementById(button.value);
                var idProduct = numBox.id.split("@")[1]
                var num = numBox.value
                if (!num.match("^[0-9]+$")) {
                    num = 1;
                    alert("Bạn nhập sai định dạng số lượng");
                } else if (num <= 0) {
                    alert("Bạn nhập sai định dạng số lượng");
                    document.getElementById(button.value).value = 1;
                } else {
                    $.ajax({
                        url: "http://localhost:8080/HanaShop/ajax/product/updateNumCart",
                        method: "POST",
                        cache: false,
                        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                        data: {
                            id: idProduct,
                            num: num,
                        },
                        success: function (data, textStatus, jqXHR) {
                            generateCartInfo(data);
                            var badgeCart = parseInt(document.getElementById("badgeCart").innerHTML);
                            num = data.realNum
                            document.getElementById("badgeCart").innerHTML = num;

                        }
                    })
                }
            }
            const generateCartInfo = (data) => {
                var row = "";
                var bodyModalInfoCart = document.getElementById("bodyModalInfoCart");
                $.each(data.listItems, (i, item) => {
                    row += "<tr>"
                    row += "<td>" + item.name + "</td>"
                    row += "<td><input value=" + item.quantityInCart + " id='productQuantityModalCartDetail@" + item.id + "'" + " required type='number' min='1' max='" + item.quantity + "'/></td>"
                    row += "<td>" + item.price + "</td>"
                    row += "<td>" + "<button class='btn btn-warning' onclick='updateCart(this)' type='button' value=" + "'productQuantityModalCartDetail@" + item.id + "'" + "/>" + "Cập nhật" + "</button>" + "</td>"
                    row += "<td>" + "<input style='margin-left:20px' value=" + item.id + " class='form-check-input cart-info' type='checkbox'/>" + "</td>"

                    row += "</tr>"
                    console.log(row)

                });
                row += "<tr>"
                row += "<th scope='row'>Tổng tiền</th>"
                row += "<td></td>"
                row += "<td>" + data.total + "</td>"
                row += "<td/>"
                row += "<td>" + "<button onclick='removeFromCart()' type='button' class='btn btn-danger'>Xóa</button>" + "</td>"
                row += "</tr>"
                bodyModalInfoCart.innerHTML = row

            }
            const removeFromCart = () => {
                var r = confirm("Bạn có chắc muốn xóa");
                if (r == true) {
                    var sessionId = document.getElementById("sessionId").value;
                    var checkBoxs = document.getElementsByClassName("cart-info");
                    var removeArray = [];
                    var i;
                    for (i = 0; i < checkBoxs.length; i++) {
                        if (checkBoxs[i].checked == true) {
                            removeArray.push(checkBoxs[i].value);
                        }
                    }
                    if (removeArray.length == 0) {
                        alert("Hãy chọn sản phẩm để xóa")
                    } else {
                        console.log(removeArray)
                        $.ajax({
                            url: "http://localhost:8080/HanaShop/ajax/product/removeFromCart",
                            method: "POST",
                            cache: false,
                            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                            data: {
                                removeArray: removeArray,
                                sessionId: sessionId
                            },
                            success: function (data, textStatus, jqXHR) {
                                generateCartInfo(data);
                                document.getElementById("badgeCart").innerHTML = data.num;
                            }
                        })
                    }
                }

            }
        </script>                             
    </body>
</html>
