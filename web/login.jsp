<%-- 
    Document   : home
    Created on : Jan 6, 2021, 12:56:43 AM
    Author     : ASUS
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Bootstrap Simple Login Form</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
        <style>
            .login-form {
                width: 340px;
                margin: 50px auto;
                font-size: 15px;
            }
            .login-form form {
                margin-bottom: 15px;
                background: #f7f7f7;
                box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
                padding: 30px;
            }
            .login-form h2 {
                margin: 0 0 15px;
            }
            .form-control, .btn {
                min-height: 38px;
                border-radius: 2px;
            }
            .btn {        
                font-size: 15px;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="login-form">
            <form action="<c:url value="/auth/login"/>" method="post">
                <h2 class="text-center">Log in</h2>       
                <div class="form-group">
                    <input name="txtUserId" type="text" class="form-control" placeholder="UserID" required="required">
                </div>
                <div class="form-group">
                    <input name="txtPassword" type="password" class="form-control" placeholder="Password" required="required">
                </div>
                <div class="form-group">
                    <button name="action" value="login" type="submit" class="btn btn-primary btn-block">Log in</button>
                </div>
                <div class="form-group">
                    <a class="btn btn-danger" href="https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/HanaShop/auth/login/google&response_type=code
                       &client_id=378885465668-vfed05jhhfhdi8gad3tfjilu8ehcd4ji.apps.googleusercontent.com&approval_prompt=force">Login With Google</a>  

                </div>
                <div class="clearfix">
                    <a href="<%=request.getContextPath()%>/home" class="float-left">Quay về trang chủ</a>
                </div>        
            </form>
            <c:if test="${sessionScope.message!=null}">
                <c:choose>
                    <c:when test="${sessionScope.message.status==false}">
                        <p style="color: red">${sessionScope.message.content}</p>
                    </c:when>
                </c:choose>
                <c:remove var="message" scope="session" /> 
            </c:if>
            <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#exampleModal">Tạo tài khoản</button>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Đăng ký tài khoản</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form onsubmit="return checkFormRegist()" action="<%=request.getContextPath()%>/auth/register" method="POST">
                            <div class="form-group">
                                <label for="recipient-name" class="col-form-label">User name</label>
                                <input name="txtUserId" type="text" class="form-control" id="regisName"/>
                            </div>
                            <div class="form-group">
                                <label for="recipient-name" class="col-form-label">Email</label>
                                <input name="txtEmail" type="text" class="form-control" id="regisEmail">
                            </div>
                            <div class="form-group">
                                <label for="recipient-name" class="col-form-label">Mật khẩu</label>
                                <input name="txtPassword" type="text" class="form-control" id="regisPass">
                            </div>
                            <div class="form-group">
                                <label for="recipient-name" class="col-form-label">Nhập lại mật khẩu</label>
                                <input type="text" class="form-control" id="regisCheckPass">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary">Đăng ký tài khoản</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            const PATTERN_TEXT = "[A-Za-z0-9]{1,100}";
            const PATTERN_EMAIL = "[A-Za-z0-9]+@[A-Za-z0-9]+";
            const checkFormRegist = () => {
                var regisName = document.getElementById("regisName").value.trim();
                var regisEmail = document.getElementById("regisEmail").value.trim();
                var regisPass = document.getElementById("regisPass").value.trim();
                var regisCheckPass = document.getElementById("regisCheckPass").value.trim();
                var check = true;
                if (!regisName.match(PATTERN_TEXT)) {
                    check = false;
                }
                if (!regisEmail.match(PATTERN_EMAIL)) {
                    check = false;
                }
                if (!regisPass.match(PATTERN_TEXT)) {
                    check = false;
                }
                if (!regisCheckPass.match(PATTERN_TEXT)) {
                    check = false;
                }
                if (check) {
                    if (regisPass !== regisCheckPass) {
                        check = false;
                    }
                }
                return check;

            }

        </script>
    </body>
</html>