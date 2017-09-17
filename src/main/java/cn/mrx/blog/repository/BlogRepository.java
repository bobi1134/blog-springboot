package cn.mrx.blog.repository;

import cn.mrx.blog.model.Blog;
import cn.mrx.blog.model.Catalog;
import cn.mrx.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: xialiangbo
 * Date: 2017/8/28 8:55
 * Description:
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * 最新查询(按照标题or关键字查询)
     * @param title
     * @param user
     * @param tags
     * @param user2
     * @param pageable
     * @return
     */
//    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user, String tags, User user2, Pageable pageable);

    /**
     * 最热查询
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
