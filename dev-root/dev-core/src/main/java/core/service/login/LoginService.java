package core.service.login;

import core.common.ServiceResult;
import core.vo.AccountVo;

public interface LoginService {

    /**
     * 实现登录逻辑
     * @param loginType
     * @param accountVo
     * @return
     */
    ServiceResult<Void> login(LoginType loginType, AccountVo accountVo);

    /**
     * 实现登录前验证逻辑
     * @param loginType
     * @param accountVo
     * @return
     */
    ServiceResult<Void> checkLogin(LoginType loginType, AccountVo accountVo);
}
