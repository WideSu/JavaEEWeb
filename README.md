# vibsignal_analysis

This is a production management ERP project sponsored by the key projects of Quanzhou city's science and technology program. It includes 7 modules: planning progress, equipment management, process monitoring, material monitoring, personnel monitoring, quality control, system Management module.

## The project's SSM architecture(Spring+SpringMVC+Mybatis
- Maven
- Spring（IOC DI AOP Declarative transaction processing）
- SpringMVC（Restful API）
- Hibernate Validate（Parameter verification）
- Mybatis（Minimum configuration scheme）
- [Druid（Data source configuration SQL | anti injection SQL | performance monitoring)](http://wosyingjun.iteye.com/blog/2306139)
- Unified exception handling
- JSP JSTL JavaScript
- shiro Authority control
- Bootstrap

## System structure
![image](https://user-images.githubusercontent.com/44923423/179386879-0af486cd-341b-4462-addd-fdb3d012bad9.png)

## Database Design（详见sql文件）
![image](https://user-images.githubusercontent.com/44923423/179386887-adf46196-62c2-44e4-bca2-732a390727ec.png)

## User Guide
### Login Interface

To log in, you can use the account number: 22 and the password: 22 to log in as a super administrator. If you enter the wrong password, you will need to enter the verification code next time you log in.

![image](https://user-images.githubusercontent.com/44923423/179386911-6d30b6d7-eb1f-4a6c-8859-d88da6268a5e.png)

### Main Menu

The super administrator can display the system management module to assign and manage system permissions, and other roles can view the information of the remaining modules except system management (including downloading attachments, viewing pictures, etc.), but can only maintain the information within the corresponding permissions of the role. The function search bar on the left can be used for function fuzzy search.

![image](https://user-images.githubusercontent.com/44923423/179386992-4c2167b2-b08d-4315-9c65-adc7d92288ce.png)

The super administrator can display the system management module to assign and manage system permissions, and other roles can view the information of the remaining modules except system management (including downloading attachments, viewing pictures, etc.), but can only maintain the information within the corresponding permissions of the role. The function search bar on the left can be used for function fuzzy search.

### Upload images

For the configuration of image upload, please refer to the notes at the end of the document. The size of the image must not exceed 1M, and images in various formats such as jpg and png are supported. After the upload is successful, it can be displayed in the corresponding display column.
![image](https://user-images.githubusercontent.com/44923423/179387013-af1f9f1a-d7bf-4708-a99e-22562b619c53.png)

### Upload files

The file upload uses an open source jQuery file upload plug-in. You can modify the parameters of the uploaded file in common.js, including the number of uploads, the supported file types, etc. The configuration information is as follows:

```{python}
url:"file/upload", 
maxFileCount: 5, //上传文件个数（多个时修改此处 
returnType: 'json', //服务返回数据 
allowedTypes: 'doc,docx,excel,sql,txt,ppt,pdf', //允许上传的文件式 
showDone: false, //是否显示"Done"(完成)按钮 
showDelete: true, //是否显示"Delete"(删除)按钮
```
![image](https://user-images.githubusercontent.com/44923423/179387111-140e17eb-a630-4fb4-8206-f3c16da0a625.png)

### Rich text editing

The system uses the open source `KindEditor` rich text editor, which is a set of online HTML editors, mainly used to allow users to obtain WYSIWYG editing effects on the website. KindEditor replaces the traditional multi-line text input box (textarea) with a visual rich text input box.

**KindEditor main features:**

- Fast: small in size and fast to load 
- Open source
- Easy to customize: built-in custom DOM class library, precise operation of DOM 
- Scalable: plug-in-based design, all functions are plug-ins, and functions can be increased or decreased according to needs 
- Easy to change style: easy to modify the editor style, just modify one CSS file 
- Compatible for major browsers: Support most major browsers, such as IE, Firefox, Safari, Chrome, Opera

![image](https://user-images.githubusercontent.com/44923423/179390335-ad090db6-4ce8-4595-91f0-62d85b8d647a.png)

### Related information

The information of the associated object, click to display in the form of a pop-up window, if you have the modification permission corresponding to the module, you can also maintain the information here.

![image](https://user-images.githubusercontent.com/44923423/179390372-31fc5018-e856-4f24-aafb-d1d1fa7e043c.png)

### Search box

You can select query conditions in the search box in the upper right corner and enter keywords to perform fuzzy search of corresponding information.

![image](https://user-images.githubusercontent.com/44923423/179390389-705c591d-3294-4b30-94d2-769cbabde7ea.png)

### Session expired jump to login

After the user logs in, the corresponding session will be established. The default expiration time of the system is 10 minutes. If you need to change it, you can change the following configuration in the applicationContextshiro.xml configuration file.

```{xml}
<!-- 会话管理器 -->

<bean id="sessionManager" class="org.apache.shiro.web.session.mgt.D

efaultWebSessionManager"> <!-- session的失效时长，单位毫秒 ，这里设置为10分钟-->

<property name="globalSessionTimeout" value="600000"/>

<!-- 删除失效的session -->

<property name="deleteInvalidSessions" value="true"/>

<!-- 指定本系统sessionId, 默认为: JSESSIONID 问题: 与Servlet容器名冲 突, 如Jetty, Tomcat等默认JSESSIONID,

当跳出shiro Servlet时如Error-page容器会为JSESSIONID重新分配值导致 登录会话丢失! -->

<property name="sessionIdCookie" ref="sessionIdCookie"/>

</bean>
```

If the session expires, you should jump to the login interface to log in again. The method adopted by the system is to set a session filter, as follows:
```{java}
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {
    public static void main(String args[]) {

        // String to be scanned to find the pattern.
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(.*)(\\d+)(.*)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
        } else {
            System.out.println("NO MATCH");
        }
    }
}
```
