package com.utils.http;

/**
 * 绑定数据
 * @author Michael
 * @des: 所有调用接口api的activity，如果需要获取接口返回数据的，都需要从该接口集成，数据将从 bindData中传回
 * @tag： 对应 EAPITASK中的MSG_***,一个常量对应一个命令
 * @object：后台返回的数据类对象
 *
 */

public interface IBindData {
    public abstract void bindData(int tag, Object object);
}