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
        <title>Bootstrap Example</title>
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
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="#">Home</a></li>

                    </ul>

                    <ul class="nav navbar-nav navbar-right">
                        <c:if test="${null eq sessionScope.USER}">
                            <li><a href="<%= request.getContextPath() + "/auth/login"%>"><span class="glyphicon glyphicon-user"></span>Login</a></li>
                            </c:if>
                            <c:if test="${null != sessionScope.USER}">
                            <li>
                                <c:if test="${sessionScope.USER.role != 'ADMIN'}"> 
                                    <a style="margin: 20px" href="<%=request.getContextPath()%>/user/detail" class='btn btn-info'><span class="glyphicon glyphicon-user"></span> Quản lý đơn hàng</a>
                                </c:if>
                            </li>
                            <li>
                                <c:if test="${sessionScope.USER.role eq 'ADMIN'}">  
                                    <a style="margin: 20px" class="btn btn-primary" href="<%=request.getContextPath()%>/product">Trang quản lý</a>
                                </c:if>
                            </li>
                            <li><form style="margin: 20px" action="<%=request.getContextPath()%>/auth/logout" method="POST">
                                    <button style="width: 100px;height: 50px" type="submit" class="btn btn-success">Logout</button>
                                </form></li>
                            </c:if>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container">
            <table class="table">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Tên</th>
                        <th>Số lượng</th>
                        <th>Mô tả</th>
                        <th>Giá</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${sessionScope.cart.cart.values()}" var="product" varStatus="count">
                        <tr>
                            <td>${count.index+1}</td>
                            <td>${product.name}</td>
                            <td>${product.quantityInCart}</td>
                            <td>${product.description}</td>
                            <td>${product.price} USD</td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <th>Tổng tiền</th>
                        <td>${sessionScope.totalReview} USD</td>
                    </tr>
                </tbody>
            </table>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-3"></div>
                <div class="col-md-3"></div>
                <div class="col-md-3">
                    <form method="GET" action="<%=request.getContextPath()%>/user/paypal/excute_payment">
                        <button style="margin-top: 100px;margin-bottom: 100px" class="btn btn-success">Xác nhận thanh toán</button>
                    </form>

                </div>
            </div>
        </div>






        <footer class="container-fluid text-center">
            <p>Online Store Copyright</p>  
            <form class="form-inline">Get deals:
                <input type="email" class="form-control" size="50" placeholder="Email Address">
                <button type="button" class="btn btn-danger">Sign Up</button>
            </form>
        </footer>

    </body>
</html>
