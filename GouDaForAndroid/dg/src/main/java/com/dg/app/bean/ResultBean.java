package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 操作结果实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月14日 下午2:59:27
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class ResultBean extends Base {

    @XStreamAlias("result")
    private Result result;


    public Result getResult() {
	return result;
    }

    public void setResult(Result result) {
	this.result = result;
    }


}
