package com.ssh.service;

import com.ssh.bean.User;
import net.sf.json.JSONObject;

public interface UserService {

    User login(String username, String password);

    int updatePassword(String username, String oldPassword, String newPassword);

    JSONObject getActiveUser() throws Exception;

}
