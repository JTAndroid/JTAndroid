package com.tr.ui.conference.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.InviteFriendActivity;
import com.tr.ui.widgets.CircleImageView;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/**
 * 将与会信息 和 主讲人设置界面 中 GridView的数据适配器抽出，共用 。有+ - 和 item 右上角角标
 * 
 * @author zhongshan
 * 
 */
public class GridViewMemberListAdapter extends BaseAdapter implements IBindData {

	private Context mContext;

	/** 人 头像的右上角 是否显示删除按钮 */
	private boolean showDeleteButton = false;
	/** 是否显示添加 和 删除按钮项 */
	private boolean isShowAddandDeleteButton = false;
	
	private int clickPosition = -1;

	private List<MMeetingMember> listMeetingMember = new ArrayList<MMeetingMember>();
	private DisplayImageOptions options;
	private AnimateFirstDisplayListener animateFirstDisplayListener;

	public GridViewMemberListAdapter(List<MMeetingMember> listMeetingMember, Context mContext) {
		super();
		this.listMeetingMember = listMeetingMember;
		this.mContext = mContext;
	}

	public List<MMeetingMember> getListMeetingMember() {
		return listMeetingMember;
	}

	public void setListMeetingMember(List<MMeetingMember> listMeetingMember) {
		this.listMeetingMember = listMeetingMember;
	}

	public void setShowAddandDeleteButton(boolean isShowAddandDeleteButton) {
		this.isShowAddandDeleteButton = isShowAddandDeleteButton;
	}

	public boolean isShowDeleteButton() {
		return showDeleteButton;
	}

	/**
	 * 设置 是否显示 头像右上角的 红色删除按钮
	 * 
	 * @param showDeleteButton
	 */
	public void setShowDeleteButton(boolean showDeleteButton) {
		this.showDeleteButton = showDeleteButton;
	}

	@Override
	public int getCount() {
		// "+2":多出两项为：+ 、 —
		return isShowAddandDeleteButton ? listMeetingMember.size() + 2 : listMeetingMember.size();
	}

	@Override
	public MMeetingMember getItem(int position) {
		if (isShowAddandDeleteButton) {
			return position < getCount() - 2 ? listMeetingMember.get(position) : null;
		} else {
			return listMeetingMember.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		if (isShowAddandDeleteButton) {
			if (position < getCount() - 2) {
				return listMeetingMember.get(position) != null ? listMeetingMember.get(position).getMemberId() : 0;
			} else {
				return 0;
			}
		} else {
			return listMeetingMember.get(position) != null ? listMeetingMember.get(position).getMemberId() : 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.gridview_show_delete_item, null);
			viewHolder = new ViewHolder();
			viewHolder.peopleIv = (CircleImageView) convertView.findViewById(R.id.peopleIv);
			viewHolder.deleteFl = (FrameLayout) convertView.findViewById(R.id.deleteFl);
			viewHolder.addBtIv = (ImageView) convertView.findViewById(R.id.addBtIv);
			viewHolder.deleteBtIv = (ImageView) convertView.findViewById(R.id.deleteBtIv);
			viewHolder.registrationIv = (ImageView) convertView.findViewById(R.id.registrationIv);
			viewHolder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
			viewHolder.locationTv = (TextView) convertView.findViewById(R.id.locationTv);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		/** 显示 GridView中添加和删除按钮 */
		if (isShowAddandDeleteButton) {
			/** 显示参会人头像 */
			if (position < getCount() - 2) {
				initAttendMeetingMemberItem(viewHolder, position);
			}
			/** 显示 + */
			else if (position == getCount() - 2) {
				viewHolder.addBtIv.setVisibility(View.VISIBLE);
				viewHolder.deleteBtIv.setVisibility(View.GONE);
				viewHolder.peopleIv.setVisibility(View.GONE);
				viewHolder.deleteFl.setVisibility(View.GONE);
				viewHolder.registrationIv.setVisibility(View.GONE);
				viewHolder.nameTv.setVisibility(View.INVISIBLE);
				viewHolder.locationTv.setVisibility(View.INVISIBLE);
			}
			/** 显示 — */
			else if (position == getCount() - 1) {
				viewHolder.deleteBtIv.setVisibility(View.VISIBLE);
				viewHolder.addBtIv.setVisibility(View.GONE);
				viewHolder.peopleIv.setVisibility(View.GONE);
				viewHolder.deleteFl.setVisibility(View.GONE);
				viewHolder.registrationIv.setVisibility(View.GONE);
				viewHolder.nameTv.setVisibility(View.INVISIBLE);
				viewHolder.locationTv.setVisibility(View.INVISIBLE);
			}
		} else {
			initAttendMeetingMemberItem(viewHolder, position);
		}
		return convertView;
	}

	private void initAttendMeetingMemberItem(ViewHolder viewHolder, int position) {
		MMeetingMember mMeetingMember = listMeetingMember.get(position);
		viewHolder.peopleIv.setVisibility(View.VISIBLE);
		viewHolder.addBtIv.setVisibility(View.GONE);
		viewHolder.deleteBtIv.setVisibility(View.GONE);
		viewHolder.nameTv.setVisibility(View.VISIBLE);
		viewHolder.locationTv.setVisibility(View.VISIBLE);
		if (isShowDeleteButton()) {
			if (isMySelfOrSpeaker(mMeetingMember)) {
				viewHolder.deleteFl.setVisibility(View.GONE);
			} else {
				viewHolder.deleteFl.setVisibility(View.VISIBLE);
			}
		} else {
			viewHolder.deleteFl.setVisibility(View.GONE);
		}
		if (options == null) {
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_people_avatar).showImageForEmptyUri(R.drawable.default_people_avatar)
					.showImageOnFail(R.drawable.default_people_avatar).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).build();
		}
		if (animateFirstDisplayListener == null) {
			animateFirstDisplayListener = new AnimateFirstDisplayListener();
		}
		ImageLoader.getInstance().displayImage(mMeetingMember.getMemberPhoto(), viewHolder.peopleIv, options, animateFirstDisplayListener);
		viewHolder.nameTv.setText(mMeetingMember.getMemberName());
		/** 是否签到 0：未签，1：已签 */
		if (mMeetingMember.getIsSign() == 1) {
			viewHolder.registrationIv.setVisibility(View.VISIBLE);
		} else {
			viewHolder.registrationIv.setVisibility(View.GONE);
		}
		if (StringUtils.isEmpty(mMeetingMember.getSignDistance())) {
			viewHolder.locationTv.setText("未知");
		} else {
			viewHolder.locationTv.setText(mMeetingMember.getSignDistance());
		}
	}

	private class ViewHolder {
		/** 头像 */
		CircleImageView peopleIv;
		/** 头像右上角 红色删除按钮 */
		FrameLayout deleteFl;
		/** 倒数第二个 添加按钮 */
		ImageView addBtIv;
		/** 倒数第一个 删除按钮 */
		ImageView deleteBtIv;
		/** 头像左上角 签到按钮 */
		ImageView registrationIv;
		/** 参会人姓名 */
		TextView nameTv;
		/** 位置 */
		TextView locationTv;
	}

	/**
	 * 点击事件的处理
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void onItemClickOpt(AdapterView<?> parent, View view, int position, long id ,long meetingId ,MMeetingQuery meetingQuery,ArrayList<MMeetingMember> addListMeetingMembers,int requestCode) {
		if (isShowAddandDeleteButton) {
			/** 参会人 */
			if (position < this.getCount() - 2) {
				/** 右上角显示 红色删除按钮——点击删除参会人 */
				if (this.isShowDeleteButton()) {
					if (isMySelfOrSpeaker(getItem(position))) {
						ENavigate.startRelationHomeActivity(mContext, getItemId(position) + "");
					}else {
						clickPosition = position;
						if (getItem(position).isAttendInvite()) {
							addListMeetingMembers.remove(InitiatorDataCache.getInstance().inviteAttendSelectedMap.get(getItem(position).getMemberId()+""));
							InitiatorDataCache.getInstance().inviteAttendSelectedMap.remove(getItem(position).getMemberId()+"");
							listMeetingMember.remove(position);
							notifyDataSetChanged();
						}else {
							ConferenceReqUtil.doDeleteMeetingMember(mContext, this, meetingId, getItemId(position), null);
						}
					}
				}
				/** 查看参会人详情 */
				else {
					ENavigate.startRelationHomeActivity(mContext, getItemId(position) + "");
				}
			}
			/** + */
			else if (position == getCount() - 2) {
				//跳转到选择联系人页面
				Bundle b = new Bundle();
				b.putInt(Util.IK_VALUE, InviteFriendActivity.TYPE_INVITE_ATTEND_FRIEND);
				b.putBoolean("showOtherInvite", true);
				b.putSerializable(ENavConsts.EMeetingDetail, meetingQuery);
				Util.forwardTargetActivityForResult((Activity)mContext, InviteFriendActivity.class, b, requestCode);
			}
			/** - */
			else if (position == getCount() - 1) {
				setShowDeleteButton(!isShowDeleteButton());
				notifyDataSetChanged();
			}
		} else {
			/** 查看参会人详情 */
			ENavigate.startRelationHomeActivity(mContext, getItemId(position) + "");
		}
	}

	/**
	 * 是否是自己或主讲人
	 * 
	 * @param mMeetingMember
	 * @return true 自己或主讲人 false其他人
	 */
	private boolean isMySelfOrSpeaker(MMeetingMember mMeetingMember) {
		boolean isMySelfOrSpeaker = false;
		if (mMeetingMember != null) {
			/** 角色 0：嘉宾 1：群众 2：创建 */
			isMySelfOrSpeaker = mMeetingMember.getMemberType() != 1 ? true : false;
		}
		return isMySelfOrSpeaker;
	}

	@Override
	public void bindData(int tag, Object object) {
		if (object==null) {
			return;
		}
		if (tag==EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_MEETING_MEMBER) {
			boolean b = (Boolean) object;
			if (b&&clickPosition!=-1) {
				listMeetingMember.remove(clickPosition);
				notifyDataSetChanged();
			}
		}
	}

}
