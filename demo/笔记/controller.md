HTTP流程

用户点击->javascript捕获->发送http->处理响应

客户端发起请求  
GET http://localhost:8080/search?keyword=java&page=1
    GET	HTTP 方法
    /search	请求路径（对应 @GetMapping("/search")）
    ?keyword=java & page=1	查询参数的关键字k是java（Query String）


Spring 匹配到方法
Spring 看到 @GetMapping("/search")，知道这个方法要处理 /search 路径的 GET 请求


@RestController	标记为 REST 控制器（返回 JSON/XML，不是页面）
@RestController
public class ApiController {// 返回 {"message": "Hello"}
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello");
    }
}

@Controller	传统 MVC 控制器（返回视图页面）
@Controller  // 返回 hello.html 页面
public class PageController {
    @GetMapping("/page")
    public String page() {
        return "hello";  // 解析为 hello.html
    }
}


@RequestMapping	通用映射，可定义基础路径，支持所有 HTTP 方法（GET/POST/PUT/DELETE 等）。
@GetMapping	专门处理 GET 请求（查询数据）。
@PostMapping	专门处理 POST 请求（创建数据）。
@PutMapping	专门处理 PUT 请求（更新数据）。
@DeleteMapping	专门处理 DELETE 请求（删除数据）。

@RestController
@RequestMapping("/api/users")  // 基础路径：/api/users
public class UserController {

    @GetMapping  // GET /api/users
    public List<User> list() { ... }
    
    @GetMapping("/{id}")  // GET /api/users/123
    public User get(@PathVariable Long id) { ... }
    
    @PostMapping  // POST /api/users
    public User create(@RequestBody User user) { ... }
    
    @PutMapping("/{id}")  // PUT /api/users/123
    public User update(@PathVariable Long id, @RequestBody User user) { ... }
    
    @DeleteMapping("/{id}")  // DELETE /api/users/123
    public void delete(@PathVariable Long id) { ... }
}


@PathVariable	从 URL 路径 中获取参数	/users/123 → 获取 123
@RequestParam	从 URL 查询参数 中获取	/users?name=Tom → 获取 Tom
@RequestBody	从 请求体 中获取 JSON 数据	POST 请求的 JSON 体

@RestController
public class DemoController {

    // GET /user/123  →  id = 123
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id) {
        return "User ID: " + id;
    }
    
    // GET /search?keyword=java&page=1  →  keyword="java", page=1
    @GetMapping("/search")
    public String search(
            @RequestParam String keyword,   //获得keyword参数
            @RequestParam(defaultValue = "1") int page) //获得page参数
    {
        return "搜索: " + keyword + ", 页码: " + page;
    }
    
    // POST /user  (请求体: {"name":"Tom","age":20})
    @PostMapping("/user")
    public String createUser(@RequestBody User user) {
        return "创建用户: " + user.getName();
    }
}


@RestController / @Controller  (标记控制器类)
↓
@RequestMapping("/api")  (定义基础路径)
↓
@GetMapping / @PostMapping / @PutMapping / @DeleteMapping  (定义具体接口)
↓
@PathVariable / @RequestParam / @RequestBody  (获取请求参数)




















