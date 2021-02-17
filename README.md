
# 定义：

Spring Cloud Config 提供了配置中心的功能，但是需要配合 git、svn 或外部存储（例如各种数据库），可以说是动态获取Git、SVN、本地的配置文件的一种工具。

# 实现一：SpringCloud Config也支持本地参数配置的获取。

如果使用本地存储的方式，在 application.yml 文件添加 spring.profiles.active=native 配置即可，它会从项目的 resources路径下读取配置文件。如果是读取指定的配置文件，那么可以使用 spring.cloud.config.server.native.searchLocations = file:D:/properties/ 来读取。
## 第一步: 在idea中创建项目，包括一个总项目，两个子项目。目录如下:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210214183804455.png#pic_center)
###  根pom如下:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>SpringCloud_config_bus_demo</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>SpringCloud-Config-Server</module>
        <module>SpringCloud-Config-Client</module>
    </modules>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.11.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Greenwich.SR4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
</project>
```
## 第二步，构建Config-Server,目录结构如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210214183950821.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dpbmQxX3JhaW4=,size_16,color_FFFFFF,t_70#pic_center)
### 1、pom 文件:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>SpringCloud_config_bus_demo</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>SpringCloud-Config-Server</artifactId>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```
### 2、启动类:
```java
package com.springcloud.configServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @auther: 坎布里奇
 * @description: xxxxx
 * @date: 2021/2/14 14:39
 * @version: 1.0.0
 */

@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }
}
```
### 3、Config-Server自己的配置文件，包括bootstrap.yml 和 application-prod.yml：
#### bootstrap.yml :
```yaml
server:
  port: 8888
spring:
  application:
    name: springcloud-config-server
  profiles:
    active: native
```
#### application-prod.yml，这个配置，在bootstrap.yml 中 profiles 激活的不是native，才会生效：

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/dream_house_1/MyConfig.git
          username: ********
          password: ********
          default-label: master #配置文件分支
#          search-paths: config  #配置文件所在根目录
```
### 4、客户端项目的配置文件,application-dev.yml:
```yaml
server:
  port: 8889
foo: 123123
```
#### 目录:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210214184353409.png#pic_center)
## 第三步，构建Config-Client,目录结构如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210214184423210.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dpbmQxX3JhaW4=,size_16,color_FFFFFF,t_70#pic_center)
### 1、pom.xml文件:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>SpringCloud_config_bus_demo</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>SpringCloud-Config-Client</artifactId>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```
### 2、启动类:
```java
package com.springcloud.configClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @auther: 坎布里奇
 * @description: xxxxx
 * @date: 2021/2/14 14:43
 * @version: 1.0.0
 */
@SpringBootApplication
public class SpringCloudConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigClientApplication.class, args);
    } 
}
```
### 3、配置文件，bootstrap.yml :

```yaml
spring:
  application:
    name: config-client
  cloud:
    config:
      uri: http://localhost:8888/
      profile: dev
      label: master
```
### 4、ConfigServerConfig,获取配置文件中的内容:

```java
package com.springcloud.configClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @auther: 坎布里奇
 * @description: 配置文件
 * @date: 2021/2/14 17:17
 * @version: 1.0.0
 */
@Component
public class ConfigServerConfig {

    // 获取的是, 配置中心，配置文件中的属性。
    @Value("${foo}")
    String foo;
    public String getFoo() {
        return foo;
    }
    public void setFoo(String foo) {
        this.foo = foo;
    }
}
```
### 5、GitController，为了测试:

```java
package com.springcloud.configClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
  *@auther: 坎布里奇
  *@description: xxxxx
 *@date: 2021/2/14 17:18
 *@version: 1.0.0
 */
@RestController
public class GitController {

    @Autowired
    private ConfigServerConfig configServerConfig;

    @RequestMapping(value = "/hi")
    public Object hi(){
        return configServerConfig;
    }
}
```
## 测试：
### 访问地址:
		http://localhost:8889/hi
### 结果:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210214184846473.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dpbmQxX3JhaW4=,size_16,color_FFFFFF,t_70#pic_center)
#  实现二：SpringCloud Config远程参数配置的获取。
## 客户端不用做任何修改；即Config-Client不做修改；
## Config-Server服务端修改:
### 配置文件目录如下:
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021021418500179.png#pic_center)
### bootstrap.yml修改如下:
```yaml
server:
  port: 8888
spring:
  application:
    name: springcloud-config-server
  profiles:
    active: prod # 这里的native 改为prod.
```
