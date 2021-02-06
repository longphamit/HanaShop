<%-- 
    Document   : admin
    Created on : Jan 6, 2021, 8:17:52 AM
    Author     : ASUS
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Bảng quản lý</title>
        <link href="css/styles.css" rel="stylesheet" />
        <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" crossorigin="anonymous" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/js/all.min.js" crossorigin="anonymous"></script>
        <script>
            sessionStorage.setItem("currentPage", 0);
        </script>
    </head>
    <body class="sb-nav-fixed">
        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <a class="navbar-brand" href="#">Hana Shop</a>
            <button class="btn btn-link btn-sm order-1 order-lg-0" id="sidebarToggle" href="#"><i class="fas fa-bars"></i></button>
        </nav>
        <%@include file="side_admin_component.jsp" %>
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid">
                    <h1 class="mt-4">Bảng Quản Lý --ADMIN: ${sessionScope.USER.name} ---</h1>
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
                    <div>
                        <input id="searchName" type="text" placeholder="tên"/>
                        <input id="searchFromPrice" type="number" placeholder="from price"/>
                        <input id="searchToPrice" type="number" placeholder="to price"/>
                        <select id="searchIdCategory">
                            <option value=${null}>NONE</option>
                            <c:forEach items="${applicationScope.listCategories}" var="category">
                                <option value="${pageScope.category.id}">${pageScope.category.name}</option>
                            </c:forEach>
                        </select>
                        <button style="margin: 20px" type="button" onclick="search()" class="btn btn-info">Tìm kiếm</button>
                        <button style="margin: 20px" type="button" onclick="getAll(0);document.getElementById('pagingBar').style.visibility = 'visible';document.getElementById('pagingBarAjax').style.visibility = 'hidden'" class="btn btn-info">Tất cả</button>
                    </div>


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
                    <button style="margin: 20px" type="button" class="btn btn-success" onclick="asyncData()">Đồng bộ dữ liệu</button>            
                    <button style="margin: 20px" type="button" class="btn btn-success" data-toggle="modal" data-target="#addProductModal">Thêm sản phẩm</button>
                    <button style="margin: 20px" type="button" class="btn btn-success" data-toggle="modal" data-target="#addCategoryModal">Thêm loại sản phẩm</button>
                    <a style="margin: 20px" class="btn btn-info" href="<%=request.getContextPath()%>/admin/vieworder">Xem đơn hàng</a>
                    <!-- Modal -->
                    <div class="modal fade" id="addProductModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <c:url value="/product/add" var="addProductAction"></c:url>
                            <form onsubmit="return checkAdd()" action="${addProductAction}" method="POST" enctype="multipart/form-data">
                                <div class="modal-content">

                                    <div class="modal-header">
                                        <h5 class="modal-title" id="exampleModalLabel">Thêm sản phẩm</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label for="recipient-name" class="col-form-label">Tên sản phẩm</label>
                                            <input name="txtProductName" type="text" class="form-control" id="recipient-name" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="recipient-name" class="col-form-label">Giá </label>
                                            <input name="txtProductPrice" step="0.01" type="number" min="1" required max="1000000" class="form-control" id="recipient-name" placeholder="1 - 1 tỷ">
                                        </div>
                                        <div class="form-group">
                                            <label for="recipient-name" class="col-form-label">Số lượng</label>
                                            <input id="addQuantity" name="txtProductQuantity" type="number" min="1" required max="1000000" class="form-control" id="recipient-name" placeholder="1 - 1 triệu">
                                        </div>
                                        <div class="form-group">
                                            <label for="message-text" class="col-form-label">Mô tả</label>
                                            <textarea name="txtProductDesc" class="form-control" id="message-text"></textarea>
                                        </div>

                                        <div>
                                            <label>Loại mặt hàng</label>
                                            <select name="txtProductCategory" id="cars">
                                                <c:forEach items="${applicationScope.listCategories}" var="category">
                                                    <option value="${pageScope.category.id}">${pageScope.category.name}</option>>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label for="message-text" class="col-form-label">Hình ảnh</label>
                                            <input name="txtImage" required accept="image/*"  type="file" class="form-control" id="message-text"/>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                        <button name="action" value="addProduct" type="submit" class="btn btn-success">Lưu</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal fade" id="addCategoryModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <c:url value="/product/category/add" var="addCategoryAction"></c:url>
                            <form onsubmit="return checkAddCategory()"  action="${addCategoryAction}" method="POST">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="exampleModalLabel">Thêm loại sản phẩm</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label for="recipient-name" class="col-form-label">Tên loại sản phẩm</label>
                                            <input id="addNameCategory" name="txtNameCategory" type="text" class="form-control" id="recipient-name">
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                        <button  name="action" value="addCategory" type="submit" class="btn btn-success">Lưu</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal fade" id="updateProductModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLabel">Sửa thông tin sản phẩm</h5>
                                    <button type="button" class="close" aria-label="Close" onclick="location.reload();">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <c:url value="/ajax/product/update" var="productUpdateAction"></c:url>
                                    <form method="POST" action="${productUpdateAction}" onsubmit="return checkUpdate()">
                                        <div class="form-group">
                                            <label for="recipient-name" class="col-form-label">Tên loại sản phẩm</label>
                                            <input name="name" type="text" class="form-control" id="productNameDetail">
                                            <input name="id" id="productIdDetail" type="hidden"/>
                                        </div>
                                        <div class="form-group">
                                            <label for="recipient-name" class="col-form-label">Giá</label>
                                            <input  name="price" type="number" min="1" max="1000000" step="0.01" class="form-control" id="productPriceDetail" >
                                        </div>
                                        <div class="form-group">
                                            <label for="recipient-name" class="col-form-label">Số lượng</label>
                                            <input name="quantity" type="number" min="0" max="1000000" class="form-control" id="productQuantityDetail" >
                                        </div>
                                        <div class="form-group">
                                            <label for="message-text" class="col-form-label">Mô tả</label>
                                            <textarea name="desc" class="form-control" id="productDescDetail"></textarea>
                                        </div>
                                        <div>
                                            <label>Loại mặt hàng</label>
                                            <select name="type" id="productTypeDetail">
                                                <c:forEach items="${applicationScope.listCategories}" var="category">
                                                    <option id="${pageScope.category.id}" value="${pageScope.category.id}">${pageScope.category.name}</option>>
                                                </c:forEach>
                                            </select>
                                            <button style="margin-bottom: 30px" name="button" value="addCategory" type="submit" class="btn btn-success float-right">Update Thông tin</button>
                                        </div>

                                    </form>
                                    <form method="POST" action="<%=request.getContextPath()%>/ajax/product/update_image" enctype="multipart/form-data">
                                        <div class="form-group">
                                            <label for="message-text" class="col-form-label">Hình ảnh</label>
                                            <input id="imageUpdate" name="txtImage" required accept="image/*"  type="file" class="form-control" id="message-text"/>
                                        </div>
                                        <input name="id" id="productIdDetailImage" type="hidden"/>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" onclick="location.reload();">Close</button>
                                            <button name="button" value="addCategory" type="submit" class="btn btn-success">Update Hình ảnh</button>
                                        </div>
                                    </form>

                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="card mb-4">
                        <div class="card-header">
                            <i class="fas fa-table mr-1"></i>
                            DataTable Example
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>Tên</th>
                                            <th>Giá</th>
                                            <th>Ngày tạo</th>
                                            <th>Ngày sửa</th>
                                            <th>Tình trạng</th>
                                            <th>Số lượng</th>
                                            <th>Loại mặt hàng</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tfoot>
                                        <tr>
                                            <th>Tên</th>
                                            <th>Giá</th>
                                            <th>Ngày tạo</th>
                                            <th>Ngày sửa</th>
                                            <th>Tình trạng</th>
                                            <th>Số lượng</th>
                                            <th>Loại mặt hàng</th>
                                            <th></th>
                                            <th>
                                                <button onclick="removeProduct()" class='btn btn-danger'>Xóa</button>
                                            </th>
                                        </tr>
                                    </tfoot>
                                    <tbody id="tableBody">
                                    </tbody>
                                    <nav id="pagingBar" aria-label="Page navigation example">
                                        <ul class="pagination">
                                            <input id="countProduct" type="hidden" value="${applicationScope.countProduct}"/>
                                            <c:if test="${applicationScope.countProduct>5}" var="checkCount">
                                                <c:set var="remain" value="${applicationScope.countProduct%5}"/>
                                                <c:if test="${remain ==0}">
                                                    <c:set var="temp" value="${applicationScope.countProduct/5}"/>
                                                </c:if>
                                                <c:if test="${remain !=0}">
                                                    <c:set var="temp" value="${applicationScope.countProduct/5 +1}"/>
                                                </c:if>
                                            </c:if>

                                            <c:if test="${pageScope.checkCount !=true}">
                                                <c:set var="temp" value="${1}"/>
                                            </c:if>

                                            <li class="page-item"><a onclick="getPage(-1)" class="page-link">Previous</a></li>
                                                <c:forEach  begin="1" end="${pageScope.temp}" var="i">
                                                <li class="page-item"><a onclick="getPage('${i-1}')" class="page-link">${i}</a></li>
                                                </c:forEach>
                                            <li class="page-item"><a onclick="getPage(-2)" class="page-link">Next</a></li>
                                        </ul>
                                    </nav>
                                    <nav  style="visibility: hidden"  id="pagingBarAjax" aria-label="Page navigation example">
                                        <ul class="pagination">
                                            <li class="page-item"><a onclick="getPage(-1)" class="page-link">Previous</a></li>
                                            <ul class="pagination" id="pagingLinkAjax">

                                            </ul>    
                                            <li class="page-item"><a onclick="getPage(-2)" class="page-link">Next</a></li>
                                        </ul>
                                    </nav>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
            <div id="logModal" class="modal fade" role="dialog">
                <div class="modal-dialog modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Product Log</h4>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <table  class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                    <tr>
                                        <th>Người người cập nhật</th>
                                        <th>Thời gian cập nhật</th>
                                        <th>Nội dung cập nhật</th>
                                    </tr>     
                                </thead>
                                <tbody id="logBodyModal">

                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>
            <footer class="py-4 bg-light mt-auto">
                <div class="container-fluid">
                    <div class="d-flex align-items-center justify-content-between small">
                        <div class="text-muted">Copyright &copy; Your Website 2020</div>
                        <div>
                            <a href="#">Privacy Policy</a>
                            &middot;
                            <a href="#">Terms &amp; Conditions</a>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    </div>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script src="js/scripts.js"></script>
    <script type="text/javascript">

                                                const checkAddCategory = () => {
                                                    if (document.getElementById("addNameCategory").value.trim() == "") {
                                                        alert("Bạn nhập sai định dạng")
                                                        return false;
                                                    }
                                                    return true;
                                                }
                                                const checkAdd = () => {
                                                    if (!document.getElementById("addQuantity").value.match("^[0-9]+$")) {
                                                        num = 1;
                                                        alert("Bạn nhập sai định dạng số lượng");
                                                        return false;
                                                    }
                                                    return true;
                                                }
                                                const generateRow = (data) => {
                                                    var tableBody = $("#tableBody");
                                                    var row = "";
                                                    var count = 0;
                                                    $.each(data, (i, item) => {
                                                        count += 1;
                                                        row += '<tr>'
                                                        row += '<td>' + item.name + '</td>'
                                                        row += '<td>' + item.price + " USD" + '</td>'
                                                        row += '<td>' + item.createDate + '</td>'
                                                        row += '<td>' + '<button class="btn btn-primary" data-toggle="modal" onClick="getLog(' + "'" + item.id + "'" + ')" data-target="#logModal">Xem log</button></td>'
                                                        item.status == 1
                                                                ? row += '<td>' + "active" + '</td>'
                                                                : row += '<td>' + "unactive" + '</td>'
                                                        row += '<td>' + item.quantity + '</td>'
                                                        row += '<td>' + item.categoryName + '</td>'
                                                        row += '<input id="idProduct' + count + '"' + ' type="hidden" value="' + item.id + '"/>'
                                                        row += '<td>' + '<button class="btn btn-primary" data-toggle="modal" onClick="getDetail(' + count + ')" data-target="#updateProductModal">Sửa</button></td>'
                                                        item.status == 1
                                                            ?row += "<td>" + "<input style='margin-left:20px' value=" + item.id + " class='form-check-input remove-product' type='checkbox'/>" + "</td>"
                                                            :row +="<td><button class='btn btn-success' value=" + item.id + " onclick='activeProduct(this)' >active</button></td>"
                                                        row += '</tr>'
                                                    });
                                                    tableBody.html(row);
                                                }
                                                const getProductList = (num) => {
                                                    var name = document.getElementById("searchName").value.trim();
                                                    var fromPrice = document.getElementById("searchFromPrice").value.trim();
                                                    var toPrice = document.getElementById("searchToPrice").value.trim();
                                                    var idCategory = document.getElementById("searchIdCategory").value;
                                                    
                                                    var currentPage = sessionStorage.getItem("currentPage");
                                                    $.ajax({
                                                        url: "http://localhost:8080/HanaShop/ajax/product",
                                                        method: "GET",
                                                        cache: false,
                                                        dataType: 'json',
                                                        contentType: 'application/json',
                                                        data: {
                                                            page: num,
                                                            currentPage: currentPage,
                                                            name: name,
                                                            fromPrice: fromPrice,
                                                            toPrice: toPrice,
                                                            idCategory: idCategory
                                                        },
                                                        success: function (data, textStatus, jqXHR) {
                                                            if (num == -1) {
                                                                sessionStorage.setItem("currentPage", parseInt(sessionStorage.getItem("currentPage")) - 1)
                                                            } else if (num == -2) {
                                                                sessionStorage.setItem("currentPage", parseInt(sessionStorage.getItem("currentPage")) + 1);
                                                            } else {
                                                                sessionStorage.setItem("currentPage", num);
                                                            }
                                                            generateRow(data)

                                                        },
                                                        error: function () {
                                                            alert("something went wrong");
                                                        }
                                                    })
                                                }
                                                const getPage = (num) => {
                                                    var countProduct = document.getElementById("countProduct").value;
                                                    var currentPage = sessionStorage.getItem("currentPage");
                                                    if (countProduct / 5 - currentPage >= 0 && countProduct / 5 - currentPage <= 1) {
                                                        if (num != -2) {
                                                            getProductList(num)
                                                        } else {
                                                            alert("Hết sản phẩm");
                                                        }
                                                    } else {
                                                        if (currentPage == 0 && num == -1) {
                                                            alert("Không có sản phẩm");
                                                        } else {
                                                            getProductList(num)
                                                        }
                                                    }
                                                }
                                                const getDetail = (count) => {
                                                    var id = document.getElementById("idProduct" + count).value;
                                                    $.ajax({
                                                        url: "http://localhost:8080/HanaShop/ajax/product/detail",
                                                        method: "GET",
                                                        cache: false,
                                                        dataType: 'json',
                                                        contentType: 'application/json',
                                                        data: {
                                                            id: id
                                                        },
                                                        success: function (data, textStatus, jqXHR) {
                                                            var productNameDetailBox = document.getElementById("productNameDetail");
                                                            var productPriceDetailBox = document.getElementById("productPriceDetail");
                                                            var productQuantityDetailBox = document.getElementById("productQuantityDetail");
                                                            var productDescDetailBox = document.getElementById("productDescDetail");
                                                            var productIdDetailBox = document.getElementById("productIdDetail");
                                                            var productIdDetailImageBox = document.getElementById("productIdDetailImage");
                                                            var productOptionType=document.getElementById(data.categoryId)
                                                            productNameDetailBox.value = data.name;
                                                            productPriceDetailBox.value = data.price;
                                                            productQuantityDetailBox.value = data.quantity;
                                                            productDescDetailBox.value = data.description;
                                                            productIdDetailBox.value = data.id;
                                                            productIdDetailImageBox.value = data.id;
                                                            productOptionType.selected=true
                                                        },
                                                        error: function () {
                                                            alert("something went wrong");
                                                        }
                                                    })
                                                }
                                                $(document).ready(getProductList(0))
                                                const checkUpdate = () => {
                                                    var flag=1;
                                                    var mess="";
                                                    var productNameDetailBox = document.getElementById("productNameDetail");
                                                    var productPriceDetailBox = document.getElementById("productPriceDetail");
                                                    var productQuantityDetailBox = document.getElementById("productQuantityDetail");
                                                    var productDescDetailBox = document.getElementById("productDescDetail");
                                                    var productTypeDetailBox = document.getElementById("productTypeDetail")
                                                    var productIdDetailBox = document.getElementById("productIdDetail");
                                                    if (!productQuantityDetailBox.value.match("^[0-9]+$")) { 
                                                        mess+="Bạn nhập sai định dạng số lượng\n";
                                                        flag=0;
                                                    }
                                                    if(!productPriceDetailBox.value.match("^[0-9]+$")){
                                                        mess+="Bạn nhập sai định dạng tiền\n";
                                                        flag=0;
                                                    }
                                                    if(productNameDetailBox.value.trim()==""){
                                                        flag=0;
                                                        mess+="Vui lòng không bỏ trống tên\n";
                                                    }
                                                    if(productDescDetailBox.value.trim()==""){
                                                        flag=0;
                                                        mess+="Vui lòng không bỏ trống mô tả\n";
                                                    }
                                                  
                                                    if(flag==0){
                                                        alert(mess)
                                                        return false;
                                                    }
                                                    return true;

                                                }

                                                const search = () => {
                                                    var name = document.getElementById("searchName").value.trim();
                                                    var fromPrice = document.getElementById("searchFromPrice").value.trim();
                                                    var toPrice = document.getElementById("searchToPrice").value.trim();
                                                    var idCategory = document.getElementById("searchIdCategory").value;
                                                    if (checkSearch()) {
                                                        $.ajax({
                                                            url: "http://localhost:8080/HanaShop/ajax/product/search",
                                                            method: "GET",
                                                            cache: false,
                                                            dataType: 'json',
                                                            contentType: 'application/json',
                                                            data: {
                                                                name: name,
                                                                fromPrice: fromPrice,
                                                                toPrice: toPrice,
                                                                idCategory: idCategory
                                                            },
                                                            success: function (data, textStatus, jqXHR) {
                                                                if (data.result.length > 0) {
                                                                    generateRow(data.result);
                                                                    alert("đã tìm thấy");
                                                                    document.getElementById("pagingBar").style.visibility = "hidden";
                                                                    document.getElementById("pagingBarAjax").style.visibility = "visible";

                                                                    var numPage = "";
                                                                    if (data.countProduct > 5) {
                                                                        if (data.countProduct % 5 == 0) {
                                                                            numPage = data.countProduct / 5
                                                                        } else {
                                                                            numPage = data.countProduct / 5 + 1;
                                                                        }
                                                                    } else {
                                                                        numPage = 1;
                                                                    }
                                                                    var i = 1
                                                                    var pagingBoxLinkAjax = $("#pagingLinkAjax");
                                                                    var row = "";
                                                                    for (i = 1; i <= numPage; i++) {
                                                                        row += "<li class='page-item'><a onclick='getPage(" + (i - 1) + ")' class='page-link'>" + i + "</a></li>"
                                                                    }
                                                                    pagingBoxLinkAjax.html(row);
                                                                    document.getElementById("countProduct").value = data.countProduct;
                                                                } else {
                                                                    alert("không tìm thấy");
                                                                }
                                                            },
                                                            error: function () {
                                                                alert("something went wrong");
                                                            }

                                                        })
                                                    } else {
                                                        alert("Không thể tạo tìm kiếm kiểm tra lại dữ liệu");
                                                    }

                                                }
                                                const checkSearch = () => {
                                                    var name = document.getElementById("searchName").value.trim();
                                                    var fromPrice = document.getElementById("searchFromPrice").value.trim();
                                                    var toPrice = document.getElementById("searchToPrice").value.trim();
                                                    var idCategory = document.getElementById("searchIdCategory").value;
                                                    if (name == "" && fromPrice == "" && toPrice == "" && idCategory == "") {
                                                        return false;
                                                    }
                                                    if(!fromPrice == "" && !toPrice == ""){
                                                        if(fromPrice>toPrice){
                                                            alert("Số tiền sai")
                                                            return false;
                                                        }
                                                    }
                                                    return true;
                                                }
                                                const removeProduct = () => {
                                                    var choose = confirm("Bạn có muốn xóa");
                                                    if (choose == true) {
                                                        var checkBoxs = document.getElementsByClassName("remove-product");
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
                                                                url: "http://localhost:8080/HanaShop/ajax/product/remove",
                                                                method: "POST",
                                                                cache: false,
                                                                contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                                                                data: {
                                                                    removeArray: removeArray,
                                                                },
                                                                success: function (data, textStatus, jqXHR) {
                                                                    if (data.status == 1) {
                                                                        alert("Đã xóa thành công");
                                                                    }
                                                                    location.reload();
                                                                },
                                                                error: function () {
                                                                    alert("some thing went wrong");
                                                                }
                                                            })
                                                        }

                                                    }
                                                }
                                                const getLog = (id) => {
                                                    $.ajax({
                                                        url: "http://localhost:8080/HanaShop/ajax/product/log_update?idProduct=" + id,
                                                        method: "GET",
                                                        cache: false,
                                                        success: function (data, textStatus, jqXHR) {
                                                            var body = $("#logBodyModal");
                                                            console.log(body)
                                                            var row = "";
                                                            console.log(data)
                                                            $.each(data, (i, item) => {
                                                                row += "<tr>"
                                                                row += "<td>" + item.userName + "</td>"
                                                                row += "<td>" + item.updateDate + "</td>"
                                                                row += "<td>" + item.content + "</td>"
                                                                row += "</tr>"
                                                            })
                                                            body.html(row);
                                                        },
                                                        error: function () {
                                                            alert("Some thing went wrong");
                                                        }
                                                    });
                                                }
                                                const activeProduct=(button)=>{
                                                    
                                                    $.ajax({
                                                        url: "http://localhost:8080/HanaShop/ajax/product/active?productId=" + button.value,
                                                        method: "GET",
                                                        cache: false,
                                                        success: function (data, textStatus, jqXHR) {
                                                            alert("Đã active thành công");
                                                            location.reload();
                                                        },
                                                        error: function () {
                                                            alert("Some thing went wrong");
                                                        }
                                                    });
                                                }
                                                const asyncData=()=>{
                                                    $.ajax({
                                                        url: "http://localhost:8080/HanaShop/ajax/product/async_data",
                                                        method: "GET",
                                                        cache: false,
                                                        success: function (data, textStatus, jqXHR) {
                                                            alert("Đã async thành công");
                                                            location.reload();
                                                        },
                                                        error: function () {
                                                            alert("Some thing went wrong");
                                                        }
                                                    })
                                                }
                                                const getAll=(num)=>{
                                                    document.getElementById("searchName").value="";
                                                    document.getElementById("searchFromPrice").value="";
                                                    document.getElementById("searchToPrice").value="";
                                                    document.getElementById("searchIdCategory").value="";
                                                    getProductList(num)
                                                }


    </script>
</body>
</html>
