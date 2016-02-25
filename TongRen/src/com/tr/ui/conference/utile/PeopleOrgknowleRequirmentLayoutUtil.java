package com.tr.ui.conference.utile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jsoup.helper.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MMeetingData;
import com.tr.model.conference.MMeetingOrgan;
import com.tr.model.conference.MMeetingPeople;
import com.tr.navigate.ENavigate;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.image.LoadImage;
import com.utils.string.StringUtils;
/**
 * 会议详情和与会信息中人 组织 知识 事件的布局初始化
 * @author Big
 *
 */
public class PeopleOrgknowleRequirmentLayoutUtil {
	/**
	 * 初始化指定位置的人脉
	 * 
	 * @param position
	 * @return
	 */
	public static ViewGroup initPeople(final List<MMeetingPeople> listMeetingPeople,final int position,final Context mContext) {
		LinearLayout people = null;
		if (listMeetingPeople!= null && position < listMeetingPeople.size()) {
			people = (LinearLayout) View.inflate(mContext, R.layout.hy_list_item_title_contact, null);
			ImageView attendeeLogoIv = (ImageView) people.findViewById(R.id.hy_contact_iv_logo);
			TextView attendeeNameTv = (TextView) people.findViewById(R.id.hy_contact_tv_name);
			String peopleName = listMeetingPeople.get(position).getPeopleName();
			String peoplePhoto = listMeetingPeople.get(position).getPeoplePhoto();
//			Util.initAvatarImage(mContext, attendeeLogoIv, peopleName, peoplePhoto,0,1);
			if (!StringUtils.isEmpty(peoplePhoto)) {
				ImageLoader.getInstance().displayImage(listMeetingPeople.get(position).getPeoplePhoto(), attendeeLogoIv,LoadImage.mDefaultHead);
			}else {
				attendeeLogoIv.setBackgroundResource(R.drawable.ic_default_avatar);
			}
			if (!StringUtils.isEmpty(peopleName)) {
				attendeeNameTv.setText(peopleName);
			}
			people.setClickable(true);
			people.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MMeetingPeople aPeople = listMeetingPeople.get(position);
					if (aPeople.getPeopleType()==2) {
						ENavigate.startRelationHomeActivity(mContext, aPeople.getPeopleId()+"");
					}else {
						ENavigate.startContactsDetailsActivity(mContext, 2, aPeople.getPeopleId(), 0, 1);
					}
				}
			});
		}
		return people;
	}

	/**
	 * 初始化指定位置的组织
	 * 
	 * @param position
	 * @return
	 */
	public static ViewGroup initOrgan(final List<MMeetingOrgan> listMeetingOrgan,final int position,final Context mContext) {
		LinearLayout orgLL = null;
		if (listMeetingOrgan!= null && position < listMeetingOrgan.size()) {
			orgLL = (LinearLayout) View.inflate(mContext, R.layout.hy_list_item_title_contact, null);
			ImageView orgIv = (ImageView) orgLL.findViewById(R.id.hy_contact_iv_logo);
			TextView orgnameTv = (TextView) orgLL.findViewById(R.id.hy_contact_tv_name);
			
			String orgName = listMeetingOrgan.get(position).getOrganName();
			String orgPhoto = listMeetingOrgan.get(position).getOrganPhoto();
//			Util.initAvatarImage(mContext, orgIv, orgName, orgPhoto,0,2);
			if (!StringUtils.isEmpty(orgPhoto)) {
				ImageLoader.getInstance().displayImage(listMeetingOrgan.get(position).getOrganPhoto(), orgIv,LoadImage.mOrganizationDefaultHead);
			}else {
				orgIv.setBackgroundResource(R.drawable.org_default_orgnization);
			}
			if (!StringUtils.isEmpty(orgName)) {
				orgnameTv.setText(orgName);
			}
			
			orgLL.setClickable(true);
			orgLL.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MMeetingOrgan mMeetingOrgan =listMeetingOrgan.get(position);
					if (mMeetingOrgan.getOrganType()==2) {
						ENavigate.startClientDedailsActivity(mContext, Long.valueOf(mMeetingOrgan.getOrganId()));
					}else {
						ENavigate.startOrgMyHomePageActivity(mContext, Long.valueOf(mMeetingOrgan.getOrganId()), 0, true, 0);
					}
				}
			});
		}
		return orgLL;
	}
	
	/**
	 * 初始化一个知识
	 * 
	 * @param position
	 * @return
	 */
	public static ViewGroup initKnowledge(final List<MMeetingData> listMeetingKnowledge,final int position,final Context mContext) {
		RelativeLayout knowledge = null;
		if (listMeetingKnowledge != null && position < listMeetingKnowledge.size()) {
			MMeetingData dData = listMeetingKnowledge.get(position);
			if (null != dData) {
				knowledge = (RelativeLayout) View.inflate(mContext, R.layout.hy_list_item_meeting_knowledge, null);
				TextView content = (TextView) knowledge.findViewById(R.id.hy_knowledge_tv_content);
				TextView time = (TextView) knowledge.findViewById(R.id.hy_knowledge_tv_time);

				content.setText(dData.getDataName());
				// TODO时间格式

				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if (false == dData.getCreateTime().isEmpty()) {
						Date tmp = (fmt.parse(dData.getCreateTime()));
						time.setText(dateFormat4.format(tmp));
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				// 知识点击事件
				knowledge.setClickable(true);
				knowledge.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ENavigate.startKnowledgeOfDetailActivity((Activity) mContext, listMeetingKnowledge.get(position).getDataId(),listMeetingKnowledge.get(position)
								.getDataReqType());
					}
				});
			}
		}
		return knowledge;
	}
	
	
	/**
	 * 初始化一个需求
	 * 
	 * @param position
	 * @return
	 */
	public static ViewGroup initRequirment(final List<MMeetingData> listMeetingRequirement,final int position,final Context mContext) {
		RelativeLayout requirment = null;
		if (listMeetingRequirement != null && position < listMeetingRequirement.size()) {
			requirment = (RelativeLayout) View.inflate(mContext, R.layout.hy_list_item_meeting_requirement, null);
			ImageView img = (ImageView) requirment.findViewById(R.id.hy_requirment_iv_logo);
			TextView content = (TextView) requirment.findViewById(R.id.hy_requirment_tv_content);
			TextView time = (TextView) requirment.findViewById(R.id.hy_requirment_tv_time);
			// 0 投资 1 融资 详见接口文档
			if (listMeetingRequirement.get(position).getDataReqType() == 12) {
				img.setBackgroundResource(R.drawable.hy_icon_meeting_detail_invest);
			} else if (listMeetingRequirement.get(position).getDataReqType() == 13) {
				img.setBackgroundResource(R.drawable.hy_icon_meeting_detail_finacing);
			}
			if (listMeetingRequirement.get(position).getDataName() != null) {
				content.setText(listMeetingRequirement.get(position).getDataName());
			}
			if (listMeetingRequirement.get(position).getCreateTime() != null) {
				MMeetingData dData = listMeetingRequirement.get(position);
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if (false == dData.getCreateTime().isEmpty()) {
						Date tmp = (fmt.parse(dData.getCreateTime()));
						time.setText(dateFormat4.format(tmp));
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

			}
			// 添加点击事件
			requirment.setClickable(true);
			requirment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 点击后跳转到详细信息页面
//					ENavigate.startRequirementDetailActivity(mContext, String.valueOf(listMeetingRequirement.get(position).getDataId()));
					ENavigate.startNeedDetailsActivity(mContext, listMeetingRequirement.get(position).getDataId() + "", 2);

				}
			});
		}
		return requirment;
	}
}
