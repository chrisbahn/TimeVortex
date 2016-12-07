package christopherbahn.com.timevortex;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class TVStoryListFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "tvstory_list";

	Activity activity;
	ListView tvstoryListView;
	ArrayList<TVStory> TVStories;
	ArrayList<TVStory> allTVStories;

	TVStoryListAdapter TVStoryListAdapter;
	TVStoryDAO tvstoryDAO;

	private GetTVStoryTask task;
	OnListItemClickedListener mListItemClicked;
	private SearchTerm searchTerm;
	private TaskCompletionSource<DataSnapshot> dbSource = new TaskCompletionSource<>();
	private Task dbTask = dbSource.getTask();
	private Task<Void> allTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		tvstoryDAO = new TVStoryDAO(activity);


		// FIREBASE CODE BEGINS HERE
//		FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//		DatabaseReference TVStoryRef = mDatabase.getReference("TVStory");
//		TVStoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//			ArrayList<TVStory> tempallTVStories = new ArrayList<TVStory>();
//			@Override
//			public void onDataChange(DataSnapshot dataSnapshot) {
//                dbSource.setResult(dataSnapshot);
//				Log.d(TAG, "on data change");
//				for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//					TVStory TVStory = postSnapshot.getValue(TVStory.class);
////                    Toast.makeText(getBaseContext(), TVStory.getTitle(), Toast.LENGTH_SHORT).show();
//					tempallTVStories.add(TVStory);
//				}
//				allTVStories.addAll(tempallTVStories);
//				// Build the adapter
////                ListAdapter adapter = new TVStoryListAdapter(getBaseContext(), allTVStories);
////                // Configure the list view
////                ListView listView = (ListView) findViewById(R.id.firebaseListView);
////                listView.setAdapter(adapter);
//				System.out.println("FIREBASE TVSTORY LIST UPDATED");
////                Toast.makeText(getBaseContext(), "FIREBASE TVSTORY LIST UPDATED!", Toast.LENGTH_SHORT).show();
//				Toast.makeText(activity, "in TVStoryListFragment onDataChange, FIREBASE says allTVStories has this many elements: " + allTVStories.size(), Toast.LENGTH_LONG).show();
//			}
//			@Override
//			public void onCancelled(DatabaseError databaseError) {
//				System.out.println("The read failed: " + databaseError.getCode());
//                dbSource.setException(databaseError.toException());
//			}
//		});
		// FIREBASE CODE ENDS HERE

// TODO This is where you quit for the night. allTVStories is populated from here, and not in MainActivity. You need to wire it up so that it does not attempt to return the Listview until it knows all the data is there. Current attempt is modifed from here : https://firebase.googleblog.com/2016/10/become-a-firebase-taskmaster-part-4.html
//		allTask = Tasks.whenAll(dbTask);
//		allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
//			@Override
//			public void onSuccess(Void aVoid) {
//				DataSnapshot dataSnapshot = (DataSnapshot) dbTask.getResult();
//				for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//					TVStory TVStory = postSnapshot.getValue(TVStory.class);
////                    Toast.makeText(getBaseContext(), TVStory.getTitle(), Toast.LENGTH_SHORT).show();
//					allTVStories.add(TVStory);
//				}
////				setAllTVStories(allTVStories);
//				// do something with db data?
////                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//			}
//		});
//		allTask.addOnFailureListener(new OnFailureListener() {
//			@Override
//			public void onFailure(@NonNull Exception e) {
//				// apologize profusely to the user!
//			}
//		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		searchTerm = bundle.getParcelable("searchTerm");
		allTVStories = bundle.getParcelableArrayList("allTVStories");
//		searchTerm.setCameFromSearchResult(false);
			Toast.makeText(activity, "in TVStoryListFragment, FIREBASE says allTVStories has this many elements: " + allTVStories.size(), Toast.LENGTH_LONG).show();

		System.out.println("48: cameFromSearchResult???? " + searchTerm.cameFromSearchResult());
		View view = inflater.inflate(R.layout.fragment_tvstory_list, container, false);
		findViewsById(view);

		task = new GetTVStoryTask(activity);
//		task.execute((Void) null);
		task.execute(searchTerm);

		tvstoryListView.setOnItemClickListener(this);
		tvstoryListView.setOnItemLongClickListener(this);
//		 TVStory n = tvstoryDAO.getTVStory(1);
//		 Log.d("TVStory n", n.toString());
		return view;
	}

	private void findViewsById(View view) {
		tvstoryListView = (ListView) view.findViewById(R.id.list_tvstory);
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
			System.out.println("82: cameFromSearchResult???? " + searchTerm.cameFromSearchResult());
			arguments.putParcelable("selectedTVStory", TVStory);
			arguments.putParcelable("searchTerm", searchTerm);
			MainActivity activity = (MainActivity) getActivity();
			activity.onListItemClicked(TVStory, searchTerm);
			mListItemClicked.onListItemClicked(TVStory, searchTerm); // todo do you actually need both these lines, or is only one of them doing the actual work?
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {
		TVStory TVStory = (TVStory) parent.getItemAtPosition(position);

		// todo In previous iteration, LongClick deleted a list item. I don't want the user to be able to do that, and have commented out that code, but I haven't decided if I still want to use LongClick for something else, so haven't deleted the whole thing. Possibility: This pops up a dialog where the user can add their star rating.
		// Use AsyncTask to delete from database
//		tvstoryDAO.delete(TVStory);
//		TVStoryListAdapter.remove(TVStory);
		return true;
	}

	// Container Activity must implement this interface
	public interface OnListItemClickedListener {
		void onListItemClicked(TVStory tvStory, SearchTerm searchTerm);
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




	public class GetTVStoryTask extends AsyncTask<SearchTerm, Void, ArrayList<TVStory>> {

		private final WeakReference<Activity> activityWeakRef;

		public GetTVStoryTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected ArrayList<TVStory> doInBackground(SearchTerm... params) {
			ArrayList<TVStory> TVStoryList = tvstoryDAO.getSelectedTVStories(searchTerm, allTVStories); // TODO Once it is established that this works for full-list and filtered search results, you can delete TVSearchListFragment and its associated code.
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
						TVStoryListAdapter = new TVStoryListAdapter(activity, TVStoryList);
						tvstoryListView.setAdapter(TVStoryListAdapter);
					} else {
						Toast.makeText(activity, "No TVStory Records", Toast.LENGTH_LONG).show();
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
