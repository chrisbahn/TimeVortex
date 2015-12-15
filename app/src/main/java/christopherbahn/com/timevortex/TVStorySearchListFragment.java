package christopherbahn.com.timevortex;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

// TODO COMPLETE CONVERSION TO TIMEVORTEX - some functions should mirror the changes in TVSToryListFragment, and the searching methodology may need to be altered

public class TVStorySearchListFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "tvstory_search_list";

	Activity activity;
	ListView tvstorySearchListView;
	ArrayList<TVStory> TVStories;
	String searchField;
	String searchParameter;
	TVStoryListAdapter TVStoryListAdapter;
	TVStoryDAO tvstoryDAO;
	OnListItemClickedListener mListItemClicked;

	private GetTVStoryTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		tvstoryDAO = new TVStoryDAO(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		searchField = getArguments().getString("searchField");
		searchParameter = getArguments().getString("searchParameter");
		if (searchParameter == null) {
			searchParameter = "nothing";
		}
		View view = inflater.inflate(R.layout.fragment_tvstory_list, container, false);
		findViewsById(view);

		task = new GetTVStoryTask(activity);
		task.execute((String) searchParameter);

		tvstorySearchListView.setOnItemClickListener(this);
		tvstorySearchListView.setOnItemLongClickListener(this);
//		 TVStory n = tvstoryDAO.getTVStory(1);
//		 Log.d("TVStory n", n.toString());
		return view;
	}

	private void findViewsById(View view) {
		tvstorySearchListView = (ListView) view.findViewById(R.id.list_tvstory);
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.app_name);
//		getActivity().getSupportActionBar().setTitle(R.string.app_name);
		((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> list, View arg1, int position, long arg3) {
		TVStory TVStory = (TVStory) list.getItemAtPosition(position);

		if (TVStory != null) {
			Bundle arguments = new Bundle();
			arguments.putParcelable("selectedTVStory", TVStory);
			MainActivity activity = (MainActivity) getActivity();
			activity.onListItemClicked(TVStory);
			mListItemClicked.onListItemClicked(TVStory);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {
		TVStory TVStory = (TVStory) parent.getItemAtPosition(position);

		// todo In previous iteration, LongClick deleted a list item. I don't want the user to be able to do that, and have commented out that code, but I haven't decided if I still want to use LongClick for something else, so haven't deleted the whole thing.
		// Use AsyncTask to delete from database
//		tvstoryDAO.delete(TVStory);
//		TVStoryListAdapter.remove(TVStory);
		return true;
	}

	// Container Activity must implement this interface
	public interface OnListItemClickedListener {
		void onListItemClicked(TVStory tvStory);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mListItemClicked = (OnListItemClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnListItemClickedListener");
		}
	}

	public class GetTVStoryTask extends AsyncTask<String, Void, ArrayList<TVStory>> {

		private final WeakReference<Activity> activityWeakRef;

		public GetTVStoryTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected ArrayList<TVStory> doInBackground(String... params) {
			ArrayList<TVStory> TVStoryList = tvstoryDAO.getSelectedTVStories(searchField, searchParameter);
			return TVStoryList;
		}

		@Override
		protected void onPostExecute(ArrayList<TVStory> TVStoryList) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				Log.d("TVStories", TVStoryList.toString());
				TVStories = TVStoryList;
				if (TVStoryList != null) {
					if (TVStoryList.size() != 0) {
						TVStoryListAdapter = new TVStoryListAdapter(activity,
								TVStoryList);
						tvstorySearchListView.setAdapter(TVStoryListAdapter);
					} else {
						Toast.makeText(activity, "No TVStory Records", Toast.LENGTH_LONG).show();
						// todo it might be nice to have something else show up here, so that you don't get a blank screen
					}
				}

			}
		}
	}

	/*
	 * This method is invoked from MainActivity onFinishDialog() method. It is
	 * called from CustomTVStoryDialogFragment when an employee record is updated.
	 * This is used for communicating between fragments.
	 */
	public void updateView() {
		task = new GetTVStoryTask(activity);
		task.execute((String) searchParameter);
	}
}
