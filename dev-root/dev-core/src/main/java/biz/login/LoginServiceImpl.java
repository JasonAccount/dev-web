package biz.login;

import cache.RedisHelp;
import common.SystemCode;
import core.common.ServiceResult;
import core.service.login.LoginService;
import core.service.login.LoginType;
import core.vo.AccountVo;
import db.entity.User;
import db.mapper.UserMapper;
import db.model.OnLineUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisHelp redisHelp;

    @Override
    public ServiceResult<Void> login(LoginType loginType, AccountVo accountVo) {
        switch (loginType){
            case PWD:
                OnLineUser onLineUser = userMapper.findOnlineUserInfo(accountVo.getAccount(), accountVo.getPwd());
                redisHelp.set("onlineUser", onLineUser);
                return ServiceResult.success();
            case WECHAT:

                return ServiceResult.success();
            case PHONE:

                return ServiceResult.success();
            default:
                return ServiceResult.fail(SystemCode.LoginTypeError);
        }
    }

    @Override
    public ServiceResult<Void> checkLogin(LoginType loginType, AccountVo accountVo) {
        switch (loginType){
            case PWD:
                User user = userMapper.findUser(accountVo.getAccount(), accountVo.getPwd());
                return user == null ? ServiceResult.fail(SystemCode.PasswordNotMatch) : ServiceResult.success();
            case WECHAT:

                return ServiceResult.success();
            case PHONE:

                return ServiceResult.success();
            default:
                return ServiceResult.fail(SystemCode.LoginTypeError);
        }
    }
}
