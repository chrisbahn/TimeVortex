package christopherbahn.com.timevortex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.test.MoreAsserts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// TODO COMPLETE CONVERSION TO TIMEVORTEX
// todo implement text search by title
// todo implement search by character for 12 Doctors and 6 villains. Wire up everything on this page, then upload new version of textfile with markers for search to catch.

public class TVStorySearchFragment extends Fragment implements OnClickListener {

	// UI references
	private TextView TVTextfield;
	private EditText EdtxtSearchByTitle;
	private TextView TVHashtags;
	private TextView TVHashtagsList;

	private Button searchByTitleButton;
	// todo There is a much better way to structure the various ImageButton searches so they don't repeat the same bits of code over and over, but this is a temporary solution so I can test whether the SQLite searches themselves are working properly.
	private ImageButton Doctor01ImageButton;
	private ImageButton Doctor02ImageButton;
	private ImageButton Doctor03ImageButton;
	private ImageButton Doctor04ImageButton;
	private ImageButton Doctor05ImageButton;
	private ImageButton Doctor06ImageButton;
	private ImageButton Doctor07ImageButton;
	private ImageButton Doctor08ImageButton;
	private ImageButton Doctor09ImageButton;
	private ImageButton Doctor10ImageButton;
	private ImageButton Doctor11ImageButton;
	private ImageButton Doctor12ImageButton;
	private ImageButton DaleksImageButton;
	private ImageButton CybermenImageButton;
	private ImageButton IceWarriorsImageButton;
	private ImageButton SontaransImageButton;
	private ImageButton MasterImageButton;
	private ImageButton WeepingAngelsImageButton;
	private Button resetButton;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	DatePickerDialog datePickerDialog;
	Calendar dateCalendar;

	TVStory TVStory = null;
	private TVStoryDAO tvstoryDAO;
//    private String allHashtagsString;
    OnSearchButtonClickedListener mSearchClicked;

    public static final String ARG_ITEM_ID = "tvstory_search_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        tvstoryDAO = new TVStoryDAO(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_page, container,
				false);

		findViewsById(rootView);
		setListeners();

		//For orientation change. 
		if (savedInstanceState != null) {
			dateCalendar = Calendar.getInstance();
			if (savedInstanceState.getLong("dateCalendar") != 0)
				dateCalendar.setTime(new Date(savedInstanceState.getLong("dateCalendar")));
		}

		return rootView;
	}

	private void setListeners() {
//		empDobEtxt.setOnClickListener(this);
//		Calendar newCalendar = Calendar.getInstance();
//		datePickerDialog = new DatePickerDialog(getActivity(),
//				new OnDateSetListener() {
//
//					public void onDateSet(DatePicker view, int year,
//							int monthOfYear, int dayOfMonth) {
//						dateCalendar = Calendar.getInstance();
//						dateCalendar.set(year, monthOfYear, dayOfMonth);
//						empDobEtxt.setText(formatter.format(dateCalendar
//								.getTime()));
//					}
//
//				}, newCalendar.get(Calendar.YEAR),
//				newCalendar.get(Calendar.MONTH),
//				newCalendar.get(Calendar.DAY_OF_MONTH));

		searchByTitleButton.setOnClickListener(this);
		Doctor01ImageButton.setOnClickListener(this);
		Doctor02ImageButton.setOnClickListener(this);
		Doctor03ImageButton.setOnClickListener(this);
		Doctor04ImageButton.setOnClickListener(this);
		Doctor05ImageButton.setOnClickListener(this);
		Doctor06ImageButton.setOnClickListener(this);
		Doctor07ImageButton.setOnClickListener(this);
		Doctor08ImageButton.setOnClickListener(this);
		Doctor09ImageButton.setOnClickListener(this);
		Doctor10ImageButton.setOnClickListener(this);
		Doctor11ImageButton.setOnClickListener(this);
		Doctor12ImageButton.setOnClickListener(this);
		DaleksImageButton.setOnClickListener(this);
		CybermenImageButton.setOnClickListener(this);
		IceWarriorsImageButton.setOnClickListener(this);
		SontaransImageButton.setOnClickListener(this);
		MasterImageButton.setOnClickListener(this);
		WeepingAngelsImageButton.setOnClickListener(this);
//		searchHashtagsButton.setOnClickListener(this);
//		resetButton.setOnClickListener(this);
    }

    protected void resetAllFields() {
//		EdtxtSearchTextfield.setText("");
//		EdtxtSearchHashtags.setText("");

	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.search_notes);
//		getActivity().getActionBar().setTitle(R.string.add_emp);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (dateCalendar != null)
			outState.putLong("dateCalendar", dateCalendar.getTime().getTime());
	}

	private void findViewsById(View rootView) {

		EdtxtSearchByTitle = (EditText) rootView.findViewById(R.id.edittext_search_by_title);
		searchByTitleButton = (Button) rootView.findViewById(R.id.search_by_title_button);

		Doctor01ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor01);
		Doctor02ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor02);
		Doctor03ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor03);
		Doctor04ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor04);
		Doctor05ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor05);
		Doctor06ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor06);
		Doctor07ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor07);
		Doctor08ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor08);
		Doctor09ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor09);
		Doctor10ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor10);
		Doctor11ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor11);
		Doctor12ImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDoctor12);
		DaleksImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonDaleks);
		CybermenImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonCybermen);
		IceWarriorsImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonIceWarriors);
		SontaransImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonSontarans);
		MasterImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonMaster);
		WeepingAngelsImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonWeepingAngels);




//		searchTextfieldsButton = (Button) rootView.findViewById(R.id.search_textfields);
//		searchHashtagsButton = (Button) rootView.findViewById(R.id.search_hashtags);
//		resetButton = (Button) rootView.findViewById(R.id.button_reset);

//		TVHashtagsList = (TextView) rootView.findViewById(R.id.textview_hashtags_list);
//        TVHashtagsList.setText("Hashtags in use: " + allHashtagsString());
//        TVHashtagsList.setText(tvstoryDAO.getAllHashtags().get(1));

	}

	public String allHashtagsString() {
        ArrayList<String> allOfThem = tvstoryDAO.getAllHashtags();
        String allHashtagsString = new String();
        for(int i=0; i < allOfThem.size(); i++) {
            allHashtagsString = allHashtagsString  + " " + allOfThem.get(i);
        }
        return allHashtagsString;
    }

	@Override
	public void onClick(View view) {
		if (view == searchByTitleButton) {
			String searchField = "searchByTitle"; // the "searchField" variable tells TVStoryDAO which SQLite fields to search
			String searchByTitle = EdtxtSearchByTitle.getText().toString();
            MainActivity activity = (MainActivity) getActivity();
            activity.onSearchButtonClicked(searchField, searchByTitle);
            mSearchClicked.onSearchButtonClicked(searchField, searchByTitle);

		} else if (view == Doctor01ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "1";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor02ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "2";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor03ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "3";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor04ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "4";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor05ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "5";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor06ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "6";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor07ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "7";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor08ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "8";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor09ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "9";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor10ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "10";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor11ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "11";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == Doctor12ImageButton) {
			String searchField = "searchByDoctor";
			String searchByDoctor = "12";
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchField, searchByDoctor);
			mSearchClicked.onSearchButtonClicked(searchField, searchByDoctor);
		} else if (view == resetButton) {
			resetAllFields();
		}
	}

    // Container Activity must implement this interface
    public interface OnSearchButtonClickedListener {
        void onSearchButtonClicked(String searchField, String searchParameter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mSearchClicked = (OnSearchButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSearchButtonClickedListener");
        }
    }



    public class AddTVStoryTask extends AsyncTask<Void, Void, Long> {

		private final WeakReference<Activity> activityWeakRef;

		public AddTVStoryTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected Long doInBackground(Void... arg0) {
			long result = tvstoryDAO.save(TVStory);
			return result;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				if (result != -1)
					Toast.makeText(activityWeakRef.get(), "TVStory Saved", Toast.LENGTH_LONG).show();
			}
		}
	}
}
