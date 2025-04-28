package io.github.schance.web;

import io.github.schance.Component;

@Controller
@Component
@RequestMapping("/hello")
public class HelloController {

    // localhost:port/hello/a
    @RequestMapping("/a")
    public String hello(@Param("name") String name, @Param("age") Integer age) { // without @Param, the args name will be compiled as arg0 arg1
        return "<h1>hello world</h1><br> name: %s age: %d".formatted(name, age);
    }

    @RequestMapping("/json")
    @ResponseBody
    public User json(@Param("name") String name, @Param("age") Integer age) { // without @Param, the args name will be compiled as arg0 arg1
        User user = new User();
        user.setName(name);
        user.setAge(age);
        return user;
    }

    @RequestMapping("/html")
    public ModelAndView html(@Param("name") String name, @Param("age") Integer age) { // without @Param, the args name will be compiled as arg0 arg1
        ModelAndView mv = new ModelAndView();
        mv.setView("index.html");
        mv.getContext().put("name", name);
        mv.getContext().put("age", age.toString());
        return mv;
    }
}
