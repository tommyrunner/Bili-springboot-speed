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


## 5.spring技巧

+ **Transactional:事务回滚**

  ```java
  //删除
  @Transactional
  public  void deleteByIdMyUser(Integer id){
      sqlMyUserDao.deleteById(id);
      int i = 1/0;
  }
  ```

+ **filter过滤器**

  文件目录:config->MyFilter

  ```java
  @Slf4j
  @Component
  public class MyFilter implements Filter {
  
  
      @Override
      public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
              throws IOException, ServletException {
          HttpServletRequest request = (HttpServletRequest) servletRequest;       //接收请求参数
          HttpServletResponse response = (HttpServletResponse) servletResponse;   //用于请求
          response.setHeader("Access-Control-Allow-Origin", "*");
          // 这个allow-headers要配为*，这样才能允许所有的请求头 --- update by zxy  in 2018-10-19
          response.setHeader("Access-Control-Allow-Headers", "*");
          response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
  
          log.info("----"+request.getRequestURI());
  
          filterChain.doFilter(servletRequest, servletResponse);
      }
  }
  
  ```

  + 跨域解决

    + core:服务器直接修改允许跨域

      ```java
      /**
       * 过滤跨域配置类
       */
      @Configuration
      public class CorsConfig {
          private CorsConfiguration buildConfig(){
              CorsConfiguration corsConfiguration = new CorsConfiguration();
              corsConfiguration.addAllowedOrigin("*");//1允许任何域名使用
              corsConfiguration.addAllowedHeader("*");//2允许任何头
              corsConfiguration.addAllowedMethod("*");//允许任何方法(post、get)
              return  corsConfiguration;
          }
          @Bean
          public CorsFilter corsFilter(){
              UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
              source.registerCorsConfiguration("/**",buildConfig());  //4添加到配置
              return new CorsFilter(source);
          }
      }
      ```

    + jsonp:服务器允许jsonp调用

    + 代理:自己搭一个服务器,调用跨域接口,并返回给自己

  

+ 使用@interface自定义注解,实现防止重复点击

  + 准备一个注解

    文件目录:annotation->ClickLock
  
  ```java
  
  @Target({ElementType.METHOD})
  /**
   * @Target(ElementType.TYPE)   //接口、类、枚举、注解
   * @Target(ElementType.FIELD) //字段、枚举的常量
   * @Target(ElementType.METHOD) //方法
   * @Target(ElementType.PARAMETER) //方法参数
   * @Target(ElementType.CONSTRUCTOR)  //构造函数
   * @Target(ElementType.LOCAL_VARIABLE)//局部变量
   * @Target(ElementType.ANNOTATION_TYPE)//注解
   * @Target(ElementType.PACKAGE) ///包
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Deprecated
  @Inherited
  public @interface ClickLock {
      /**
       * 定义属性
       */
      String key() default "";
  }
  ```
  
+ 实现注解的逻辑，并实现**不能重复点击的业务**
  
  文件目录:aop->ClickLockTo
  
    ```java
    package com.example.testdemo.aop;
    
    
    @Configuration
    @Aspect
    public class ClickLockTo {
        //准备一个缓存池
        private static final Map<String,Object> CACHES = new HashMap<>();
    
        //接受到所有的注解
        //拦截所有的public方法并且注释为ClickLock
        //注意：你注解了多少个方法就会调用几次
        @Around("execution(public * *(..))&&@annotation(com.example.testdemo.annotation.ClickLock)")
        public Object interceptor(ProceedingJoinPoint pjp){
            //获取注解中的参数和方法里的参数
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            ClickLock clickLock = method.getAnnotation(ClickLock.class);
            //获取的参数集合pjp.getArgs():所有注解的方法里的参数
            //clickLock.key()：获取注解上的属性值
            String key = clickLock.key();
            if (!StringUtils.isEmpty(key)){ //不能为空
                if (CACHES.get(key)!=null){
                    //找到了，里面存入过（刚刚访问过）
                    System.out.println("----------重新插入");
                    throw new RuntimeException("不能重复点击");
    
                }else {
                    //没存过
                    CACHES.put(key,key);
                    System.out.println("----------插入");
                    new Thread(){
                        @SneakyThrows
                        @Override
                        public void run() {
                            sleep(1000);
                            CACHES.remove(key);
                        }
                    }.start();
                }
            }
            try {
                return pjp.proceed();  //继续执行
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new RuntimeException("错误");
            }
        }
    }
    ```
  
    > 注意：
    >
    > + 流程
    >   + 准备一个map代替缓存池CACHES
    >   + 获取所有的注解参数并存入缓存进行判定interceptor
    >     + Around：扫描-拦截所有的public方法并且注释为ClickLock
    >   + 插入后,记得设置时间删除
  
  + 使用这个注解
  
    ```java
    @ClickLock(key = "MyUserService/findMyUserAll")
    public List<SqlMyUser> findMyUserAll(){
        return sqlMyUserDao.findAll();
    }
    ```
  
+ **ExceptionHandler全局错误管理**

  + 准备一个管理类

    文件目录:config->ErrorManage
  
    ```java
    @RestControllerAdvice   //RestControllerAdvice controller 增强器
    public class ErrorManage {
        //监听只要spring有Exception错误就调用这个方法
        @ExceptionHandler(RuntimeException.class)
        public Map<String, String> SysException(RuntimeException e) {
            Map<String,String> map = new HashMap<>();
            map.put("error","服务器错误:"+e.toString());
            return map;
        }
  }
    ```

  + 抛出错误
  
    ```java
    throw new RuntimeException("不能重复点击");
    ```
  
    > + 注意拦截范围-一般处理用户访问发生的,service里

## 6.swagger2自动生成接口文档器

+ 引入

  ```xml
  <!--引入swagger2-->
  <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger2</artifactId>
      <version>2.9.2</version>
  </dependency>
  <!--引入swagger2的ui-->
  <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger-ui</artifactId>
      <version>2.9.2</version>
  </dependency>
  ```

+ 配置swagger2

  + config文件里-->SwaggerConfig

  ```java
  @Configuration
  @EnableSwagger2
  public class SwaggerConfig {
      @Bean
      public Docket createRestApi() {
          return new Docket(DocumentationType.SWAGGER_2)
                  .apiInfo(apiInfo())
                  .select()
                  .apis(RequestHandlerSelectors.basePackage("com.xxx.xxxx"))    //api接口包扫描路径
                  .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                  .build();
      }
  
      private ApiInfo apiInfo() {
          return new ApiInfoBuilder()
                  .title("测试Swgger生产文档") //设置文档的标题
                  .description("文档描述") // 设置文档的描述
                  .version("1.0.0") // 设置文档的版本信息-> 1.0.0 Version information
                  .termsOfServiceUrl("http://www.baidu.com") // 设置文档的License信息->1.3 License information
                  .build();
      }
  }
  ```

  > 注意apis是扫描自己的包文件，得填自己的

+ 配置spring的接口

  ```java
  @RestController
  @Api(value = "用户接口")
  public class MyUserController {
      @Autowired
      MyUserService myUserService;
  
      //获取用户所有数据接口
      @GetMapping("/getMyUserAll")
      @ApiOperation(value = "查询所有用户")
      public List<SqlMyUser> getMyUserAll(){
          return myUserService.findMyUserAll();
      }
      //添加用户:并且传递json字符串
      @PostMapping("/addMyUser")
      @ApiOperation(value = "添加用户")
      public String addMyUser(@ApiParam(value = "用户对象") @RequestBody SqlMyUser sqlMyUser){
          SqlMyUser user = myUserService.saveMyUser(sqlMyUser);
          if(null==user) return "添加失败!";
          return  "添加成功!";
      }
      //删除用户
      @PostMapping("deleteMyUser")
      @ApiOperation(value = "删除用户")
      public String deleteMyUser(@ApiParam(value = "用户唯一id")Integer id){
          myUserService.deleteByIdMyUser(id);
          return "删除成功!";
      }
  
  }
  ```

  > 注意：
  >
  > + @Api：修饰整个类，描述Controller的作用
  >
  > + @ApiOperation：描述一个类的一个方法，或者说一个接口
  >
  > + @ApiParam：单个参数描述
  >
  > + @ApiModelProperty：用对象来接收参数(springboot2.x使用ApiModelProperty)
  >
  > + @ApiProperty：用对象接收参数时，描述对象的一个字段
  >
  > + @ApiResponse：HTTP响应其中1个描述
  >
  > + @ApiResponses：HTTP响应整体描述
  >
  > + @ApiIgnore：使用该注解忽略这个API
  >
  > + @ApiError ：发生错误返回的信息
  >
  > + @ApiImplicitParam：描述一个请求参数，可以配置参数的中文含义，还可以给参数设置默认值
  >
  > + @ApiImplicitParams：描述由多个 @ApiImplicitParam 注解的参数组成的请求参数列表


## 7.保存文件-阿里OSS

+ 注册阿里:https://oss.console.aliyun.com/

+ 创建OSS服务

  + 菜单栏->对象储存OSS->创建Bucket文件目录->权限修改为"公开"
  + 进入Bucket目录的菜单栏的"概述"可以获取endpoint外网/内网
    + 外网需要收费-级少
    + 内网不收费

  + 头像->AccessKey管理->获取AccessKey ID和Secret
    + 创建临时:	xxx

+ 引入pom

```xml
<!-- 阿里云oss依赖 -->
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>2.8.3</version>
</dependency>
<!-- 时间插件-用于生成文件名-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.9</version>
</dependency>
```

+ 自定义配置properties文件

```properties
#阿里Oss对象储存
#访问网络来源
oss.endpoint=oss-cn-beijing.aliyuncs.com
#访问accessKey
oss.accessKeyId=xxx
#访问Secret
oss.accessKeySecret=xxx
#访问的bucket目录
oss.bucketName=tmm-tommy
```

> 注意：
>
> + 可能会报警告，这个因为是自定义的，使用才报黄
> + 处理keyId和Secret不一样
> + 链接使用外网

+ 准备oss上传工具类

```java
@Component
@Slf4j
public class OssManagerUtil {
    //------------------变量----------

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;


    private static String FILE_URL;

    /**
     * @param file     上传的文件
     * @param upPath   上传的路径
     * @param fileName 创建的文件名(包括后缀)
     * @return
     */
    public String upLoad(File file, String upPath, String fileName) {

        // 默认值为：true
        boolean isImage = true;
        // 判断所要上传的图片是否是图片，图片可以预览，其他文件不提供通过URL预览
        try {
            Image image = ImageIO.read(file);
            isImage = image == null ? false : true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.debug("------OSS文件上传开始--------" + file.getName());

        // 文件名格式
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
//        String dateString = sdf.format(new Date()) + "." + suffix; // 20180322010634.jpg

        // 判断文件
        if (file == null) {
            return null;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
//            String fileUrl = fileHost + "/" + (dateString + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.getName());
            String fileUrl = upPath + "/" + fileName;
            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                //上传成功后拼接地址
                if (isImage) {//如果是图片，则图片的URL为：....
                    FILE_URL = "https://" + bucketName + "." + endpoint + "/" + fileUrl;
                } else {
                    FILE_URL = fileUrl;
                    log.debug("非图片,不可预览。文件路径为：" + fileUrl);
                }
                log.debug("------OSS文件上传成功------" + fileUrl);
            }
        } catch (OSSException oe) {
            log.debug(oe.getMessage());
        } catch (ClientException ce) {
            log.debug(ce.getErrorMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return FILE_URL;
    }

    /**
     * 通过文件名下载文件
     *
     * @param objectName    要下载的文件名
     * @param localFileName 本地要创建的文件名
     */
    public void downloadFile(String objectName, String localFileName) {

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localFileName));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除文件
     * objectName key 地址
     *
     * @param filePath
     */
    public Boolean delFile(String filePath) {
        log.debug("删除开始，objectName=" + filePath);
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 删除Object.
        boolean exist = ossClient.doesObjectExist(bucketName, filePath);
        if (!exist) {
            log.debug("文件不存在,filePath={}", filePath);
            return false;
        }
        log.debug("删除文件,filePath={}", filePath);
        ossClient.deleteObject(bucketName, filePath);
        ossClient.shutdown();
        return true;
    }

    /**
     * 批量删除
     *
     * @param keys
     */
    public Boolean delFileList(List<String> keys) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 删除文件。
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            ossClient.shutdown();
        }
        return true;

    }

    /**
     * 获取文件夹
     *
     * @param fileName
     * @return
     */
    public List<String> fileFolder(String fileName) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        // 设置正斜线（/）为文件夹的分隔符。
        listObjectsRequest.setDelimiter("/");   //文件文件夹
        // 设置prefix参数来获取fun目录下的所有文件。
        if (StringUtils.isNotBlank(fileName)) {
            listObjectsRequest.setPrefix(fileName + "/");
        }
        // 列出文件
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有commonPrefix
        List<String> list = new ArrayList<>();
        for (String commonPrefix : listing.getCommonPrefixes()) {
            String newCommonPrefix = commonPrefix.substring(0, commonPrefix.length() - 1);
            String[] s = newCommonPrefix.split("/");
            list.add(s[1]);
        }
        // 关闭OSSClient
        ossClient.shutdown();
        return list;
    }

    /**
     * 列举文件下所有的文件url信息
     */
    public List<String> listFile(String fileHost) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

        // 设置prefix参数来获取fun目录下的所有文件。
        listObjectsRequest.setPrefix(fileHost + "/");
        listObjectsRequest.setDelimiter("/");  //如果这个值为空,则获取文件夹里所有文件,包括子文件
        // 列出文件。
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有文件。
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listing.getObjectSummaries().size(); i++) {
            if (i == 0) {
                continue;
            }
            FILE_URL = "https://" + bucketName + "." + endpoint + "/" + listing.getObjectSummaries().get(i).getKey();
            list.add(FILE_URL);
        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return list;
    }

    /**
     * 获得url链接
     *
     * @param objectName
     * @return
     */
    public String getUrl(String objectName) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 设置权限(公开读)
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 设置图片处理样式。
//        String style = "image/resize,m_fixed,w_100,h_100/rotate,90";
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 100);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
        req.setExpiration(expiration);
//        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);
        // 关闭OSSClient。
        log.debug("------OSS文件文件信息--------" + signedUrl.toString());
        ossClient.shutdown();
        if (signedUrl != null) {
            return signedUrl.toString();
        }
        return null;
    }

    // 获取文 MultipartFile 文件后缀名工具
    public static String getSuffix(MultipartFile fileupload) {
        String originalFilename = fileupload.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        System.out.println(suffix);
        return suffix;
    }

    /**
     * 创建文件夹
     *
     * @param folder
     * @return
     */
    public String createFolder(String folder) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 文件夹名
        final String keySuffixWithSlash = folder;
        // 判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            // 创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            log.debug("创建文件夹成功");
            // 得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            ossClient.shutdown();
            return fileDir;
        }
        return keySuffixWithSlash;
    }


}
```

> 注意：
>
> + 头部的加上注解Component
> + 前方属性获取完美配置文件中的值

+ 准备一个文件工具类

```java
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {
    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }
    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

+ 根据类的使用

```java
//注入工具类
@Autowired
OssManagerUtil ossManagerUtil;
public void test(){
    ossManagerUtil.upLoad(new File(), "test", "a.png");//上传文件
    ossManagerUtil.delFile("tommy/img/a.png");//删除文件
    List lists =  ossManagerUtil.listFile('tommy/img');//获取所有文件,并转为url
    System.out.print(lists.toString())
}
```

> https://+**bucket**+.**来源**/test/+图片

## 8.整合Redis缓存

+ 安装

  + key-value,非关系,数据库
  + [服务器官网][http://www.redis.cn/]
  + [客户端][https://github.com/uglide/RedisDesktopManager/releases/tag/0.9.3]

+ 引入

  ```xml
  <!--        redis缓存-->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
      <version>2.3.0.RELEASE</version>
  </dependency>
  ```

+ 配置(一般使用默认)

```properties
# Redis数据库索引（默认为0）
spring.redis.database=0
# Redis服务器地址
spring.redis.host=127.0.0.1
# Redis服务器连接端口
spring.redis.port=6379
# Redis服务器连接密码（默认为空）
spring.redis.password=
# 连接池最大连接数（使用负值表示没有限制）
spring.redis.jedis.pool.max-active=20
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.jedis.pool.max-wait=-1
# 连接池中的最大空闲连接
spring.redis.jedis.pool.max-idle=10
# 连接池中的最小空闲连接
spring.redis.jedis.pool.min-idle=0
# 连接超时时间（毫秒）
spring.redis.timeout=1000
```

+ 设置注解方式

```java
@SpringBootApplication
@EnableCaching//设置注解方式
public class TestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestDemoApplication.class, args);
    }

}
```

+ redis配置

```java
package com.example.testdemo.config;
/**
 * 配置缓存Redis对象为json
 */
@Configuration
public class MyRedisConfig {

    @Bean
    public RedisTemplate<Object, Object> myRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return template;
    }

    @Primary//表示默认的主缓存管理器如果有多个缓存管理器，要指定一个默认的，否则报错
    @Bean
    public RedisCacheManager myCacheManager(RedisConnectionFactory factory){
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Object.class);
        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jsonRedisSerializer.setObjectMapper(om);
        //设置使用json存入缓存
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        RedisCacheConfiguration configuration= RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer));
//                .disableKeyPrefix() 隐藏key前缀 例如 emp::1 => 1
//                .disableCachingNullValues();//不缓存空值
        return RedisCacheManager.builder(factory)
                .cacheDefaults(configuration)
                .build();
    }
    @Bean
    public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory connectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

}

```

+ 设置缓存

```java
@Service
@CacheConfig(cacheNames = "User" ,cacheManager = "myCacheManager")
public class MyUserService {
    @Autowired
    SqlMyUserDao sqlMyUserDao;
    //查询
    @Cacheable(value = "'findMyUserAll'")
    public List<SqlMyUser> findMyUserAll(){
        return sqlMyUserDao.findAll();
    }
    @Cacheable(value = "#id")
    public SqlMyUser findByIdUser(int id){
        return sqlMyUserDao.findById(id).get();
    }

    //删除
    @Transactional
    @CacheEvict(value = "#id")
    public  void deleteByIdMyUser(Integer id){
        sqlMyUserDao.deleteById(id);
    }
}

```

> + @Cacheable:保存
>
> + @CacheEvict:删除/清空
>
> + @CachePut:更新
>
> + key指定
>
>   | 名字         | 位置               | 描述             | 实例             |
>   | ------------ | ------------------ | ---------------- | ---------------- |
>   | methodName   | root object        | 当前调用的方法名 | #root.methodName |
>   | argumentname | evaluation context | 方法参数,#参数   | #id              |
>   | 自定义字符串 |                    |                  | '字符串'         |
>   | ...          | ...                | ...              | ...              |

## 9.scrity+jwt管理权限,negix...