<%@ include file="../../../../includes.jspf"%>
<%--
  ~ Copyright 2015 Karl Dahlgren
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<c:url var="create_rest_application_url"  value="/web/rest/project/${restProjectId}/create/application" />
<div class="content-top">
    <h1><spring:message code="rest.createapplication.header.application"/></h1>
</div>
<form:form action="${create_rest_application_url}" method="POST" commandName="restApplication">
    <table class="formTable">
        <tr>
            <td class="column1"><label><spring:message code="rest.createapplication.label.name"/></label></td>
            <td class="column2"><form:input id="restApplicationNameInput" path="name" /></td>
        </tr>
    </table>
 
    <button class="button-success pure-button" type="submit" name="submit"><i class="fa fa-plus"></i> <span><spring:message code="rest.createapplication.button.createapplication"/></span></button>
    <a href="<c:url value="/web/rest/project/${restProjectId}"/>" class="button-error pure-button"><i class="fa fa-times"></i> <span><spring:message code="rest.createapplication.button.cancel"/></span></a>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>
<script>
    $("#restApplicationNameInput").attr('required', '');
</script>
