package com.utils.common;

/** 社交类型
 * 
 * @author qingc */
public enum SocialType {
	/** 私聊 */
	PRIVATE_CHAT {
		@Override
		public String toString() {
			return "私聊";
		}

		@Override
		public int code() {
			return 1;
		}
	},
	/** 群聊 */
	GROUP_CHAT {
		@Override
		public String toString() {
			return "群聊";
		}

		@Override
		public int code() {
			return 2;
		}
	},
	/** 进行中会议 */
	IN_MEETING {
		@Override
		public String toString() {
			return "进行中会议";
		}

		@Override
		public int code() {
			return 3;
		}
	},
	/** 已结束会议 */
	ENDED_MEETING {
		@Override
		public String toString() {
			return " 已结束会议";
		}

		@Override
		public int code() {
			return 5;
		}
	},
	/** 未开始会议 */
	NOT_BEGIN_MEETING {
		@Override
		public String toString() {
			return "未开始会议";
		}

		@Override
		public int code() {
			return 4;
		}

	}

	,
	/** 通知 */
	NOTICE {
		@Override
		public String toString() {
			return "通知";
		}

		@Override
		public int code() {
			return 6;
		}
	},
	/** 邀请函 */
	INVITATION {
		@Override
		public String toString() {
			return "邀请函";
		}

		@Override
		public int code() {
			return 7;
		}
	};
	@Override
	public abstract String toString();

	public abstract int code();

}
