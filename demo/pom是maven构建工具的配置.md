定义 groupId、artifactId、version，唯一标识项目
声明项目所需的所有依赖库（如 Spring Boot Starter、数据库驱动等）
通过 <parent> 继承 Spring Boot 父 POM，统一管理版本



<!-- <?xml version="1.0" encoding="UTF-8"?> -->
<project xmlns="http://maven.apache.org/POM/4.0.0">
 
    <!-- 继承 Spring Boot 父 POM -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <!-- 项目坐标 -->
    <groupId>com.example</groupId>
    <artifactId>my-project</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <!-- 属性配置 -->
    <properties>
        <java.version>17</java.version>
        <mysql.version>8.0.33</mysql.version>
    </properties>
    
    <!-- 依赖管理 -->
    <dependencies>
        <!-- Spring Boot Web 启动器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- 数据库相关 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
        
        <!-- 测试 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <!-- 构建插件 -->
    <build>
        <plugins>
            <!-- Spring Boot Maven 插件：支持打包可执行 JAR -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
    
</project>