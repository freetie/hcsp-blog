package com.github.freetie.hcspblog.controller;

import com.github.freetie.hcspblog.entity.Result;
import com.github.freetie.hcspblog.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return Result.failure("用户名或密码不能为空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return Result.failure("用户名长度必须为1~15");
        }
        if (password.length() < 6 || password.length() > 16) {
            return Result.failure("密码长度必须为6~16");
        }
        try {
            userService.createUser(username, password);
            return Result.success("注册成功");
        } catch (DuplicateKeyException e) {
            return Result.failure("用户名已存在");
        }
    }

    @GetMapping("/auth")
    public Result auth() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.contains("anonymous")) {
            Result failureResult = Result.failure();
            failureResult.isLogin = false;
            return failureResult;
        }
        Result result = Result.success();
        result.isLogin = true;
        result.data = userService.getUserByUsername(username);
        return result;
    }

    @PostMapping("/auth/login")
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            Result result = Result.success("登陆成功");
            result.data = userService.getUserByUsername(username);
            return result;
        } catch (UsernameNotFoundException e) {
            return Result.failure("用户不存在");
        } catch (BadCredentialsException e) {
            return Result.failure("密码不正确");
        }
    }

    @GetMapping("/auth/logout")
    public Result logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.contains("anonymous")) {
            return Result.failure("用户尚未登录");
        }
        SecurityContextHolder.clearContext();
        return Result.success("注销成功");
    }
}
