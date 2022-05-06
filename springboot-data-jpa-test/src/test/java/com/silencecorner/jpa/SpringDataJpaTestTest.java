package com.silencecorner.jpa;

import com.silencecorner.jpa.dto.UserDto;
import com.silencecorner.jpa.model.User;
import com.silencecorner.jpa.model.Wallet;
import com.silencecorner.jpa.repos.UserRepository;
import com.silencecorner.jpa.repos.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author hai
 * @description junit测试
 * @email hilin2333@gmail.com
 * @date 30/12/2017 5:50 PM
 */
@SpringBootTest
public class SpringDataJpaTestTest {
    @Resource
    private UserRepository userDao;
    @Resource
    private WalletRepository walletDao;
    @Test
    public void deleteAll(){
        userDao.deleteAll();
        walletDao.deleteAll();
    }
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
        userDao.saveAll(users);
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
            user.setUid(userDtoList.get(0).getUid());
            user.setUsername("大海");
            user.setEmail("hilin2333@gmail.com");
            user.setMobile("13212345680");
            user.setStatus(1);
            User newUser = userDao.save(user);
            System.out.println(newUser.toString());
        }
    }

    @Test
    public void find(){
       User user = userDao.findUserByMobile("13212345678");
       System.out.print(user.toString());
    }
    @Test
    public void oneToOneCascadeTest(){
        User user = new User();
        user.setUsername("海哥4");
        user.setEmail("hilin2333@gmail.com");
        user.setMobile("15281718794");
        user.setStatus(1);
        user.setWallet(Wallet.builder().balance(1111111).build());
        userDao.save(user);
        User selectUser = userDao.findUserByMobile("15281718794");
        System.out.println("用户余额：" + selectUser.getWallet().getBalance());
    }
    @Test
    public void oneToOneCascadeSelectTest(){
        User selectUser = userDao.findUserByMobile("15281718794");
        System.out.println("用户名称1：" + selectUser.getWallet().getUser().getUsername());
        String uuid = selectUser.getWallet().getUid();
        Optional<Wallet> wallet = walletDao.findById(uuid);
        wallet.ifPresent(w -> {
            System.out.println("用户名称1：" + w.getUser().getUsername());
        });


    }
}
