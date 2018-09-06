package com.ctrip.framework.apollo.portal.spi.letv.sso;

import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用乐盟用户中心接口
 *
 * @author evilhex.
 * @date 2018/9/4 下午4:46.
 */
public class LoginUtil {

    /** 乐盟接口域名 */
    public static final String SSO_FAMILY_DOMAIN = "http://ucapi.lecommons.com";
    /** 检查用户登录密码的url */
    public static final String SSO_URL_CHECKUSER = SSO_FAMILY_DOMAIN + "/check.php";
    /** 转换用户密码的url */
    public static final String SSO_URL_TRANSCODE = SSO_FAMILY_DOMAIN + "/transcode.php";

    /** 签名秘钥 */
    public static final String SECRET_KEY = "ahr0ccuzqs8vy29uzmlnlm";

    private static final int SECOND_PER_MILLIS = 1000;

    private static HttpUtil httpUtil = HttpUtil.getIntance();

    /**
     * 验证用户名称密码是否正确
     *
     * @param userName 用户名称
     * @param password 密码原文
     * @return
     */
    public static Boolean verify(String userName, String password) {
        long currSeconds = System.currentTimeMillis() / SECOND_PER_MILLIS;

        try {
            String signedPwd = geneSignedPwd(password);
            String origParam = "password=" + signedPwd + "&site=apollo&time=" + currSeconds + "&username=" + userName;
            String key = origParam + SECRET_KEY;
            String sign = Md5Encrypt.md5(key);

            Map<String, String> params = new HashMap<>(10);
            params.put("username", userName);
            params.put("password", signedPwd);
            params.put("site", "apollo");
            params.put("time", String.valueOf(currSeconds));
            params.put("sign", sign);
            JSONObject response = JSONObject.parseObject(httpUtil.post(SSO_URL_CHECKUSER, params));
            JSONObject respond = (JSONObject) response.get("respond");
            if (respond.getIntValue("status") == 1) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 加密密码
     *
     * @param pwd 密码原文
     * @return
     */
    private static String geneSignedPwd(String pwd) throws Exception {
        long currSeconds = (System.currentTimeMillis() / 1000);
        String encodedPwd = URLEncoder.encode(pwd, "UTF-8");
        String originParam = "site=apollo&time=" + currSeconds + "&type=ENCODE&v=" + encodedPwd;
        String key = originParam + SECRET_KEY;
        String sign = Md5Encrypt.md5(key);
        StringBuilder sbUrl = new StringBuilder(SSO_URL_TRANSCODE);
        sbUrl.append("?" + originParam);
        sbUrl.append("&sign=" + sign);
        JSONObject response = JSONObject.parseObject(httpUtil.get(sbUrl.toString()));
        return response.get("objects").toString();
    }

    /**
     * 通过tk获取当前用户
     *
     * @param tk
     * @return
     * @throws Exception
     */
    private static String getUserFromTk(String tk) throws Exception {
        long currSeconds = (System.currentTimeMillis() / 1000);
        String originParam = "site=apollo&time=" + currSeconds + "&type=DECODE&v=" + tk;
        String key = originParam + SECRET_KEY;
        String sign = Md5Encrypt.md5(key);
        StringBuilder sbUrl = new StringBuilder(SSO_URL_TRANSCODE);
        sbUrl.append("?" + originParam);
        sbUrl.append("&sign=" + sign);
        JSONObject response = JSONObject.parseObject(httpUtil.get(sbUrl.toString()));
        return null;
    }

}
