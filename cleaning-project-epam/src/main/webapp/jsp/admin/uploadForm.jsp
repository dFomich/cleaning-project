
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>File Upload</title>
</head>
<body>
<center>
    <form method="post" action="${pageContext.request.contextPath}/Upload" enctype="multipart/form-data">
        Select file to upload: <input type="file" name="uploadFile" />
        <br/><br/>
        <input type="submit" value="Upload" />
    </form>
</center>
${upload_result}
</body>
</html>
