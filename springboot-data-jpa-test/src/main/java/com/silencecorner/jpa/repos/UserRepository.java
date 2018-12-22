package com.silencecorner.jpa.repos;

import com.silencecorner.jpa.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hai
 * @description 用户dao
 * @email hilin2333@gmail.com
 * @date 30/12/2017 5:47 PM
 */
@Repository
public interface UserRepository extends JpaRepository<User,String>{
    User findUserByMobile(String mobile);
    <T> List<T> findAllByStatus(Integer status,Class<T> tClass);
}
