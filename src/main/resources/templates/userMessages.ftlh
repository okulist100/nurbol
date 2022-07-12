<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">


<@c.page>
<div >
<#if isTeacher>
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                       value="<#if message??>${message.text}</#if>" name="text" placeholder="Введите сообщение" />
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control"
                       value="<#if message??>${message.tag}</#if>" name="tag" placeholder="Тэг">
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile">Choose file</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>" />
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Save message</button>
            </div>
        </form>
    </div>
    </#if>
</div>
<#if isUsr>
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
                    <a class="btn btn-warning" href="/del-user-messages/${message.author.id}?message=${message.id}">Delete</a>
</td>
            </tr>
            </#list>
            </tbody>
        </table
    <#else>
    No message
    </#if>
</@c.page>