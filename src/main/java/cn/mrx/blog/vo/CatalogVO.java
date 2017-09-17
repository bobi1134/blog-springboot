package cn.mrx.blog.vo;

import cn.mrx.blog.model.Catalog;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 1:29
 * Description:
 */
public class CatalogVO {

    private String username;
    private Catalog catalog;

    /** getter and setter method */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
