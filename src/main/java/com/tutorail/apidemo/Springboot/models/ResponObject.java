package com.tutorail.apidemo.Springboot.models;

//trả về một object chứa trạng thái (thành công hay không), nội dung thông báo, data (Product)
public class ResponObject {
    private String status;
    private String message;
    private Object data;

    public ResponObject() {
    }

    public ResponObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
