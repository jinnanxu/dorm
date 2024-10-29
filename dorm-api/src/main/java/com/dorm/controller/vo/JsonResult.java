package com.dorm.controller.vo;

public class JsonResult {

    private Object data;
    private String code;
    private String msg;
    private boolean success = true;

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     */
    public JsonResult() {
        this.code = "0";
        this.msg = "操作成功！";
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     * @param code
     * @param msg
     */
    public JsonResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(boolean success, String code, String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     * @param data
     */
    public JsonResult(Object data) {
        this.data = data;
        this.code = "0";
        this.msg = "操作成功！";
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     * @param data
     * @param msg
     */
    public JsonResult(Object data, String msg) {
        this.data = data;
        this.code = "0";
        this.msg = msg;
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     * @param data
     * @param msg
     */
    public static JsonResult success(Object data, String msg) {
        JsonResult jr = new JsonResult();
        jr.setData(data);
        jr.setMsg(msg);
        jr.setSuccess(true);
        return jr;
    }

    public static JsonResult error(Object data, String msg) {
        JsonResult jr = new JsonResult();
        jr.setData(data);
        jr.setMsg(msg);
        jr.setSuccess(false);
        return jr;
    }
    
    public static JsonResult success() {
    	return new JsonResult("0", "操作成功");
    }
    
    public static JsonResult success(Object data) {
    	JsonResult ret = new JsonResult("0", "操作成功");
    	ret.setData(data);
    	return ret;
    }
    
    public static JsonResult error() {
    	JsonResult ret = new JsonResult("500", "系统繁忙");
    	return ret;
    }
    
    public static JsonResult error(String code, String msg) {
    	JsonResult ret = new JsonResult(code, msg);
    	return ret;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}