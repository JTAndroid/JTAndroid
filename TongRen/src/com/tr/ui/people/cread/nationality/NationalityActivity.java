package com.tr.ui.people.cread.nationality;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.people.cread.NewConnectionsActivity;
import com.tr.ui.people.cread.PersonalInformationActivity;
import com.tr.ui.people.cread.nationality.liucanwen.citylist.adapter.CityAdapter;
import com.tr.ui.people.cread.nationality.liucanwen.citylist.data.CityData;
import com.tr.ui.people.cread.nationality.liucanwen.citylist.model.CityItem;
import com.tr.ui.people.cread.nationality.liucanwen.citylist.widget.ContactItemInterface;
import com.tr.ui.people.cread.nationality.liucanwen.citylist.widget.ContactListViewImpl;

/**
 * 国籍
 * @author Wxh07151732
 *
 */
public class NationalityActivity extends Activity implements TextWatcher
{
	private Context context_ = NationalityActivity.this;

	private ContactListViewImpl listview;

//	private EditText searchBox;
	private String searchString;

	private Object searchLock = new Object();
	boolean inSearchMode = false;

	private final static String TAG = "MainActivity2";

	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;

	private RelativeLayout infoRowContainer_china;

	private TextView cityName_china;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_nationalitylist);

		filterList = new ArrayList<ContactItemInterface>();
		contactList = CityData.getSampleContactList();

		CityAdapter adapter = new CityAdapter(this,
				R.layout.people_city_item, contactList);

		listview = (ContactListViewImpl) this.findViewById(R.id.listview);
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);
		infoRowContainer_china = (RelativeLayout) findViewById(R.id.infoRowContainer_china);
		cityName_china = (TextView) findViewById(R.id.cityName_china);
		infoRowContainer_china.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CharSequence tet = cityName_china.getText();
				Toast.makeText(context_,
						tet, //����Ĺ��
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(NationalityActivity.this, PersonalInformationActivity.class);
				intent.putExtra("Nationality", tet);
				setResult(1, intent);
				finish();
			}
		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id)
			{
				List<ContactItemInterface> searchList = inSearchMode ? filterList
						: contactList;

				Toast.makeText(context_,
						searchList.get(position).getDisplayInfo(), //����Ĺ��
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(NationalityActivity.this, PersonalInformationActivity.class);
				intent.putExtra("Nationality", searchList.get(position).getDisplayInfo());
				setResult(1, intent);
				finish();
			}
		});

//		searchBox = (EditText) findViewById(R.id.input_search_query);
//		searchBox.addTextChangedListener(this);
	}

	@Override
	public void afterTextChanged(Editable s)
	{
//		searchString = searchBox.getText().toString().trim().toUpperCase();

		if (curSearchTask != null
				&& curSearchTask.getStatus() != AsyncTask.Status.FINISHED)
		{
			try
			{
				curSearchTask.cancel(true);
			} catch (Exception e)
			{
				Log.i(TAG, "Fail to cancel running search task");
			}

		}
		curSearchTask = new SearchListTask();
		curSearchTask.execute(searchString); 
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		// do nothing
	}

	private class SearchListTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			filterList.clear();

			String keyword = params[0];

			inSearchMode = (keyword.length() > 0);

			if (inSearchMode)
			{
				// get all the items matching this
				for (ContactItemInterface item : contactList)
				{
					CityItem contact = (CityItem) item;

					boolean isPinyin = contact.getFullName().toUpperCase()
							.indexOf(keyword) > -1;
					boolean isChinese = contact.getNickName().indexOf(keyword) > -1;

					if (isPinyin || isChinese)
					{
						filterList.add(item);
					}

				}

			}
			return null;
		}

		protected void onPostExecute(String result)
		{

			synchronized (searchLock)
			{

				if (inSearchMode)
				{

					CityAdapter adapter = new CityAdapter(context_,
							R.layout.people_city_item, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else
				{
					CityAdapter adapter = new CityAdapter(context_,
							R.layout.people_city_item, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}

		}
	}

}
