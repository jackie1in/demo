package com.silencecorner.jpa.repos;

import com.silencecorner.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author hai
 * @description 用户dao
 * @email hilin2333@gmail.com
 * @date 30/12/2017 5:47 PM
 */
@Repository
public interface UserRepository<T,String extends Serializable> extends JpaRepository<User,String>{
    User findUserByMobile(String mobile);
    List<T> findAllByStatus(Integer status, Class<T> type);
}
