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


        <nav class="navbar">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>                        
                    </button>

                </div>
                <nav class="navbar navbar-default">
                    <div class="container-fluid">

                        <ul class="nav navbar-nav">
                            <c:if test="${null eq sessionScope.USER}">
                                <c:url value="/auth/login" var="login"></c:url>
                                <li><a href="${login}"><span class="glyphicon glyphicon-user"></span>Login</a></li>
                                </c:if>
                                <c:if test="${null != sessionScope.USER}">
                                <li>
                                    <c:if test="${sessionScope.USER.role != 'ADMIN'}"> 
                                        <c:url value="/user/detail" var="userOrderDetail"></c:url>
                                        <a style="margin: 20px;color: white" href="${userOrderDetail}" class='btn btn-info'><span class="glyphicon glyphicon-user"></span> Đơn hàng của bạn</a>
                                    </c:if>
                                </li>
                                <li>
                                    <c:if test="${sessionScope.USER.role eq 'ADMIN'}">  
                                        <c:url value="/product" var="admin"></c:url>
                                        <a style="margin: 20px;color: white" class="btn btn-primary" href="${admin}">Trang quản lý</a>
                                    </c:if>
                                </li>
                                <li>
                                    <c:url value="/auth/logout" var="logout"></c:url>
                                    <form style="margin: 20px" action="${logout}" method="POST">
                                        <button style="width: 100px;height: 50px" type="submit" class="btn btn-success">Logout</button>
                                    </form></li>
                                </c:if>
                        </ul>
                        <c:if test="${sessionScope.USER != null}">
                            <div style="margin: auto">
                                <h2 >Welcome ${sessionScope.USER.userId}</h2>
                            </div>

                        </c:if>
                    </div>

                </nav>

            </div>
        </nav>
        <div class="container" style="margin-bottom: 50px">
            <div id="myCarousel" style="width: 1000px;height: 500px;margin: auto;" class="carousel slide" data-ride="carousel">
                <!-- Indicators -->
                <ol class="carousel-indicators">
                    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#myCarousel" data-slide-to="1"></li>
                    <li data-target="#myCarousel" data-slide-to="2"></li>
                </ol>

                <!-- Wrapper for slides -->
                <div class="carousel-inner">
                    <div class="item active">
                        <img style="width:1000px;height: 500px" src="http://localhost:8080/HanaShop/carousel/carousel_1.jpg" alt="Los Angeles">
                    </div>

                    <div class="item">
                        <img style="width:1000px;height: 500px" src="http://localhost:8080/HanaShop/carousel/carousel_2.jpg" alt="Chicago">
                    </div>

                    <div class="item">
                        <img style="width:1000px;height: 500px;" src="http://localhost:8080/HanaShop/carousel/carousel_3.jpg" alt="New York">
                    </div>
                </div>

                <!-- Left and right controls -->
                <a class="left carousel-control" href="#myCarousel" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#myCarousel" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
        </div>
        <div class="row">
        </div>
        <div class="row">
            <div class="col-md-4"></div>
            <div class="col-md-4">
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
                <div class="col-md-4"></div>
            </div>
        </div>     
        <div class="container">
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
            </c:if>

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
            <c:url value="/user/search" var="search"></c:url>
            <form method="GET" action="${search}" onsubmit="return checkSearch()">
                <div class="ui-widget">
                    <input  id="searchName" name="name" type="text" placeholder="tên" value="${param.name}"/>
                </div>
                <input id="searchFromPrice" name="fromPrice" type="number" placeholder="from price" step="0.1" min="0" value="${param.fromPrice}"/>
                <input id="searchToPrice" name="toPrice" type="number" placeholder="to price" step="0.1" min="0" value="${param.toPrice}"/>
                <select id="searchIdCategory" name="idCategory">
                    <option value=${null}>NONE</option>
                    <c:forEach items="${applicationScope.listCategories}" var="category">
                        <c:if test="${pageScope.category.id == param.idCategory}" var="setSearchValue">
                            <option selected="true" value="${pageScope.category.id}">${pageScope.category.name}</option>
                        </c:if>
                        <c:if test="${!setSearchValue}">
                            <option value="${pageScope.category.id}">${pageScope.category.name}</option>
                        </c:if>

                    </c:forEach>
                </select>
                <button style="margin: 20px" type="submit" class="btn btn-info">Tìm kiếm</button>
                <c:url value="/home" var="all"></c:url>
                <a style="margin: 20px" class="btn btn-success" href="${all}">Tất cả</a>

            </form>
            <c:if test="${requestScope.numSearch !=null}">
                <c:if test="${requestScope.numSearch >0}">
                    <p>Có ${requestScope.numSearch} được tìm thấy</p>
                </c:if>
                <c:if test="${requestScope.numSearch ==0}">
                    <p>Không tìm thấy kết quả nào</p>
                </c:if>
            </c:if>
            <c:if test="${sessionScope.outOfStock!=null}">
                <p style="font-weight: bold">Đơn hàng của bạn có</p>
                <c:forEach items="${sessionScope.outOfStock}" var="productOutOfStock">
                    <p>${productOutOfStock.name}</p>
                </c:forEach>
                <p style="color:red">Đã có số lượng lớn hơn trong kho</p>   
                <c:remove var="outOfStock" scope="session" /> 
            </c:if>
            <div class="row">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Hình ảnh</th>
                            <th>Tên</th>
                            <th>Số lượng</th>
                            <th>Mô tả</th>
                            <th>Giá</th>
                            <th>Chọn số lượng</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${null!=requestScope.listProducts}">

                                <c:forEach items="${requestScope.listProducts}" var="product" varStatus="count">
                                    <tr>
                                        <td><img src="http://localhost:8080/HanaShop/${product.images.get(0).sourceAddress}" class="img-responsive" style="width:200px;height: 200px;margin: 30px"  alt="Image"/></td>
                                        <td>${product.name}</td>
                                        <td>${product.quantity}</td>
                                        <td>${product.description}</td>
                                        <td>${product.price} USD</td>
                                        <td><input id="productQuantityCart${product.id}" type="number" min="1" max="${product.quantity}"/></td>
                                <input id="sessionId" type="hidden" value="${pageContext.session.id}"/>
                                <c:if test="${sessionScope.USER.role !='ADMIN'}">
                                    <td>
                                        <button style="width: 110px;height: 50px;margin-bottom: 20px" type="button" onclick="addToCart(this)" value="${product.id}"  style="margin-top: 10" class="btn btn-danger">Thêm vào giỏ</button>
                                        <c:url value="/user/product/detail" var="productDetail"></c:url>
                                        <form action="${productDetail}" method="GET">
                                            <input type="hidden" name="idProductDetail" value="${product.id}"/>
                                            <button style="width: 110px;height: 50px;margin-bottom: 20px" class="btn btn-info"> Xem chi tiết</button>
                                        </form>

                                    </td>
                                </c:if>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:when test="${null==requestScope.listProducts}" >
                            <c:forEach items="${applicationScope.listProducts}" var="product" varStatus="count">
                                <tr>
                                    <td><img src="http://localhost:8080/HanaShop/${product.images.get(0).sourceAddress}" class="img-responsive" style="width:200px;height: 200px;margin: 30px" alt="Image"/></td>
                                    <td>${product.name}</td>
                                    <td>${product.quantity}</td>
                                    <td>${product.description}</td>
                                    <td>${product.price} USD</td>
                                    <td><input id="productQuantityCart${product.id}" type="number" min="1" max="${product.quantity}"/></td>
                                        <c:if test="${sessionScope.USER.role !='ADMIN'}">
                                        <td> 
                                            <button style="width: 110px;height: 50px;margin-bottom: 20px" type="button" onclick="addToCart(this)" value="${product.id}"  style="margin-top: 10" class="btn btn-danger">Thêm vào giỏ</button>
                                            <c:url value="/user/product/detail" var="productDetail"></c:url>
                                            <form action="${productDetail}" method="GET">
                                                <input type="hidden" name="idProductDetail" value="${product.id}"/>
                                                <button style="width: 110px;height: 50px" class="btn btn-info"> Xem chi tiết</button>
                                            </form>
                                        </td>
                                    </c:if>
                                <input id="sessionId" type="hidden" value="${pageContext.session.id}"/>
                                </tr>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                    </tbody>
                </table>


            </div>

            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <c:url value="/home" var="pagingPrevious">
                        <c:param name="name" value="${param.name}"></c:param>
                        <c:param name="fromPrice" value="${param.fromPrice}"></c:param>
                        <c:param name="toPrice" value="${param.toPrice}"></c:param>
                        <c:param name="idCategory" value="${param.idCategory}"></c:param>
                        <c:param name="page" value="-1"/>
                    </c:url>

                    <c:url value="/home" var="pagingNext">
                        <c:param name="name" value="${param.name}"></c:param>
                        <c:param name="fromPrice" value="${param.fromPrice}"></c:param>
                        <c:param name="toPrice" value="${param.toPrice}"></c:param>
                        <c:param name="idCategory" value="${param.idCategory}"></c:param>
                        <c:param name="page" value="-2"/>
                    </c:url>
                    <c:choose>
                        <c:when test="${applicationScope.countProduct!=0&&sessionScope.countProduct==null}">
                            <c:set var="countProduct" value="${applicationScope.countProduct}"></c:set>
                        </c:when>
                        <c:when test="${sessionScope.countProduct!=null}">
                            <c:set var="countProduct" value="${sessionScope.countProduct}"></c:set>
                        </c:when>
                    </c:choose>
                    <c:forEach begin="${sessionScope.minPage}" end="${sessionScope.maxPage}" var="i" >
                        <c:url value="/home?page=${i}" var="url">
                            <c:param name="name" value="${param.name}"></c:param>
                            <c:param name="fromPrice" value="${param.fromPrice}"></c:param>
                            <c:param name="toPrice" value="${param.toPrice}"></c:param>
                            <c:param name="idCategory" value="${param.idCategory}"></c:param>
                        </c:url>
                        <li class="page-item"><a class="page-link" href="${pageScope.url}">${i}</a></li>
                        </c:forEach>
                </ul>
            </nav>
            <p>Bạn đang ở trang ${sessionScope.currentPage}/${sessionScope.pageTotal}</p> 
        </div><br>
        <footer class="container-fluid text-center">
            <p>Online Store Copyright</p>  
            <form class="form-inline">Get deals:
                <input type="email" class="form-control" size="50" placeholder="Email Address">
                <button type="button" class="btn btn-danger">Sign Up</button>
            </form>
        </footer>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script type="text/javascript">

                                                const addToCart = (button) => {
                                                    let max = document.getElementById("productQuantityCart" + button.value).max
                                                    var productQuantityCart = document.getElementById("productQuantityCart" + button.value);
                                                    var badgeCart = document.getElementById("badgeCart").innerHTML;

                                                    if (productQuantityCart.value == "") {
                                                        productQuantityCart.value = 1;
                                                    } else if (parseInt(productQuantityCart.value) > parseInt(max)) {
                                                        alert("Số lượng quá nhiều")
                                                        return;
                                                    } else if (!productQuantityCart.value.match("^[0-9]+$")) {
                                                        alert("Số lượng phải là số nguyên");
                                                        return;
                                                    }
                                                    productQuantityCart = parseInt(productQuantityCart.value);

                                                    if (badgeCart == "") {
                                                        badgeCart = 0
                                                    }
                                                    badgeCart = parseInt(badgeCart);
                                                    $.ajax({
                                                        url: "http://localhost:8080/HanaShop/ajax/product/addToCart",
                                                        cache: false,
                                                        dataType: 'json',
                                                        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                                                        method: "POST",
                                                        data: {
                                                            idProduct: button.value,
                                                            productQuantityCart: productQuantityCart,
                                                        },
                                                        success: function (data, textStatus, jqXHR) {
                                                            if (data.status == 0) {
                                                                alert("chọn quá số lượng các sản phẩm")
                                                            } else {
                                                                var num = (badgeCart + productQuantityCart)
                                                                console.log(num)
                                                                document.getElementById("badgeCart").innerHTML = num;
                                                                alert("Đã thêm vào giỏ hàng");
                                                                document.getElementsByClassName();
                                                            }
                                                        }
                                                    })
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

                                                const checkCart = () => {

                                                    var num = document.getElementById("badgeCart").innerHTML;
                                                    if (num === "" || num == 0) {
                                                        alert("Không có hàng trong giỏ")
                                                        return false;
                                                    }
                                                    return true;
                                                }
                                                const checkSearch = () => {
                                                    var name = document.getElementById("searchName").value.trim();
                                                    var fromPrice = document.getElementById("searchFromPrice").value.trim();
                                                    var toPrice = document.getElementById("searchToPrice").value.trim();
                                                    var idCategory = document.getElementById("searchIdCategory").value;
                                                    if (name == "" && fromPrice == "" && toPrice == "" && idCategory == "") {
                                                        return false;
                                                    }
                                                    if (fromPrice != "" && toPrice != "") {
                                                        if (fromPrice > toPrice) {
                                                            alert("Số tiền sai")
                                                            return false;
                                                        }
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
                                                $(function () {
                                                    $.ajax({
                                                        url: "http://localhost:8080/HanaShop/user/search/autocomplete",
                                                        method: "GET",
                                                        success: function (data, textStatus, jqXHR) {
                                                            console.log(data)
                                                            $("#searchName").autocomplete({
                                                                source: data,
                                                                minLength: 1,
                                                            });
                                                        }
                                                    })
                                                });
        </script>
    </body>
</html>
