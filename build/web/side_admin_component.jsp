<%-- 
    Document   : side_admin_component
    Created on : Jan 6, 2021, 10:26:23 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div id="layoutSidenav">
    <div id="layoutSidenav_nav">
        <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
            <div class="sb-sidenav-menu">
                <div class="nav">
                    <div class="sb-sidenav-menu-heading">Core</div>
                    <a class="btn btn-success" href="<%=request.getContextPath()%>/home">Trang bán hàng</a>
                    
                    <div class="sb-sidenav-menu-heading"></div>
                    <form action="<%=request.getContextPath()%>/auth/logout" method="POST">
                        <button style="margin-left: 50px" class="btn btn-danger" type="submit" class="dropdown-item">Logout</button>
                    </form>
                    
                  
                </div>
            </div>
            <div class="sb-sidenav-footer">
                <div class="small">Logged in as:</div>
                Phạm Càn Long
            </div>
        </nav>
    </div>
