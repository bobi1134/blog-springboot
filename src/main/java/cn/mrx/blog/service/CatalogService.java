package cn.mrx.blog.service;

import cn.mrx.blog.model.Catalog;
import cn.mrx.blog.model.User;

import java.util.List;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 1:32
 * Description:
 */
public interface CatalogService {

    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     * @param id
     * @return
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取Catalog
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取Catalog列表
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
