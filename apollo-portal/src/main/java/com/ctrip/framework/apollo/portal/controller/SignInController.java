package com.ctrip.framework.apollo.portal.controller;

import com.ctrip.framework.apollo.portal.entity.po.UserPO;
import com.ctrip.framework.apollo.portal.repository.UserRepository;
import com.ctrip.framework.apollo.portal.spi.letv.sso.LoginUtil;
import com.ctrip.framework.apollo.portal.spi.letv.sso.Md5Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lepdou 2017-08-30
 */
@Controller
public class SignInController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 下发Cookie
     *
     * @param username
     * @param response
     */
    private static void produceCookie(String username, HttpServletResponse response) {
        String hideInfo = "zbdhrirkdmgngitmagic" + username;
        Cookie cookie = new Cookie("tk", hideInfo);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 10 * 30); // 30分钟
        response.addCookie(cookie);
    }

    @RequestMapping(value = "/signin", method = {RequestMethod.POST, RequestMethod.GET})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {
        return "login.html";
    }

    /**
     * 登录验证，下发tk
     *
     * @return
     */
    @RequestMapping(value = "/logon", method = {RequestMethod.POST, RequestMethod.GET})
    public String logon(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password,
                        HttpServletRequest request, HttpServletResponse response) {
        if (LoginUtil.verify(username, password)) {
            UserPO user = userRepository.findByUsername(username);
            if (null == user) {
                saveUser(username, password);
            } else {
                if (!Md5Encrypt.md5(password).equals(user.getPassword())) {
                    user.setPassword(Md5Encrypt.md5(password));
                    deleteUser(user.getId());
                    saveUser(username, password);
                }
            }
            //下发cookie，并跳转
            produceCookie(username, response);
            return "redirect:/";
        } else {
            return "redirect:/signin";
        }
    }

    private void saveUser(String username, String password) {
        UserPO userPO = new UserPO();
        userPO.setUsername(username);
        userPO.setPassword(Md5Encrypt.md5(password));
        userPO.setEnabled(1);
        userPO.setEmail(username + "@le.com");
        userRepository.save(userPO);
    }

    private void deleteUser(long userId){
        userRepository.deleteById(userId);
    }


}
