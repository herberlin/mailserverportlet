<%@page pageEncoding="UTF-8" contentType="text/html; ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui"%>

<portlet:defineObjects />
<portlet:actionURL name="startServer" var="startServerUrl">
	<portlet:param name="configTab" value="${configTab}"/>
</portlet:actionURL>
<portlet:actionURL name="stopServer" var="stopServerUrl">
	<portlet:param name="configTab" value="${configTab}"/>
</portlet:actionURL>
<portlet:actionURL name="configure" var="configureUrl">
	<portlet:param name="configTab" value="${configTab}"/>
</portlet:actionURL>
<portlet:actionURL name="sendTestmail" var="testmailUrl">
	<portlet:param name="configTab" value="log"/>
</portlet:actionURL>
<portlet:renderURL var="refreshUrl">
	<portlet:param name="configTab" value="log"/>
</portlet:renderURL>


<c:choose>
	<c:when test="${serverRunning }">
		<liferay-ui:message key="server-running" /> ${serverPort }.
	</c:when>
	<c:otherwise>
		<liferay-ui:message key="server-not-running" />
	</c:otherwise>
</c:choose>
<aui:button-row>
	<aui:button disabled="${serverRunning }" value="start-server"
		onClick="location.href='${startServerUrl}'" />
	<aui:button disabled="${not serverRunning }" value="stop-server"
		onClick="location.href='${stopServerUrl}'" />
	<aui:button value="refresh" onClick="location.href='${refreshUrl}'" />
	<aui:button value="testmail" onClick="location.href='${testmailUrl}'" />
</aui:button-row>

<liferay-ui:tabs param="configTab" names="log,config" url="${refreshUrl}">



	<liferay-ui:section>
		<%-- show  --%>
		
		<c:forEach var="event" items="${eventList}" varStatus="varStatus">
		
			<div class="mail-server-portlet-mail" >
				<strong><c:out value="${event.date}"/> : 
				<c:out value="${event.from}"/> :
				<c:out value="${event.subject}"/> </strong>
				<div id="row-${varStatus.index}">
						
							<dl  style="margin:5px 0px 10px 18px">
								<dt>Recipients</dt>
								<dd><c:out value="${event.to}"/></dd>

								<dt>Header</dt>
								<dd>
									<table>
							<c:forEach var="entry" items="${event.mailHeader.headers}">
								<tr>
									<td><c:out value="${entry.key }" />&nbsp;</td>
									<td><c:out value="${entry.value }" /></td>
								</tr>
							</c:forEach>
								</table>
								</dd>

								<dt>Body</dt>
								<dd>
									<textarea class="hide" id="text-${varStatus.index }" style="width:500px;height:100px"><c:out value="${event.mailHeader.content}"/></textarea>
									<div id="html-${varStatus.index }" style="width:500px; height:100px; border:1px solid silver;resize:both;overflow:auto;">
										${event.mailHeader.content}
									</div>
									<div><a href="javascript:toggleThings(${varStatus.index })">Toggle Html / Plain</a></div>
									
								</dd>
							</dl>									

				</div>
			</div>
		</c:forEach>

	</liferay-ui:section>
	<liferay-ui:section>
		<aui:form method="post" action="${configureUrl}">
			<aui:input name="host" value="${host }">
				<aui:validator name="required" />
			</aui:input>
			<aui:input name="port" value="${port}">
				<aui:validator name="required" />
				<aui:validator name="digits" />
			</aui:input>
			<aui:input type="checkbox" name="autostart" checked="${autostart }"></aui:input>
			<aui:button name="submit" type="submit" />
		</aui:form>
	</liferay-ui:section>

	<script>
		function toggleThings(num){
			var t = document.getElementById("text-"+num);
			var h = document.getElementById("html-"+num);
			if (t){
				if (t.classList.contains("hide")) {
					t.classList.remove("hide");
					h.classList.add("hide");
				} else {
					t.classList.add("hide");
					h.classList.remove("hide");
				}
			}
		}

		<c:if test="${configTab eq 'log'}">
			// var interval = window.setTimeout(function() {
			// 	location.href="${refreshUrl}";
			// }, 3000);
			
			window.addEventListener("scroll", function(){
				window.clearInterval(interval);
			}); 

		</c:if>
	</script>
</liferay-ui:tabs>

