package christopherbahn.com.timevortex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.test.MoreAsserts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

// TODO Add "reset all search fields" button
// TODO Add selectors to the ImageButtons so that they visually change when clicked: http://developer.android.com/reference/android/widget/ImageButton.html Too time-consuming to do it now.


public class TVStorySearchFragment extends Fragment implements OnClickListener {

	// UI references
	private EditText EdtxtSearchByTitle;

	private Button searchByTitleButton;
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

	private CheckBox checkBoxWantToSeeIt;
	private CheckBox checkBoxInMyCollection;
	private RadioGroup radioGroupSeenIt;
	private RadioButton radioButtonSeenItYes;
	private RadioButton radioButtonSeenItNo;
	private RadioButton radioButtonSeenItAll;
	private RadioGroup radioGroupRatings;
	private RadioButton radioButtonUnrated;
	private RadioButton radioButtonRated1;
	private RadioButton radioButtonRated2;
	private RadioButton radioButtonRated3;
	private RadioButton radioButtonRated4;
	private RadioButton radioButtonRated5;
;
	// this is not currently being used, but might be in a later iteration
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	DatePickerDialog datePickerDialog;
	Calendar dateCalendar;

	SearchTerm searchTerm = new SearchTerm(); // this collects all the search parameters that tell TVStoryDAO how to narrow down the returned list
	TVStory TVStory = null;
	private TVStoryDAO tvstoryDAO;
    OnSearchButtonClickedListener mSearchClicked;

    public static final String ARG_ITEM_ID = "tvstory_search_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        tvstoryDAO = new TVStoryDAO(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_page, container, false);

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
		checkBoxWantToSeeIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				searchTerm.setWantToSeeIt(isChecked);
			}
		});
		checkBoxInMyCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				searchTerm.setWantToSeeIt(isChecked);
			}
		});
		radioGroupSeenIt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (radioButtonSeenItYes.isChecked()) {
					searchTerm.setSeenIt("true");
				} else if (radioButtonSeenItNo.isChecked()) {
					searchTerm.setSeenIt("false");
				} else if (radioButtonSeenItAll.isChecked()) {
					searchTerm.setSeenIt("everything");
				}
				Log.d("radioGroupSeenIt: ", searchTerm.getSeenIt());
			}
		});

		radioGroupRatings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (radioButtonUnrated.isChecked()) {
					searchTerm.setUserStarRatingNumber(0);
				} else if (radioButtonRated1.isChecked()) {
					searchTerm.setUserStarRatingNumber(1);
				} else if (radioButtonRated2.isChecked()) {
					searchTerm.setUserStarRatingNumber(2);
				} else if (radioButtonRated3.isChecked()) {
					searchTerm.setUserStarRatingNumber(3);
				} else if (radioButtonRated4.isChecked()) {
					searchTerm.setUserStarRatingNumber(4);
				} else if (radioButtonRated5.isChecked()) {
					searchTerm.setUserStarRatingNumber(5);
				}
			}
		});
    }

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.search_main_label);
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

		checkBoxWantToSeeIt = (CheckBox) rootView.findViewById(R.id.checkBoxWantToSeeIt);
		checkBoxInMyCollection = (CheckBox) rootView.findViewById(R.id.checkBoxInMyCollection);
		radioGroupSeenIt = (RadioGroup) rootView.findViewById(R.id.radioGroupSeenIt);
		radioButtonSeenItYes = (RadioButton) rootView.findViewById(R.id.radioButtonSeenItYes);
		radioButtonSeenItNo = (RadioButton) rootView.findViewById(R.id.radioButtonSeenItNo);
		radioButtonSeenItAll = (RadioButton) rootView.findViewById(R.id.radioButtonSeenItAll);
		radioGroupRatings = (RadioGroup) rootView.findViewById(R.id.RadioGroupRatings);
		radioButtonUnrated = (RadioButton) rootView.findViewById(R.id.RadioButtonUnrated);
		radioButtonRated1 = (RadioButton) rootView.findViewById(R.id.RadioButtonRated1);
		radioButtonRated2 = (RadioButton) rootView.findViewById(R.id.RadioButtonRated2);
		radioButtonRated3 = (RadioButton) rootView.findViewById(R.id.RadioButtonRated3);
		radioButtonRated4 = (RadioButton) rootView.findViewById(R.id.RadioButtonRated4);
		radioButtonRated5 = (RadioButton) rootView.findViewById(R.id.RadioButtonRated5);

	}

	@Override
	public void onClick(View view) {
		if (view == searchByTitleButton) {
			searchTerm.setTitle(EdtxtSearchByTitle.getText().toString());
			// The search will ONLY fire when this button is pressed. Everything else should toggle on/off.
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchTerm);
			mSearchClicked.onSearchButtonClicked(searchTerm);
		}



		// todo put the handling of ImageButtons in a method? it's very repetitive
		if (view == Doctor01ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=1) {
				searchTerm.setDoctor(1);
				Doctor01ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor02ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=2) {
				searchTerm.setDoctor(2);
				Doctor02ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor03ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=3) {
				searchTerm.setDoctor(3);
				Doctor03ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor04ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=4) {
				searchTerm.setDoctor(4);
				Doctor04ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor05ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=5) {
				searchTerm.setDoctor(5);
				Doctor05ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor06ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=6) {
				searchTerm.setDoctor(6);
				Doctor06ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor07ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=7) {
				searchTerm.setDoctor(7);
				Doctor07ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor08ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=8) {
				searchTerm.setDoctor(8);
				Doctor08ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor09ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=9) {
				searchTerm.setDoctor(9);
				Doctor09ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor10ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=10) {
				searchTerm.setDoctor(10);
				Doctor10ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor11ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=11) {
				searchTerm.setDoctor(11);
				Doctor11ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		} if (view == Doctor12ImageButton) {
			clearDoctorImageButtons();
			if (searchTerm.getDoctor()!=12) {
				searchTerm.setDoctor(12);
				Doctor12ImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: Doctor No. " + searchTerm.getDoctor(), Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setDoctor(0);
			}
		}
		if (view == DaleksImageButton) {
			clearOtherCastImageButtons();
			if (searchTerm.getOtherCast()!="Daleks") {
				searchTerm.setOtherCast("Daleks");
				DaleksImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: The Daleks", Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setOtherCast("");
			}
		} if (view == CybermenImageButton) {
			clearOtherCastImageButtons();
			if (searchTerm.getOtherCast()!="Cybermen") {
				searchTerm.setOtherCast("Cybermen");
				CybermenImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: The Cybermen", Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setOtherCast("");
			}
		} if (view == IceWarriorsImageButton) {
			clearOtherCastImageButtons();
			if (searchTerm.getOtherCast()!="IceWarriors") {
				searchTerm.setOtherCast("IceWarriors");
				IceWarriorsImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: The Ice Warriors", Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setOtherCast("");
			}
		} if (view == SontaransImageButton) {
			clearOtherCastImageButtons();
			if (searchTerm.getOtherCast()!="Sontarans") {
				searchTerm.setOtherCast("Sontarans");
				SontaransImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: The Sontarans", Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setOtherCast("");
			}
		} if (view == MasterImageButton) {
			clearOtherCastImageButtons();
			if (searchTerm.getOtherCast()!="Master") {
				searchTerm.setOtherCast("Master");
				MasterImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: The Master", Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setOtherCast("");
			}
		} if (view == WeepingAngelsImageButton) {
			clearOtherCastImageButtons();
			if (searchTerm.getOtherCast()!="WeepingAngels") {
				searchTerm.setOtherCast("WeepingAngels");
				WeepingAngelsImageButton.setBackgroundColor(Color.YELLOW);
				Toast.makeText(getActivity(), "You selected: The Weeping Angels", Toast.LENGTH_LONG).show();
			} else {
				searchTerm.setOtherCast("");
			}
		}




	}

    // Container Activity must implement this interface
    public interface OnSearchButtonClickedListener {
        void onSearchButtonClicked(SearchTerm searchTerm);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mSearchClicked = (OnSearchButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSearchButtonClickedListener");
        }
    }

	public void clearDoctorImageButtons() {
		Doctor01ImageButton.setBackgroundColor(Color.rgb(10, 23, 72));
		Doctor02ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor03ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor04ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor05ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor06ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor07ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor08ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor09ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor10ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor11ImageButton.setBackgroundColor(Color.rgb(10,23,72));
		Doctor12ImageButton.setBackgroundColor(Color.rgb(10,23,72));
	}
	public void clearOtherCastImageButtons() {
		DaleksImageButton.setBackgroundColor(Color.rgb(10,23,72));
		CybermenImageButton.setBackgroundColor(Color.rgb(10,23,72));
		IceWarriorsImageButton.setBackgroundColor(Color.rgb(10,23,72));
		SontaransImageButton.setBackgroundColor(Color.rgb(10,23,72));
		MasterImageButton.setBackgroundColor(Color.rgb(10,23,72));
		WeepingAngelsImageButton.setBackgroundColor(Color.rgb(10, 23, 72));
	}

}
