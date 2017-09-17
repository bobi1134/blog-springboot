package cn.mrx.blog.controller;

import cn.mrx.blog.model.Blog;
import cn.mrx.blog.model.Catalog;
import cn.mrx.blog.model.User;
import cn.mrx.blog.model.Vote;
import cn.mrx.blog.service.BlogService;
import cn.mrx.blog.service.CatalogService;
import cn.mrx.blog.service.UserService;
import cn.mrx.blog.utils.ConstraintViolationExceptionHandler;
import cn.mrx.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Author: xialiangbo
 * Date: 2017/8/25 11:25
 * Description:
 */
@Controller
@RequestMapping("/u")
//@PreAuthorize("hasAnyAuthority(new String[]{'ROLE_USER', 'ROLE_ADMIN'})")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @Autowired
    private CatalogService catalogService;

    /**
     * 个人设置页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profilePage(@PathVariable("username") String username, Model model) {
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/profile", "userModel", model);
    }

    /**
     * 保存个人设置
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());

        // 判断密码是否做了变更
        String rawPassword = originalUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveUser(originalUser);
        return "redirect:/u/" + username + "/profile";
    }

    /**
     * 用户头像页面，模态窗口
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatarPage(@PathVariable("username") String username, Model model) {
        User  user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", model);
    }

    /**
     * 保存头像
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
        String avatarUrl = user.getAvatar();
        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveUser(originalUser);
        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }

    /**个人主页
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username,
                            Model model) {
        User  user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }*/

    /**
     * 个人主页
     * @param username
     * @param order
     * @param catalogId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String userSpacePage(@PathVariable("username") String username,
                                @RequestParam(value="order",required=false,defaultValue="new") String order,
                                @RequestParam(value="catalogId",required=false ) Long catalogId,
                                @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
                                @RequestParam(value="async",required=false) boolean async,
                                @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                @RequestParam(value="pageSize",required=false,defaultValue="5") int pageSize,
                                Model model) {

        User  user = (User)userDetailsService.loadUserByUsername(username);

        /** 分页查询 */
        Page<Blog> page = null;
        if (catalogId != null && catalogId > 0) {
            System.out.println("按分类查询...");
            Catalog catalog = catalogService.getCatalogById(catalogId);
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByCatalog(catalog, pageable);
            order = "";
        }else if(order.equals("hot")){
            System.out.println("最热查询...");
            Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
        }else if(order.equals("new")){
            System.out.println("最新查询...");
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleVote(user, keyword, pageable);
        }
        List<Blog> list = page.getContent();

        /** 判断操作用户是否是博客的所有者(根据这个是否显示新增分类按钮)  */
        boolean isCatalogsOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() !=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && username.equals(principal.getUsername())) {
                isCatalogsOwner = true;
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("isCatalogsOwner", isCatalogsOwner);
        model.addAttribute("keyword", keyword);
        model.addAttribute("blogList", list);
        return (async==true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u");
    }

    /**
     * 根据id获取博客文章内容
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        /** 每次读取，简单的可以认为阅读量增加1次 */
        blogService.readingIncrease(id);

        /** 判断操作用户是否是博客的所有者(根据这个是否显示删除、编辑按钮) */
        User principal = null;
        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() !=null
            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
            &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
        }

        /** 判断操作用户的点赞情况 */
        System.out.println("principal-->"+principal);
        Blog blog = blogService.getBlogById(id);
        List<Vote> votes = blog.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况
        if (principal != null) {
            for (Vote vote : votes) {
                if(vote.getUser().getUsername().equals(principal.getUsername())){
                    currentVote = vote;
                    break;
                };
            }
        }

        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel", blogService.getBlogById(id));
        model.addAttribute("currentVote", currentVote);
        return "/userspace/blog";
    }

    /**
     * 新增博客界面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/add")
    public ModelAndView addBlogPage(@PathVariable("username") String username, Model model) {
        /**model.addAttribute("fileServerUrl", fileServerUrl);*/
        // 分类信息
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("catalogs", catalogs);
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("title", "添加博客");
        return new ModelAndView("/userspace/blogform", "blogModel", model);
    }

    /**
     * 编辑博客界面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlogPage(@PathVariable("username") String username,  @PathVariable("id") Long id, Model model) {
        /**model.addAttribute("fileServerUrl", fileServerUrl);*/

        // 分类信息
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("catalogs", catalogs);
        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("title", "修改博客");
        return new ModelAndView("/userspace/blogform", "blogModel", model);
    }

    /**
     * 保存/编辑博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/saveOrUpdate")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveOrUpdateBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        try {
            if(blog.getId() != null){
                /** 修改 */
                Blog orignalBlog = blogService.getBlogById(blog.getId());
                orignalBlog.setTitle(blog.getTitle());      //标题
                orignalBlog.setSummary(blog.getSummary());  //摘要
                orignalBlog.setContent(blog.getContent());  //内容
                orignalBlog.setCatalog(blog.getCatalog());  //分类
                orignalBlog.setTags(blog.getTags());        //标签
                blogService.saveBlog(orignalBlog);
            }else{
                /** 添加 */
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blog.setReadSize(0);    //访问量
                blog.setCommentSize(0); //评论量
                blog.setVoteSize(0);    //点赞量
                blogService.saveBlog(blog);
            }
        } catch (ConstraintViolationException e)  {
            // bean验证
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        // 跳转到文章内容页面
        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/blogs/del/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id) {
        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        // 删除成功后跳转到个人主页
        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }
}
