# springboot快速使用+常用三方使用

## 1.环境配置/简单理解后端

+ 环境准备
  + 下载安装jdk:https://www.oracle.com/java/technologies/javase-downloads.html
  + 下载安装idea:https://www.jetbrains.com/idea/
+ Rap2平台:http://rap2.taobao.org/

+ 后端一般做什么事?
  + 根据前端要求提供数据(json)
  + 实时提供数据(socket)
  + 权限控制(用户权限)
  + ....

## 2.第一个接口数据

+ 创建项目(使用全默认,调整好自己的项目路径)

+ 重要文件目录

  + src:项目主文件
    + main->java:java文件
      + 包下有一个自动生成入口文件xxxApplication
    + resources:项目配置文件
      + 默认:application.properties
      + application.yaml(格式不一样)
      + ....
  + target:打包生成文件
  + pom:项目插件/打包配置文件

+ 常见文件夹

  + java包下创建
    + config:配置
    + entity:实体对象管理
    + dao:操作数据库管理模块
    + service:服务管理(主要逻辑处理)
    + controller文件夹:接口管理
    + utils:工具包
    + ....

+ 创建第一个简单接口controller创建一个MyUser.java

  ```java
  @Controller //声明这是一个Controller类
  //@RestController //结合了ResponseBody和Controller
  public class MyUser {
      //准备一个接口
      @ResponseBody   //将java对象转为json格式的数据。
      @RequestMapping("/hello")
      public String testHello(){
          return "spring";
      }
  }
  ```

  > 简单理解:
  >
  > + 在类上加@Controller(bean,Component...)注解，来向spring声明这是一个组件
  >
  > + 启动项目时会自动配置到一个**bean容器**里
  > + 使用的时候使用**@Autowired**可以注入直接使用

+ 配置文件application.properties

  ```properties
  # 应用名称
  spring.application.name=test-demo
  # 应用服务 WEB 访问端口
  server.port=8080
  #访问路径
  server.servlet.context-path=/spring
  #....
  ```


## 3.Jpa+MySql数据库(上)

+ 简单理解数据库(wps里表格)

+ 安装相应软件

  + MySql服务
    + 官网:https://www.mysql.com/cn/services/
    + 安装方法:https://www.cnblogs.com/honeynan/p/12408119.html
  + Xampp:管理服务开启状态,(根据自己需要安装)
    + 官网:https://www.apachefriends.org/index.html
  + navicat查看MySql数据
    + 官网:http://www.navicat.com.cn/
  + postman:测试接口
    + 官网:https://www.postman.com/downloads/

+ 安装相应三方

  + pom插件

    ```xml
    <!--mysql-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <!--jdbc-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <!--jpa-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    ```

  + 配置

    + mysql连接

    ```yaml
    #数据库连接池
    spring:
      datasource:
        username: root
        password: xxxx
        url: jdbc:mysql://localhost:3306/xxx?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC
        driver-class-name: com.mysql.jdbc.Driver
        #设置数据源
        type: com.alibaba.druid.pool.DruidDataSource
    
        # 连接池的配置信息
        # 初始化大小，最小，最大
        initialSize: 6
        minIdle: 5
        maxActive: 20
        # 配置获取连接等待超时的时间
        maxWait: 60000
        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        timeBetweenEvictionRunsMillis: 60000
        # 配置一个连接在池中最小生存的时间，单位是毫秒
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
    ```

    + 配置jpa

    ```properties
    #---配置jpa
    #设置为更新或创建表
    spring.jpa.hibernate.ddl-auto=update
    #设置显示sql语句
    spring.jpa.show-sql=true
    #配置生成字段的编码
    spring.jpa.database-platform=com.xxx.JpaFormatConfig
    ```

    + 配置jpa字段编码配置类

    ```java
    /**
     * 配置一个jpa生成字段为中文的类
     */
    public class JpaFormatConfig extends MySQL5Dialect {
        @Override
        public String getTableTypeString() {
            return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
        }
    }
    
    ```

    

+ MySql简单使用

  + 表:tb_user

  | id   | int      | 唯一id |
  | ---- | -------- | ------ |
  | user | varchart | 用户名 |
  | pwd  | varchart | 密码   |
  | node | varchart | 备注   |

  + 查询

  ```mysql
  select * from tb_user
  ```

  + 修改

  ```mysql
  update tb_user set node='修改后的备注' where id = 1
  ```

  + 删除

  ```mysql
  delete from tb_user where id = 1
  ```

  + 添加

  ```mysql
  insert into tb_usesr(id,user,pwd,node) values(1,'user2','123456','暂无')
  ```

  

## 4.Jpa+MySql数据库(下)

+ 插件

```xml
<!--        get/set自动配置-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.12</version>
    <scope>provided</scope>
</dependency>
```

> ide配置
>
> + settings->plugins->安装插件lombok(红辣椒)
> + 具体其他操作:https://blog.csdn.net/u013225178/article/details/80721799

+ 准备一个**实体类**,并使用**jpa创建表格**

```java
@Entity         //表明这是个实体类
@Table(name = "tb_user")    //设置该类对于的表格
@Data   //默认给类加上get/set/构造方法...
public class SqlMyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column
    String user;
    @Column
    String pwd;
    @Column
    String node;
}

```

> 其他jpa注释:https://blog.csdn.net/weixin_39942785/article/details/113455345
>
> 注意:使用lombok,注意属性命名问题,驼峰

+ 创建dao层,dao->SqlMyUserDao接口

```java
public interface SqlMyUserDao extends JpaRepository<SqlMyUser, Integer> {
    //默认已经实现常用语句方法
}
```

+ 创建service层,service->MyUserService类操作数据库查删改增

```java
@Service
public class MyUserService {
    @Autowired
    SqlMyUserDao sqlMyUserDao;
    //查询
    public List<SqlMyUser> findMyUserAll(){
        return sqlMyUserDao.findAll();
    }
    //添加/修改
    public  SqlMyUser saveMyUser(SqlMyUser sqlMyUser){
        SqlMyUser save = sqlMyUserDao.save(sqlMyUser);
        return save;
    }
    //删除
    public  void deleteByIdMyUser(Integer id){
        sqlMyUserDao.deleteById(id);
    }
}
```

+ controller->MyUserController接口

```java
@RestController
public class MyUserController {
    @Autowired
    MyUserService myUserService;

    //获取用户所有数据接口
    @GetMapping("/getMyUserAll")
    public List<SqlMyUser> getMyUserAll(){
        return myUserService.findMyUserAll();
    }
    //添加用户:并且传递json字符串
    @PostMapping("/addMyUser")
    public String addMyUser(@RequestBody SqlMyUser sqlMyUser){
        SqlMyUser user = myUserService.saveMyUser(sqlMyUser);
        if(null==user) return "添加失败!";
        return  "添加成功!";
    }
    //删除用户
    @PostMapping("deleteMyUser")
    public String deleteMyUser(Integer id){
        myUserService.deleteByIdMyUser(id);
        return "删除成功!";
    }

}
```

+ Slf4j的使用

  + 配置级别

    ```properties
    #Slf4j
    logging.level.com.tommy.spring = info
    ```

  + 打印

    ```java
    @Slf4j
    public class MyUserController {
        
        //查询所有用户
        @GetMapping("/getSqlMyUserAll")
        public List<SqlMyUser> getSqlMyUserAll(){
            log.info("------访问了查询所有用户-----");
            log.debug("------测试时候:访问了查询所有用户-----");
           return myUserService.findMyUserAll();
        }
    }
    ```

    

