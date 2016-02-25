package com.tr.ui.people.contactsdetails.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iflytek.speech.UtilityConfig;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.connections.revision20150122.detail.RelationHomeActivity;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.home.frg.FrgFlow;
import com.tr.ui.organization.GetOrgIdUtils;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.Organ_Id;
import com.tr.ui.organization.model.meet.CustomerMeet_Re;
import com.tr.ui.organization.model.meet.CustomerMeetingDetail;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.people.contactsdetails.MeetingDetailsActivity;
import com.tr.ui.people.contactsdetails.NewCreateActivity;
import com.tr.ui.people.contactsdetails.adapter.MyListBaseAdapter;
import com.tr.ui.people.contactsdetails.bean.News;
import com.tr.ui.people.model.CustomerMeetingDetail_requrst;
import com.tr.ui.people.model.MeetList;
import com.tr.ui.people.model.MeetingPersonID;
import com.tr.ui.people.model.Person;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.ScrollViewDelegate;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 会面
 * 
 * @author User
 * 
 */

public class MeetingDetailsFragment extends BaseViewPagerFragment implements
		OnClickListener, OnItemClickListener, IBindData {

	/** 适应框架 */
	private ScrollViewDelegate mScrollViewDelegate = new ScrollViewDelegate();
	private ScrollView mScrollView;

	private BasicListView ptrlv_listView;

	// private XListView ptrlv_listView;

	private TextView add_meeting;

	private Intent intent;
	private GetId getId2;
	private long org;
	private CustomerMeet_Re meet2;
	private MyListBaseAdapter myListBaseAdapter;
	private Organ_Id organ_Id;
	private ArrayList<CustomerMeetingDetail> detail;
	public int type = 0;
	private ContactsDetailsActivity contactsDetailsActivity;
	private ClientDetailsActivity client_DetailsActivity;
	private MeetList peple2;
	private MeetingPersonID meeting_PersonID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.people_contactsdetails_fragment_time_plistview,
				container, false);
		// ContactsDetailsActivity contact = (ContactsDetailsActivity)
		// getActivity();
		// pid = contact.id;
		mScrollView = (ScrollView) view.findViewById(R.id.pScrollView);
		peple2 = new MeetList();
		ptrlv_listView = (BasicListView) view.findViewById(R.id.ptrlv_listView);
		ptrlv_listView.setHaveScrollbar(false);

		ptrlv_listView.setOnItemClickListener(this);
		
		meeting_PersonID = new MeetingPersonID();
		add_meeting = (TextView) view.findViewById(R.id.add_meeting);
		meet2 = new CustomerMeet_Re();
		add_meeting.setOnClickListener(this);
		myListBaseAdapter = new MyListBaseAdapter(meet2.meetingList,
				getActivity());
		ptrlv_listView.setAdapter(myListBaseAdapter);
		// OrganizationReqUtil.doRequestWebAPI(getActivity(), this, meet_Re,
		// null,
		// OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY);
		// ptrlv_listView.setAdapter(myListBaseAdapter);
		// myListBaseAdapter.notifyDataSetChanged();
		// setListViewHeightBasedOnChildren(ptrlv_listView);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (ContactsDetailsActivity.class.getSimpleName().equals(
				getActivity().getClass().getSimpleName())) {
			contactsDetailsActivity = (ContactsDetailsActivity) getActivity();
			meeting_PersonID.personId = contactsDetailsActivity.id;
			PeopleReqUtil.doRequestWebAPI(getActivity(), this,
					meeting_PersonID, null,
					PeopleRequestType.PEOPLE_REQ_MEET_FINDLIST);
			type = ENavConsts.PEOPLE;
		} else if (ClientDetailsActivity.class.getSimpleName().equals(
				getActivity().getClass().getSimpleName())) {
			organ_Id = new Organ_Id();
			client_DetailsActivity = (ClientDetailsActivity) getActivity();
			organ_Id.id = client_DetailsActivity.client_id;
			OrganizationReqUtil.doRequestWebAPI(getActivity(), this, organ_Id,
					null, OrganizationReqType.MEET_FINDLIST);
			type = ENavConsts.CLIENT;
		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				- 45;
		listView.getDividerHeight();
		listView.setLayoutParams(params);
	}

	public static MeetingDetailsFragment newInstance(int index) {
		MeetingDetailsFragment fragment = new MeetingDetailsFragment();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
	}

	@Override
	public void onClick(View v) {
		if (type == ENavConsts.PEOPLE) {
			intent = new Intent(getActivity(), NewCreateActivity.class);
			intent.putExtra("Org_Id", contactsDetailsActivity.id);
			intent.putExtra("TYPE", type);
			startActivityForResult(intent, 9);
		} else if (type == ENavConsts.CLIENT) {
			intent = new Intent(getActivity(), NewCreateActivity.class);
			intent.putExtra("Org_Id", client_DetailsActivity.client_id);
			intent.putExtra("TYPE", type);
			startActivityForResult(intent, 9);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Organ_Id meet_id = new Organ_Id();
		if (type == ENavConsts.CLIENT) {
			meet_id.id = detail.get((int) arg3).id;
			OrganizationReqUtil.doRequestWebAPI(getActivity(), this, meet_id,
					null, OrganizationReqType.MEET_FINDONE);
		} else if (type == ENavConsts.PEOPLE) {
			meet_id.id = peple2.meetList.get((int) arg3).id;
			PeopleReqUtil.doRequestWebAPI(getActivity(), this, meet_id, null,
					PeopleRequestType.PEOPLE_REQ_MEET_FINDONE);
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case EAPIConsts.OrganizationReqType.MEET_FINDLIST: // 7.3 查询会面情况列表
															// /customer/meet/findList.json
			Map<String, Object> meet_map = (Map<String, Object>) object;
			if (meet_map!=null) {
				detail = (ArrayList<CustomerMeetingDetail>) meet_map
						.get("meetList");
				if (detail != null) {
					// 数据已返回，解析出现问题
					myListBaseAdapter.putCustomerMeetingDetail_List(detail);
					myListBaseAdapter.notifyDataSetChanged();
//					setListViewHeightBasedOnChildren(ptrlv_listView);
				}

			}
			
			break;
		case EAPIConsts.OrganizationReqType.MEET_FINDONE: // 7.4 查询单个会面情况对象
															// /customer/meet/findOne.json
			Map<String, Object> one_meet = (Map<String, Object>) object;
			CustomerMeetingDetail one = (CustomerMeetingDetail) one_meet
					.get("result");
			if (one != null) {
				intent = new Intent(getActivity(), MeetingDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("customerMeetingDetail", one);
				intent.putExtras(bundle);
				intent.putExtra("MeetId", one.id);
				intent.putExtra("TYPE", type);
				getActivity().startActivityForResult(intent, 9);
			}
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_FINDLIST:
			MeetList people = (MeetList) object;
			peple2 = people;
			if (people != null) {
				if (people.meetList != null) {
					// 数据已返回，解析出现问题
					myListBaseAdapter
							.putCustomerMeetingDetail_List(people.meetList);
					myListBaseAdapter.notifyDataSetChanged();
					// setListViewHeightBasedOnChildren(ptrlv_listView);
				}
			}
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_FINDONE: // 查询单个会面情况
			CustomerMeetingDetail_requrst one_people = (CustomerMeetingDetail_requrst) object;
			if (one_people != null) {
				if (one_people.success) {

					intent = new Intent(getActivity(),
							MeetingDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("customerMeetingDetail",
							one_people.result);
					intent.putExtras(bundle);
					intent.putExtra("MeetId", one_people.result.id);
					intent.putExtra("TYPE", type);
					getActivity().startActivityForResult(intent, 10);
				}
			}
			break;
		default:
			break;
		}
	}
}
