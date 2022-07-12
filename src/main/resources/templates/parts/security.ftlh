<#assign
    known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isAdmin = user.isAdmin()
        currentUserId = user.getId()
    >

<#else>
    <#assign
        name = "unknown"
        isAdmin = false
        currentUserId = -1
    >
</#if>

<#assign
 teacher= Session.SPRING_SECURITY_CONTEXT??
>
<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    isTeacher = user.isTeacher()
    >

<#else>
    <#assign
    name = "unknown"
    isTeacher = false
    >
</#if>
<#assign
user= Session.SPRING_SECURITY_CONTEXT??
>
<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    isUsr = user.isUsr()
    >

<#else>
    <#assign
    name = "unknown"
    isUsr = false
    >
</#if>
