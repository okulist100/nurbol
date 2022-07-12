<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">


<@c.page>
<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/main" class="form-inline">
            <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Поиск">
            <button type="submit" class="btn btn-primary ml-2">Search</button>
        </form>
        <input type="hidden" name="currentUser" value="${currentUserId}"/>
    </div>
</div>
<div >
<#if isUsr>
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
             <#if isTeacher>
            <div class="form-group">
                <input type="text" class="form-control" name="text" placeholder="Оценка"/>
            </div>
            </#if>
            <div class="form-group">
                <input type="text" class="form-control" name="tag" placeholder="Тема">
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile">Выберите файл</label>
                </div>
            </div>
            <#list messages as message>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>" />
                </#list>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Добавить</button>
            </div>
        </form>
    </div>
    </#if>
</div>
<#if isTeacher>
<div>
    <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Имя файла</th>
                <th scope="col">Оценка</th>
                <th scope="col">Тег</th>
                <th scope="col">Имя студента</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
             <#list messages as message>

            <tr>
                <th scope="row"> </th>
        <#if message.filename??>
                <td> <a href="main/${message.filename}" >${message.filename}</a></td>
        </#if>

                <td>${message.text}</td>
                <td>${message.tag}</td>
                <td>${message.authorName}</td>
                <td>

                      <a class="btn btn-primary" href="/user-messages/${message.author.id}?message=${message.id}">
                            Оценить
                        </a>
                    </td>
            </tr>
            </#list>
            </tbody>
        </table
    <#else>
    No message

</div>
</#if>
</@c.page>
