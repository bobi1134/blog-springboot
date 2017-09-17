package cn.mrx.blog.repository;

import cn.mrx.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Author: xialiangbo
 * Date: 2017/8/18 15:34
 * Description:
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据name进行分页模糊查询
     * @param name
     * @param pageable
     * @return
     */
    Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 根据username查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据名称列表查询
     * @param usernames
     * @return
     */
    List<User> findByUsernameIn(Collection<String> usernames);

}
