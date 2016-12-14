package christopherbahn.com.timevortex;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

// TVStoryListFragment controls calling the entire allTVSTories list of episodes, wuch as when you click on "See All Episodes" button at the top of the page. TVStorySearchListFragment handles any search that returns only a subset of that list

public class TVStorySearchListFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "tvstory_search_list";

	Activity activity;
	ListView tvstorySearchListView;
	ArrayList<TVStory> TVStories;
	ArrayList<TVStory> allTVStories;
	ArrayList<UserTVStoryInfo> allUserTVStoryInfo;
	private SearchTerm searchTerm;
	TVStoryListAdapter TVStoryListAdapter;
	TVStoryDAO tvstoryDAO;
	onSearchListItemClickedListener mListItemClicked;

	private GetTVStoryTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		tvstoryDAO = new TVStoryDAO(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		searchTerm = bundle.getParcelable("searchTerm");
		allTVStories = bundle.getParcelableArrayList("allTVStories");
		allUserTVStoryInfo = bundle.getParcelableArrayList("allUserTVStoryInfo");
		searchTerm.setCameFromSearchResult(true);
		View view = inflater.inflate(R.layout.fragment_tvstory_list, container, false);
		findViewsById(view);

		task = new GetTVStoryTask(activity);
		task.execute(searchTerm);

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
			arguments.putParcelable("searchTerm", searchTerm);
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchListItemClicked(TVStory, searchTerm);
			mListItemClicked.onSearchListItemClicked(TVStory, searchTerm);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {
		TVStory TVStory = (TVStory) parent.getItemAtPosition(position);

		// todo In future iteration, this will pop up a dialog on an episode list so user can set UserTVStoryInfo for a chosen episode without leaving the list and going to FullPageFragment.
		// Use AsyncTask to delete from database
//		tvstoryDAO.delete(TVStory);
//		TVStoryListAdapter.remove(TVStory);
		return true;
	}

	// Container Activity must implement this interface
	public interface onSearchListItemClickedListener {
		void onSearchListItemClicked(TVStory tvStory, SearchTerm searchTerm);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mListItemClicked = (onSearchListItemClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement onSearchListItemClickedListener");
		}
	}

	public class GetTVStoryTask extends AsyncTask<SearchTerm, Void, ArrayList<TVStory>> {
		private final WeakReference<Activity> activityWeakRef;
		public GetTVStoryTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected ArrayList<TVStory> doInBackground(SearchTerm... params) {
			ArrayList<TVStory> TVStoryList = tvstoryDAO.getSelectedTVStories(searchTerm, allTVStories, allUserTVStoryInfo);
			return TVStoryList;
		}

		@Override
		protected void onPostExecute(ArrayList<TVStory> TVStoryList) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				Log.d("TVStorySearchList", TVStoryList.toString());
				TVStories = TVStoryList;
				if (TVStoryList != null) {
					if (TVStoryList.size() != 0) {
						TVStoryListAdapter = new TVStoryListAdapter(activity, TVStoryList);
						tvstorySearchListView.setAdapter(TVStoryListAdapter);
					} else {
//						Toast.makeText(activity, "No TVStory Records", Toast.LENGTH_LONG).show();
					}
				}

			}
		}
	}

	/*
	 * This method is invoked from MainActivity onFinishDialog() method. It is
	 * called from CustomTVStoryDialogFragment when a record is updated.
	 * This is used for communicating between fragments.
	 */
	public void updateView() {
		task = new GetTVStoryTask(activity);
		task.execute(searchTerm);
	}



}
