package com.tr.ui.people.model;

import java.io.Serializable;
import java.security.Timestamp;

/**
 * 人脉、好友目录实体
 *
 * @author xingtianlun
 */
public class PersonCategoryRelation implements Serializable {

    /**
     * 用户或者人脉ID
     */
    public Long personId;

    /**
     * 1-用户;2-人脉
     */
    public Integer personType;

    /**
     * 目录ID(当目录ID为 -1 时，说明目录为顶级目录)
     */
    public Long categoryId;

    /**
     * 1-我创建的人脉;2-我收藏的人脉;5-我的好友
     */
    public Integer ctype;

    /**
     * 创建时间
     */
    public Timestamp ctime;

    /**
     * 用户ID
     */
    public Long userId;

    public static enum Ctype {
        create(1);
        public Integer code;
        Ctype(Integer code) {
            this.code = code;
        }
    }

   
}
