package com.github.freetie.hcspblog.entity;

public class Result {
    public String status;
    public String msg;
    public boolean isLogin;
    public User data;

    Result(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public User getData() {
        return data;
    }

    public static Result success() {
        return new Result("ok");
    }
    public static Result success(String msg) {
        Result successfulResult = new Result("ok");
        successfulResult.msg = msg;
        return successfulResult;
    }
    public static Result failure() {
        return new Result("fail");
    }
    public static Result failure(String msg) {
        Result failureResult = new Result("fail");
        failureResult.msg = msg;
        return failureResult;
    }
}
