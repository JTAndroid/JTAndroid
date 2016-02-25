package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.ui.tongren.model.message.MessageType;
import com.tr.ui.tongren.model.message.TongRenMessage;
import com.utils.time.TimeUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TongRenMessageAdapter extends BaseAdapter {
	private List<TongRenMessage> listTongRenMessage = new ArrayList<TongRenMessage>();
	private Context mContext;
	private TongRenMessageOperateListener mTongRenMessageOperateListener;
	/* 普通消息 */
	public final int GENERAL_MESSAGE = 0;
	/* 组织同意、拒绝操作 begin */
	public final int INVITATION = 1;
	public final int APPLICATION = 2;
	public final int SIGNOUT = 3;
	/* 组织同意、拒绝操作 end */
	/* 组织删除操作 begin */
	public final int AGREEJIONIN = 4;
	public final int REFUSEAGREEJOININ = 5;
	public final int AGREESIGNOUT = 6;
	public final int REFUSEAGREESIGNOUT = 7;
	public final int DISSOLUTION = 8;
	public final int KICKED = 9;
	/* 组织删除操作 end */
	/* 组织查看操作begin */
	public final int ASSIGNMENT_TASK = 10;
	public final int RETURN_TASK = 11;
	public final int REPEAT_TASK = 12;
	/* 组织查看操作end */
	/* 项目删除操作begin */
	public final int GIVE_UP_PROJECT = 13;
	public final int END_PROJECT = 14;
	/* 项目删除操作end */
	/* 项目查看操作begin */
	public final int ORGANIZATION_TO_UNDERTAKE_PROJECTS = 15;
	public final int PROJECT_DOCUMENT = 16;
	public final int ASSIGNMENT_SUB_TASK = 17;
	/* 项目查看操作end */
	/* 项目同意、拒绝操作begin */
	public final int INVITATION_PROJECT = 18;// 主要是项目可以发起申请承接人承接项目
	/* 项目同意、拒绝操作end */
	/* 项目删除操作begin */
	public final int AGREE_TO_UNDERTAKE_PROJECTS = 19;
	public final int REFUSEAGREE_TO_UNDERTAKE_PROJECTS = 20;
	/* 项目删除操作end */
	/* 项目同意、拒绝操作begin */
	public final int APPLICATION_EXTENSION_PROJECT = 21;
	/* 项目同意、拒绝操作end */
	/* 项目删除操作begin */
	public final int AGREE_APPLICATION_EXTENSION_PROJECT = 22;
	public final int REFUSEAGREE_APPLICATION_EXTENSION_PROJECT = 23;
	public final int ORGANIZATION_TO_ASSIGNMENT = 24;
	public TongRenMessageAdapter(Context context,
			TongRenMessageOperateListener tongRenMessageOperateListener) {
		this.mContext = context;
		this.mTongRenMessageOperateListener = tongRenMessageOperateListener;
	}

	public List<TongRenMessage> getListTongRenMessage() {
		return listTongRenMessage;
	}

	public void setListTongRenMessage(List<TongRenMessage> listTongRenMessage) {
		this.listTongRenMessage = listTongRenMessage;
	}

	@Override
	public int getCount() {
		return listTongRenMessage.size();
	}

	@Override
	public TongRenMessage getItem(int position) {
		return listTongRenMessage.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TongRenMessageViewHolder holder;
		if (convertView == null) {
			holder = new TongRenMessageViewHolder();
			convertView = View.inflate(mContext,
					R.layout.adapter_tongrenmessage, null);
			holder.messageAgreeTv = (TextView) convertView
					.findViewById(R.id.messageAgreeTv);
			holder.messageTitleTv = (TextView) convertView
					.findViewById(R.id.messageTitleTv);
			holder.messageLogoIv = (ImageView) convertView
					.findViewById(R.id.messageLogoIv);
			holder.messageRefuseTv = (TextView) convertView
					.findViewById(R.id.messageRefuseTv);
			holder.messageDeleteTv = (TextView) convertView
					.findViewById(R.id.messageDeleteTv);
			holder.messageContentTv = (TextView) convertView
					.findViewById(R.id.messageContentTv);
			holder.messageTimeTv = (TextView) convertView
					.findViewById(R.id.messageTimeTv);
			holder.messageOperationLl = (LinearLayout) convertView
					.findViewById(R.id.messageOperationLl);
			convertView.setTag(holder);
		} else {
			holder = (TongRenMessageViewHolder) convertView.getTag();
		}
		final TongRenMessage itemTongRenMessage = getItem(position);
		if (itemTongRenMessage.messageType != 0) {
			switch (itemTongRenMessage.messageType) {
			/* 同意、拒绝操作 begin */
			case INVITATION:
			case APPLICATION:
			case SIGNOUT:
			case INVITATION_PROJECT:
			case APPLICATION_EXTENSION_PROJECT:
				holder.messageOperationLl.setVisibility(View.VISIBLE);
				holder.messageAgreeTv.setVisibility(View.VISIBLE);
				holder.messageRefuseTv.setVisibility(View.VISIBLE);
				holder.messageDeleteTv.setVisibility(View.GONE);
				break;
			/* 删除操作 begin */
			case AGREEJIONIN:
			case ORGANIZATION_TO_ASSIGNMENT:
			case REFUSEAGREEJOININ:
			case AGREESIGNOUT:
			case REFUSEAGREESIGNOUT:
			case DISSOLUTION:
			case KICKED:
			case GIVE_UP_PROJECT:
			case END_PROJECT:
			case ORGANIZATION_TO_UNDERTAKE_PROJECTS:
			case PROJECT_DOCUMENT:
			case ASSIGNMENT_SUB_TASK:
			case AGREE_TO_UNDERTAKE_PROJECTS:
			case REFUSEAGREE_TO_UNDERTAKE_PROJECTS:
			case AGREE_APPLICATION_EXTENSION_PROJECT:
			case REFUSEAGREE_APPLICATION_EXTENSION_PROJECT:
			case ASSIGNMENT_TASK:
			case RETURN_TASK:
			case REPEAT_TASK:
				holder.messageOperationLl.setVisibility(View.GONE);
				holder.messageAgreeTv.setVisibility(View.GONE);
				holder.messageRefuseTv.setVisibility(View.GONE);
				holder.messageDeleteTv.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
		if (!TextUtils.isEmpty(itemTongRenMessage.sendUserPic)) {
			ImageLoader.getInstance().displayImage(
					itemTongRenMessage.sendUserPic, holder.messageLogoIv);
		}
		holder.messageTitleTv.setText(itemTongRenMessage.title);
		holder.messageContentTv.setText(itemTongRenMessage.content);
		holder.messageTimeTv.setText(TimeUtil
				.TimeMillsToString(itemTongRenMessage.sendTime));
		holder.messageAgreeTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTongRenMessageOperateListener.doAgree(v,
						itemTongRenMessage.messageReceiveID);
				holder.messageOperationLl.setVisibility(View.GONE);
			}
		});
		holder.messageRefuseTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTongRenMessageOperateListener.doRefuse(v,
						itemTongRenMessage.messageReceiveID);
				holder.messageOperationLl.setVisibility(View.GONE);
			}
		});
		holder.messageDeleteTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTongRenMessageOperateListener.doDelete(itemTongRenMessage);
			}
		});
		return convertView;
	}

	class TongRenMessageViewHolder {
		public LinearLayout messageOperationLl;
		ImageView messageLogoIv;
		TextView messageTitleTv, messageAgreeTv, messageRefuseTv,
				messageDeleteTv, messageContentTv, messageTimeTv;
	}

	public interface TongRenMessageOperateListener {
		void doAgree(View view, String messageReceiveID);

		void doRefuse(View view, String messageReceiveID);

		void doDelete(TongRenMessage itemTongRenMessage);
	}
}
