package christopherbahn.com.timevortex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// TODO COMPLETE CONVERSION TO TIMEVORTEX: This needs to become the main display for TVStory objects, all of which have ALREADY BEEN CREATED ---- NOTE: I am going to try converting this page into TVStoryFullPageFragment
public class TVStoryAddFragment extends Fragment implements OnClickListener {

	// UI references
	private TextView TVTitle;
	private EditText EdtxtEnterTitle;
	private TextView TVTextfield;
	private EditText EdtxtEnterTextfield;
	private TextView TVHashtags;
	private EditText EdtxtEnterHashtags;
	private TextView TVDatecreated;
	private TextView TVHasPhoto;
	private TextView TVId;

	private Button saveButton;
	private Button cancelButton;
	private Button resetButton;

    OnSaveButtonClickedListener mSaveClicked;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(			"yyyy-MM-dd", Locale.ENGLISH);

	DatePickerDialog datePickerDialog;
	Calendar dateCalendar;

	TVStory TVStory = null;
	private TVStoryDAO tvstoryDAO;
	private AddTVStoryTask task;

	public static final String ARG_ITEM_ID = "note_add_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvstoryDAO = new TVStoryDAO(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_fullpage_tvstory, container,
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

		saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
	}

	protected void resetAllFields() {
		EdtxtEnterTitle.setText("");
		EdtxtEnterTextfield.setText("");
		EdtxtEnterHashtags.setText("");

	}

	private void setTVStory() {
		TVStory = new TVStory();
//		TVStory.setId(randomUUID());
//		TVStory.setTitle(EdtxtEnterTitle.getText().toString());
//		TVStory.setTextField(EdtxtEnterTextfield.getText().toString());
//		TVStory.setHashtags(EdtxtEnterHashtags.getText().toString());
//			Date date = new Date();
//        TVStory.setDateCreated(date);
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.add_note);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (dateCalendar != null)
			outState.putLong("dateCalendar", dateCalendar.getTime().getTime());
	}

	private void findViewsById(View rootView) {

		EdtxtEnterTitle = (EditText) rootView.findViewById(R.id.mynotes_edittext);
		EdtxtEnterTextfield = (EditText) rootView.findViewById(R.id.note_textfield);
//		EdtxtEnterHashtags = (EditText) rootView.findViewById(R.id.note_hashtag_field);

//		saveButton = (Button) rootView.findViewById(R.id.save_note);
//        cancelButton = (Button) rootView.findViewById(R.id.button_cancel);
		resetButton = (Button) rootView.findViewById(R.id.button_reset);
	}

	@Override
	public void onClick(View view) {
		if (view == cancelButton) {
            Toast.makeText(getActivity(), "Returning to Main List.", Toast.LENGTH_SHORT).show();
            MainActivity activity = (MainActivity) getActivity();
            activity.onSaveButtonClicked();
            mSaveClicked.onSaveButtonClicked();
		} else if (view == saveButton) {
			setTVStory();
			task = new AddTVStoryTask(getActivity());
			task.execute((Void) null);
            MainActivity activity = (MainActivity) getActivity();
            activity.onSaveButtonClicked();
            mSaveClicked.onSaveButtonClicked();
        } else if (view == resetButton) {
			resetAllFields();
		}
	}


    // Container Activity must implement this interface
    public interface OnSaveButtonClickedListener {
        void onSaveButtonClicked();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mSaveClicked = (OnSaveButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onSaveButtonClickedListener");
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
