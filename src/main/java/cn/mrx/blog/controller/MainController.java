package cn.mrx.blog.controller;

import cn.mrx.blog.model.Authority;
import cn.mrx.blog.model.User;
import cn.mrx.blog.service.AuthorityService;
import cn.mrx.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: xialiangbo
 * Date: 2017/8/20 22:57
 * Description:
 */
@Controller
public class MainController {

    /** 普通用户 */
    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root(){
        return "redirect:index";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
//        return "redirect:/blogs";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/login-error")
    public String login(Model model){
        model.addAttribute("loginError", true);
        model.addAttribute("loginErrorMsg", "登录失败，用户名或密码错误！");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);
        user.setEncodePassword(user.getPassword());
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/403")
    public String forbidden(){
        return "403";
    }
}
