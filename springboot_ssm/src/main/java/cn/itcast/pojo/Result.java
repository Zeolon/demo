package cn.itcast.pojo;

import java.io.Serializable;

public class Result implements Serializable {


    private boolean success;   //状态判断
    private String message;    //返回的消息内容


    public Result(boolean success,String message) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
