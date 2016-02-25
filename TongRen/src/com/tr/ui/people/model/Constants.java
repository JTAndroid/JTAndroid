package com.tr.ui.people.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Constants implements Serializable {
    /** 未分组SORTID值 **/
    public final static String unGroupSortId = "111111111";
    /** 未分组名称 **/
    public final static String unGroupSortName = "未分组";
    public static int success = 0;
    public static int fail = 1;

    public enum ErrorMessage {
        levelPathTooLong("目录级别不能超过5级!"), hasName("名称重复!"), sensitiveWord("您的文章存在敏感词"), artNotExsit(
                "亲爱的用户你好：你所查看的文章不存在或被删除!"), addKnowledgeFail("添加知识失败!"), addCollFail("文章收藏失败!"), addColumnFail(
                "添加栏目失败!"), alreadyCollection("您已经收藏过该文章!"), addCommentFail("评论失败!"), artUserNotExsit("文章作者不存在!"), addReportFail(
                "添加举报失败!"), columnNotFound("未找到知识所属栏目"), addFriendsFail("添加好友失败!"), addFriendsWaiting(
                "您已申请过添加好友,请耐心等待!"), checkColumnFail("栏目名已存在!"), IsFriends("您与该用户已是好友关系!"), UserNotExisitInSession(
                "请确认是否登陆!"), contentIsBlank("评论内容不能为空!"), commentNotExsit("评论不存在!"), delCommentNotPermission("无权删除该评论!"), delCommentFail(
                "删除评论失败!"), notFindColumn("栏目不存在，请刷新页面后重试!"), delColumnNotPermission("无权删除该栏目!"), delFail("删除失败!"), paramNotValid(
                "用户权限参数不合法!"), updateFail("更新失败!"), paramNotBlank("参数不能为空"), contentTooLong("内容过长"), artPermissionNotFound(
                "对不起,您没有查看该文章的权限!"), parseError("解析错误!"), userNotLogin("您未登陆,请先登陆!"), addasso("添加关联失败!");
        String v;

        ErrorMessage(String v) {
            this.v = v;
        }

        public String v() {
            return v;
        }
    }

    public static long gtnid = 0l;

    public final static String status = "result";

    public final static String msg = "msg";

    public final static String permIds = "permIds";

    public final static String categoryIds = "categoryIds";

    public final static String TAG = "tag";

    public final static String CONN = "conn";

    public final static Long ALL = -9l;

    public final static String asso = "asso";

    /** 全平台 **/
    public final static String platform = "\"id\":-1";

    /** 金桐脑 **/
    public final static String gintong = "\"id\":0";

    /** 独乐 **/
    public final static String dule = "dule";
    /** 大乐 **/
    public final static String dales = "dales";
    /** 中乐 **/
    public final static String zhongles = "zhongles";
    /** 小乐 **/
    public final static String xiaoles = "xiaoles";

    public enum ResultType {
        fail(0), success(1);

        private int v;

        private ResultType(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public static enum PermissionType {
        dule(1, "dule"), dales(2, "dales"), zhongles(3, "zhongles"), xiaoles(4,
                "xiaoles");
        private int v;

        private String c;

        private PermissionType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    public static enum Ids {
        jinTN(0), platform(-1);

        private int v;

        private Ids(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public static enum ConnectType {
        // 关联格式（p:人脉,r:事件,o:组织,k:知识）
        event(1, "r"), people(2, "p"), organization(5, "o"), knowledge(6, "k");
        private int v;

        private String c;

        private ConnectType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    public static enum DemandStatus {
        draft(1), waitcheck(2), checking(3), checked(4), uncheck(5), recycle(6), foreverdelete(
                7), forbid(8);
        private int v;

        private DemandStatus(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }

    }

    public static enum DemandRelationOpeType {
        news(1), save(2), collection(3);
        private int v;

        private DemandRelationOpeType(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }

    }
    
    public enum noticeType {
        demand(1, "需求"),demandRelation(2, "操作关系"),dynamic(3,"需求动态");

        private int v;
        private String c;

        private noticeType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    public static long selectPk() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dd = dateFormat.format(now);
        int max = 1000;
        int min = 100;
        Random random = new Random();
        int ran = random.nextInt(max) % (max - min) + min;
        long rel = Long.parseLong(dd + ran);
        return rel;
    }

}
