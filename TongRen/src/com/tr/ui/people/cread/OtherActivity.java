package com.tr.ui.people.cread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.model.demand.Metadata;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.model.Area;
import com.tr.ui.people.model.BaseResult;
import com.tr.ui.people.model.Basic;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.PeopleRequest;
import com.tr.ui.people.model.PermIds;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonTagRelation;
import com.tr.ui.people.model.PersonalInformation;
import com.tr.ui.work.WorkDatePickerDialog;
import com.tr.ui.work.WorkDatePickerDialog.OnDayChangeListener;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/**
 * 其他
 * 
 */
public class OtherActivity extends JBaseActivity implements OnClickListener, IBindData {
	@ViewInject(R.id.item_other)
	private RelativeLayout item_start_time;// 隐藏

	@ViewInject(R.id.text_birthdate)
	private TextView text_birthdate;// 出生日期
	@ViewInject(R.id.my_native_place)
	private EditText native_place;// 籍贯
	@ViewInject(R.id.hobbies)
	private EditText hobbies;// 爱好
	@ViewInject(R.id.be_skilled_in)
	private EditText be_skilled_in;// 擅长技能
	@ViewInject(R.id.text_other_action)
	private TextView text_other_action;// 保存

	private List<PersonalInformation> personalInformationList = new ArrayList<PersonalInformation>();
	private PeopleDetails people_details = new PeopleDetails();// 人脉详情对象
	/** 向后台提交的人脉对象 */
	private PeopleRequest people_request;
	private Person person;
	private ArrayList<Long> categoryList = new ArrayList<Long>(); // 目录
	private ArrayList<Long> tid = new ArrayList<Long>(); // 标签
	private PermIds permIds; // 权限
	private ArrayList<Metadata> metadataArea; // 区域
	private Area area_result; // 所在地区对象
	private String birthdate = "";// 出生日期
	private String birthPlace = "";// 籍贯
	private String goodAt = "";// 擅长
	private String hobby = "";// 爱好
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private PersonalInformation information = new PersonalInformation();
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "其他", false, null, false, true);
		setContentView(R.layout.activity_other);
		getBundle();
		ViewUtils.inject(this);
		initView();
	}

	private void getBundle() {
		people_details = (PeopleDetails) this.getIntent().getSerializableExtra(ENavConsts.datas);
		if (people_details!=null) {
			person = people_details.people;
			personalInformationList = people_details.people.getPersonalInformationList();
		}
		
	}

	private void initView() {
		item_start_time.setVisibility(View.GONE);
		text_other_action.setOnClickListener(this);
		text_birthdate.setOnClickListener(this);
		native_place.setOnClickListener(this);
		if (personalInformationList != null) {
			for (int i = 0; personalInformationList != null && i < personalInformationList.size(); i++) {
				List<Basic> keyDate = personalInformationList.get(i).keyDate;
				for (int j = 0; keyDate != null && j < keyDate.size(); j++) {
					if (keyDate.get(j).type.contains("1")) {
						birthdate = keyDate.get(j).content;
					}
				}
				information=personalInformationList.get(i);
				birthPlace = information.getBirthCountry() + information.getBirthCity() + information.getBirthCounty();
				goodAt = personalInformationList.get(i).getGoodAt();
				hobby = personalInformationList.get(i).getHobby();
			}
			native_place.setFocusable(false);
			if (!TextUtils.isEmpty(birthdate))
				text_birthdate.setText(birthdate);
			if (!TextUtils.isEmpty(birthPlace))
				native_place.setText(birthPlace);
			if (!TextUtils.isEmpty(goodAt))
				hobbies.setText(hobby);
			if (!TextUtils.isEmpty(hobby))
				be_skilled_in.setText(goodAt);

		} else {

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_native_place:// 地区
			ENavigate.startChooseActivityForResult(this, false, "区域", ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.text_other_action:
			onSave();
			break;
		case R.id.text_birthdate:// 出生日期
			WorkDatePickerDialog datePicKDialog = new WorkDatePickerDialog(OtherActivity.this, null);
			datePicKDialog.setSimpleDateFormat(dateFormat);
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(OtherActivity.this.INPUT_METHOD_SERVICE);
			datePicKDialog.dateTimePicKDialog(0);

			datePicKDialog.setDayChangeListener(mStartTime);

			if (inputmanger.isActive()) {
				inputmanger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏输入法
			}
			break;

		default:
			break;
		}
	}

	public OnDayChangeListener mStartTime = new OnDayChangeListener() {

		@Override
		public void onDayChagne(String outDay) {
			text_birthdate.setText(outDay);
		}
	};

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
		menu.findItem(R.id.menu_save).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != data) {
			switch (requestCode) {
			case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT:// 地区
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void setChooseText(ArrayList<Metadata> data) {
		// 地区
		if (metadataArea != null) {
			metadataArea.clear();
		}
		metadataArea = data;
		area_result = ChooseDataUtil.getMetadataName(metadataArea);
		// 去除直辖市名字重叠的问题
		native_place.setText(getAreaStr(area_result));

	}

	/**
	 * 获取地区对象
	 * 
	 * @param area_result
	 * @return
	 */
	public String getAreaStr(Area area_result) {
		String area = "";
		if (area_result != null) {
			String province = TextUtils.isEmpty(area_result.province) ? "" : area_result.province;
			String city = TextUtils.isEmpty(area_result.city) ? "" : area_result.city;
			String county = TextUtils.isEmpty(area_result.county) ? "" : area_result.county;
			if (city.equalsIgnoreCase(province)) {
				area = province + county;
			} else {
				area = province + city + county;
			}

		}
		return area;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			// TODO 保存动作
			onSave();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onSave() {
		people_request = new PeopleRequest(); // 向后台传入的对象
		people_request.opType = "5";// 编辑个人资料
		
		birthdate = text_birthdate.getText().toString().trim();// 出生日期
		// 籍贯 +将这些值赋值给创建人脉的对象，避免值的丢失
		if (area_result != null) {
			if (!TextUtils.isEmpty(area_result.province))
				information.birthCountry = area_result.province;// 省
			if (!TextUtils.isEmpty(area_result.city))
				information.birthCity = area_result.city;// 市
			if (!TextUtils.isEmpty(area_result.county))
				information.birthCounty = area_result.county;// 县
		}

		goodAt = be_skilled_in.getText().toString().trim();// 擅长
		hobby = hobbies.getText().toString().trim();// 爱好
		information.setGoodAt(goodAt);// 擅长
		information.setHobby(hobby);// 爱好

		List<Basic> keyDate =information.getKeyDate();
		Basic basic = new Basic();
		basic.type = "1";// "type":"N-自定义；1-生日；2-纪念日",
		basic.name = "出生日期";
		basic.content = birthdate;
		keyDate.clear();
		keyDate.add(basic);

		personalInformationList.clear();
		personalInformationList.add(information);

		Person person = people_details.people;
		person.setPersonalInformationList(personalInformationList);

		people_request.people = person;
		doRequst();
	}

	private void doRequst() {
		for (int i = 0; people_details.tid != null && i < people_details.tid.size(); i++) {
			PersonTagRelation personTagRelation = people_details.tid.get(i);
			tid.add(personTagRelation.tagId);
		}
		people_request.tid = tid;

		for (int i = 0; people_details.categoryList != null && i < people_details.categoryList.size(); i++) {
			Long categoryId = people_details.categoryList.get(i).id;
			categoryList.add(categoryId);
		}
		people_request.categoryList = categoryList;
		if (people_details.people.permIds != null) {
			permIds = people_details.people.permIds;
		}
		people_request.permIds = permIds;
		people_request.asso = people_details.asso;
		PeopleReqUtil.doRequestWebAPI(this, this, people_request, null, PeopleRequestType.PEOPLE_REQ_CREATE);
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_CREATE:
			if (null != object) {
				BaseResult base = (BaseResult) object;
				if (base.success)
					setResult();
				else
					showToast("保存失败");
			} else
				showToast("保存失败");
			break;

		default:
			break;
		}
	}

	private void setResult() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("people", people_request.people);
		// intent.putExtra("birthdate", birthdate);
		// intent.putExtra("birthCity", birthCity);
		// intent.putExtra("goodAt", goodAt);
		// intent.putExtra("hobby", hobby);
		// 设置返回数据
		this.setResult(RESULT_OK, intent);
		// 关闭Activity
		this.finish();
	}
}
