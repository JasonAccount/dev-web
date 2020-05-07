package db.mapper;

import db.entity.User;
import db.model.OnLineUser;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    /**
     * 查询 通过账户+密码方式登录的用户信息
     * @param accountName
     * @param password
     * @return
     */
    OnLineUser findOnlineUserInfo(@Param("accountName") String accountName, @Param("password") String password);

    /**
     * 根据用户名+密码获取用户，判断用户是否存在
     * @param accountName
     * @param password
     * @return
     */
    User findUser(@Param("accountName") String accountName, @Param("password") String password);
}
