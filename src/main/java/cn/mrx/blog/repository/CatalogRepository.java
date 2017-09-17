package cn.mrx.blog.repository;

import cn.mrx.blog.model.Catalog;
import cn.mrx.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 1:31
 * Description:
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    /**
     * 根据用户查询
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户查询
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user,String name);
}
