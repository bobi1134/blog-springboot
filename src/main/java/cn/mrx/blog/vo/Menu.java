package cn.mrx.blog.vo;

/**
 * Author: xialiangbo
 * Date: 2017/8/22 8:52
 * Description:
 */
public class Menu {

    private String name;
    private String url;

    /** 构造器 */
    public Menu() {
    }

    public Menu(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /** getter and getter method */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
