package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.ArticleAdapter;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.model.ArticleModel;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArticleListActivity extends Activity implements VKCUrlConstants {
	private Activity mActivity;
	private String search_key;
	List<ArticleModel> listArticle;
	ListView listViewArticle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_article);
		mActivity = this;
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			search_key = extras.getString("key");

		}

		initialiseUI();

		listViewArticle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AppController.articleNumber = listArticle.get(position)
						.getArticle_no();
				finish();
			}
		});
	}

	private void initialiseUI() {
		// TODO Auto-generated method stub
		listArticle = new ArrayList<ArticleModel>();
		getActionBar().setLogo(R.drawable.back);
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("");
		actionBar.setTitle("");
		actionBar.show();
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(true);
		listViewArticle = (ListView) findViewById(R.id.listViewArticle);

		getArticleApi();
	}

	private void getArticleApi() {
		VKCInternetManager manager = null;
		listArticle.clear();
		// Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
		String name[] = { "article_no" };
		String value[] = { AppPrefenceManager.getUserId(this) };
		manager = new VKCInternetManager(URL_ARTICLE_SEARCH_PRODUCT);
		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				// parseJSON(successResponse);
				Log.v("LOG", "06012015 " + successResponse);
				parseMyDealerJSON(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub
				Log.v("LOG", "04122014FAIL " + failureResponse);
				// mIsError = true;

			}
		});

	}

	private void parseMyDealerJSON(String successResponse) {
		// TODO Auto-generated method stub

		try {
			// ArrayList<DealersShopModel> dealersShopModels = new
			// ArrayList<DealersShopModel>();
			JSONObject respObj = new JSONObject(successResponse);
			JSONObject response = respObj.getJSONObject("response");
			String status = response.getString("status");
			if (status.equals("Success")) {
				JSONArray respArray = response.getJSONArray("orderdetails");
				for (int i = 0; i < respArray.length(); i++) {
					ArticleModel model = new ArticleModel();
					JSONObject obj = respArray.getJSONObject(i);
					model.setId(obj.getString("id"));
					model.setArticle_no(obj.getString("article_no"));
					listArticle.add(model);
				}
				ArticleAdapter adapter = new ArticleAdapter(mActivity,
						listArticle);
				listViewArticle.setAdapter(adapter);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// title/icon
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(item));
	}

}
