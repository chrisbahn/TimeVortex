package christopherbahn.com.timevortex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class AboutDoctorWhoFragment extends Fragment implements OnClickListener {

	// UI references
	TextView ADWHistory; // txt_aboutdoctorwho_history;
	TextView ADWWhereToStart; // txt_aboutdoctorwho_beginnersrecommendations;
	TextView ADWOtherReading; // textView_aboutdoctorwho_otherreading;
	Button buttonBestOfBahn;
	Button buttonBestOfBBCAmerica;
	Button buttonBestOfDWM2009;
	Button buttonBestOfDWM2014;
	Button buttonBestOfAVCTVC10;
	Button buttonBestOfIo9;
	Button buttonBestOfLMMyles;
	OnSearchButtonClickedListener mSearchClicked;
	SearchTerm searchTerm = new SearchTerm(); // this collects all the search parameters that tell TVStoryDAO which SQLite fields to search




	TVStory TVStory = null;
	private TVStoryDAO tvstoryDAO;

    public static final String ARG_ITEM_ID = "about_doctor_who_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        tvstoryDAO = new TVStoryDAO(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_aboutdoctorwho, container, false);

		findViewsById(rootView);
		setListeners();

		ADWHistory.setText("An icon of modern British culture and the longest-running science-fiction TV show in history, Doctor Who debuted on Nov. 23, 1963. It follows the adventures of a mysterious, eccentric time-traveling alien known as the Doctor, who battles evil and visits strange planets in his timeship, the TARDIS.");
		ADWWhereToStart.setText("A collection of best-of lists by critics, prominent fans, and others. This section could be expanded to include recommendations from each era for first episodes for new viewers.");
		ADWOtherReading.setText("This section will have recommendations for other places to read or watch about the show. Wikipedia, fan sites, books, etc.");


		return rootView;
	}

	private void setListeners() {
		buttonBestOfBahn.setOnClickListener(this);
		buttonBestOfBBCAmerica.setOnClickListener(this);
		buttonBestOfDWM2009.setOnClickListener(this);
		buttonBestOfDWM2014.setOnClickListener(this);
		buttonBestOfAVCTVC10.setOnClickListener(this);
		buttonBestOfIo9.setOnClickListener(this);
		buttonBestOfLMMyles.setOnClickListener(this);

    }

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.aboutdoctorwho_title);
//		getActivity().getActionBar().setTitle(R.string.add_emp);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.aboutdoctorwho_title);
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	}

	private void findViewsById(View rootView) {

		ADWHistory = (TextView) rootView.findViewById(R.id.txt_aboutdoctorwho_history);
		ADWWhereToStart = (TextView) rootView.findViewById(R.id.txt_aboutdoctorwho_beginnersrecommendations);
		ADWOtherReading = (TextView) rootView.findViewById(R.id.textView_aboutdoctorwho_otherreading);


		buttonBestOfBahn = (Button) rootView.findViewById(R.id.buttonBestOfBahn);
		buttonBestOfBBCAmerica = (Button) rootView.findViewById(R.id.buttonBestOfBBCAmerica);
		buttonBestOfDWM2009 = (Button) rootView.findViewById(R.id.buttonBestOfDWM2009);
		buttonBestOfDWM2014 = (Button) rootView.findViewById(R.id.buttonBestOfDWM2014);
		buttonBestOfAVCTVC10 = (Button) rootView.findViewById(R.id.buttonBestOfAVCTVC10);
		buttonBestOfIo9 = (Button) rootView.findViewById(R.id.buttonBestOfIo9);
		buttonBestOfLMMyles = (Button) rootView.findViewById(R.id.buttonBestOfLMMyles);

	}

	@Override
	public void onClick(View view) {
		if (view == buttonBestOfBahn) {
			searchTerm.setBestOfLists("Bahn");
		}
		if (view == buttonBestOfBBCAmerica) {
			searchTerm.setBestOfLists("BBCAmerica");
		}
		if (view == buttonBestOfDWM2009) {
			searchTerm.setBestOfLists("DWM2009");
		}
		if (view == buttonBestOfDWM2014) {
			searchTerm.setBestOfLists("DWM2014");
		}
		if (view == buttonBestOfAVCTVC10) {
			searchTerm.setBestOfLists("AVCTVC10");
		}
		if (view == buttonBestOfIo9) {
			searchTerm.setBestOfLists("Io9");
		}
		if (view == buttonBestOfLMMyles) {
			searchTerm.setBestOfLists("LMMyles");
		}
		MainActivity activity = (MainActivity) getActivity();
		activity.onSearchButtonClicked(searchTerm);
		mSearchClicked.onSearchButtonClicked(searchTerm);
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


}
