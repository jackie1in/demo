package com.silencecorner.jpa;

import com.silencecorner.jpa.dto.UserDto;
import com.silencecorner.jpa.model.User;
import com.silencecorner.jpa.repos.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hai
 * @description junit测试
 * @email hilin2333@gmail.com
 * @date 30/12/2017 5:50 PM
 */
@SpringBootTest
@RunWith(value = SpringRunner.class)
public class SpringDataJpaTestTest {
    @Resource
    private UserRepository userDao;
    @Test
    public void save(){
        User user = new User();
        user.setUsername("海哥");
        user.setEmail("hilin2333@gmail.com");
        user.setMobile("13212345678");
        user.setStatus(1);
        userDao.save(user);
    }
    @Test
    public void saveBatch(){
        User user = new User();
        user.setUsername("海哥");
        user.setEmail("hilin2333@gmail.com");
        user.setMobile("13212345678");
        user.setStatus(1);
        User user1 = new User();
        user.setUsername("海哥2");
        user1.setEmail("hilin2333@gmail.com");
        user1.setMobile("13212345679");
        user1.setStatus(1);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        userDao.deleteAll();
        userDao.save(users);
    }
    @Test
    public void dynamicProjectionTest(){
        //because of UserDto properties don't have setter,cannot be used for spring BeanUtils.copyProperties()  target argument
        List<UserDto> userDtoList = userDao.findAllByStatus(1, UserDto.class);
        for (UserDto dto : userDtoList){
            System.out.println(dto.toString());
        }
        if (!CollectionUtils.isEmpty(userDtoList)){
            User user = new User();
            BeanUtils.copyProperties(userDtoList.get(1),user);
            user.setUsername("大海");
            user.setEmail("hilin2333@gmail.com");
            user.setMobile("13212345679");
            user.setStatus(1);
            User newUser = (User) userDao.save(user);
            System.out.println(newUser.toString());
        }
    }

    @Test
    public void find(){
       User user = userDao.findUserByMobile("13212345678");
       System.out.print(user.toString());
    }
}
