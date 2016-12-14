package christopherbahn.com.timevortex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.associates.AssociatesAPI;
import com.amazon.device.associates.LinkService;
import com.amazon.device.associates.NotInitializedException;
import com.amazon.device.associates.OpenProductPageRequest;
import com.amazon.device.associates.OpenSearchPageRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TVStoryFullPageFragment extends Fragment implements OnClickListener {

	// UI references
	private LinearLayout submitLayout;
	private LinearLayout layoutAllBestOf;
	private LinearLayout layoutBestOfBBCAmerica;
	private LinearLayout layoutBestOfDWM2009;
	private LinearLayout layoutBestOfDWM2014;
	private LinearLayout layoutBestOfAVCTVC10;
	private LinearLayout layoutBestOfIo9;
	private LinearLayout layoutBestOfLMMyles;
	private LinearLayout layoutBestOfBahn;
	private TextView tvstoryTitle;
	private TextView tvstorySeasonInfo;
	private TextView tvstorySynopsis;
	private TextView tvstoryCastAndCrew;
	private CheckBox seenIt;
	private CheckBox wantToSeeIt;
	private CheckBox iOwnIt;
	private TextView TVuserStarRatingTitle;
	private RatingBar userStarRating;
	private float userStarRatingNumber; // app user's rating of the episode

	private TextView TVMyNotesTitle;
	private ImageView tvstoryImage;
	private EditText EdtxtMyNotes;


	private TextView textviewBestOfBBCAmerica;
	private TextView textviewBestOfDWM2009;
	private TextView textviewBestOfDWM2014;
	private TextView textviewBestOfAVCTVC10;
	private TextView textviewBestOfIo9;
	private TextView textviewBestOfLMMyles;
	private TextView textviewBestOfBahn;
	private ImageButton imageButtonBestOfBBCAmerica;
	private ImageButton imageButtonBestOfDWM2009;
	private ImageButton imageButtonBestOfDWM2014;
	private ImageButton imageButtonBestOfAVCTVC10;
	private ImageButton imageButtonBestOfIo9;
	private ImageButton imageButtonBestOfLMMyles;
	private ImageButton imageButtonBestOfBahn;

	private TextView textviewErrorItunes;
	private String amazonSearchTerm;
	private String amazonCategory;
	private ImageButton amazonImageButton;
	private ImageButton iTunesImageButton;
	private String asin = null;
	private static final String TAG = "TimeVortex";

	private String iTunesCollectionViewUrl;

	private Button saveButton;
	private Button returnToListButton;
	private Button resetButton;
	private ArrayList<UserTVStoryInfo> allUserTVStoryInfo;

    OnSaveButtonClickedListener mSaveClicked;

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	DatePickerDialog datePickerDialog;
	Calendar dateCalendar;

	private TVStory TVStory;
	public UserTVStoryInfo userTVStoryInfo;
	private SearchTerm searchTerm;
	private TVStoryDAO tvstoryDAO;
	private UpdateUserTVStoryInfoTask task;
	OnSearchButtonClickedListener mSearchClicked;
	int[] doctorImages = {R.drawable.doctor01, R.drawable.doctor02, R.drawable.doctor03, R.drawable.doctor04, R.drawable.doctor05, R.drawable.doctor06, R.drawable.doctor07, R.drawable.doctor08, R.drawable.doctor09, R.drawable.doctor10, R.drawable.doctor11, R.drawable.doctor12};
	int[] logoImages = {R.drawable.logodoctor01, R.drawable.logodoctor02, R.drawable.logodoctor03, R.drawable.logodoctor04, R.drawable.logodoctor05, R.drawable.logodoctor06, R.drawable.logodoctor07, R.drawable.logodoctor08, R.drawable.logodoctor09, R.drawable.logodoctor10, R.drawable.logodoctor11, R.drawable.logodoctor12};

	public static final String ARG_ITEM_ID = "tvstory_fullpage_fragment";

	public TVStoryFullPageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		searchTerm.setCameFromSearchResult(false); // automatically flips to true if the user came from anything but the all-episodes list, via SearchListFragment rather than ListFragment
		tvstoryDAO = new TVStoryDAO(getActivity());
		Bundle bundle = this.getArguments();
		TVStory = bundle.getParcelable("selectedTVStory");
		searchTerm = bundle.getParcelable("searchTerm");
		userTVStoryInfo = bundle.getParcelable("userTVStoryInfo");
		allUserTVStoryInfo = bundle.getParcelableArrayList("allUserTVStoryInfo");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_fullpage_tvstory, container, false);
		// Hook up with Amazon
		String APPLICATION_KEY = getKeyFromRawResource("APPLICATION_KEY");
		AssociatesAPI.initialize(new AssociatesAPI.Config(APPLICATION_KEY, getActivity()));
		// start asynchronous process to request iTunes data, some of which is used to populate this page, as well as a URL target if user clicks the iTunesImageButton.
		// send URL as argument
		String plussedTitle = TVStory.getTitle().replace(' ', '+');
		String iTunesTempUrl = "https://itunes.apple.com/search?term=doctor+who+" + plussedTitle + "&mediatype=tvshow";
		// This method gets the iTunes JSON data for the desired title.
		new RequestITunesSearchResultTask(getActivity()).execute(iTunesTempUrl);

		findViewsById(rootView);
//		submitLayout.setVisibility(View.GONE);
		resetButton.setVisibility(View.GONE);

		getRatings();

		textviewErrorItunes.setVisibility(View.GONE);

			// ASIN is an Amazon search term that calls up a specific product, in this case the DVD of the TVStory, if it exists.
			asin = TVStory.getASIN();

		amazonImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				amazonCategory = "MoviesAndTV"; // this category covers both DVDs and downloadable media on Amazon. Streaming is not searchable. Future iterations of this program may also want to search by categories like KindleStore, Books, etc. More info here https://developer.amazon.com/public/apis/earn/mobile-associates/docs/available-search-categories
				if (asin != null) {
					OpenProductPageRequest request = new OpenProductPageRequest(asin);
					try {
						LinkService linkService = AssociatesAPI.getLinkService();
						linkService.openRetailPage(request);
					} catch (NotInitializedException e) {
						Log.v(TAG, "NotInitializedException error");
					}
				} else {
					amazonSearchTerm = "Doctor Who " + TVStory.getTitle();
					// Categories available for the Amazon Mobile Associates API are found here https://developer.amazon.com/public/apis/earn/mobile-associates/docs/available-search-categories
					//This is from https://developer.amazon.com/public/apis/earn/mobile-associates/docs/direct-linking
					OpenSearchPageRequest request = new OpenSearchPageRequest(amazonCategory, amazonSearchTerm);
					try {
						LinkService linkService = AssociatesAPI.getLinkService();
						linkService.openRetailPage(request);
					} catch (NotInitializedException e) {
						Log.v(TAG, "NotInitializedException error");
					}
				}
			}
		});
		iTunesImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// intent to launch browser here
				Uri searchUri = Uri.parse(iTunesCollectionViewUrl);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, searchUri);
				startActivity(browserIntent);
			}
		});









		setValue();
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
		saveButton.setOnClickListener(this);
		returnToListButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
		seenIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				userTVStoryInfo.setIveSeenIt(isChecked);
			}
		});
		wantToSeeIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				userTVStoryInfo.setiWantToSeeIt(isChecked);
			}
		});
		iOwnIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				userTVStoryInfo.setiOwnIt(isChecked);
			}
		});
		addListenerOnRatingBar();
		imageButtonBestOfBBCAmerica.setOnClickListener(this);
		imageButtonBestOfDWM2009.setOnClickListener(this);
		imageButtonBestOfDWM2014.setOnClickListener(this);
		imageButtonBestOfAVCTVC10.setOnClickListener(this);
		imageButtonBestOfIo9.setOnClickListener(this);
		imageButtonBestOfLMMyles.setOnClickListener(this);
		imageButtonBestOfBahn.setOnClickListener(this);
	}

	@Override
	public void onResume() {
//		getActivity().setTitle(R.string.add_note);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (dateCalendar != null)
			outState.putLong("dateCalendar", dateCalendar.getTime().getTime());
	}

	private void findViewsById(View rootView) {

		tvstoryImage = (ImageView) rootView.findViewById(R.id.list_item_image);
		tvstoryTitle = (TextView) rootView.findViewById(R.id.txt_fullpagetvstory_title);
		tvstorySeasonInfo = (TextView) rootView.findViewById(R.id.txt_fullpagetvstory_seasoninfo);
		tvstorySynopsis = (TextView) rootView.findViewById(R.id.txt_fullpagetvstory_synopsis);
		tvstoryCastAndCrew = (TextView) rootView.findViewById(R.id.txt_fullpagetvstory_castandcrew);
		seenIt = (CheckBox) rootView.findViewById(R.id.checkBox_listitem_seenit);
		wantToSeeIt = (CheckBox) rootView.findViewById(R.id.checkBox_listitem_wanttoseeit);
		iOwnIt = (CheckBox) rootView.findViewById(R.id.checkBox_listitem_iOwnIt);
		TVuserStarRatingTitle = (TextView) rootView.findViewById(R.id.textview_myrating);
		userStarRating = (RatingBar) rootView.findViewById(R.id.UserStarRatingBar);
		TVMyNotesTitle = (TextView) rootView.findViewById(R.id.textview_mynotes_title);
		EdtxtMyNotes = (EditText) rootView.findViewById(R.id.mynotes_edittext);

		imageButtonBestOfBBCAmerica = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogobbc);
		imageButtonBestOfDWM2009 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogodwm2009);
		imageButtonBestOfDWM2014 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogodwm2014);
		imageButtonBestOfAVCTVC10 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogoavclub);
		imageButtonBestOfIo9 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogoio9);
		imageButtonBestOfLMMyles = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogoLMMyles);
		imageButtonBestOfBahn = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogoBahn);
		textviewBestOfBBCAmerica = (TextView) rootView.findViewById(R.id.textviewBestOfBBCAmerica);
		textviewBestOfDWM2009 = (TextView) rootView.findViewById(R.id.textviewBestOfDWM2009);
		textviewBestOfDWM2014 = (TextView) rootView.findViewById(R.id.textviewBestOfDWM2014);
		textviewBestOfAVCTVC10 = (TextView) rootView.findViewById(R.id.textviewBestOfAVCTVC10);
		textviewBestOfIo9 = (TextView) rootView.findViewById(R.id.textviewBestOfIo9);
		textviewBestOfLMMyles = (TextView) rootView.findViewById(R.id.textviewBestOfLMMyles);
		textviewBestOfBahn = (TextView) rootView.findViewById(R.id.textviewBestOfBahn);
		layoutAllBestOf = (LinearLayout) rootView.findViewById(R.id.layoutAllBestOf);
		layoutBestOfBBCAmerica = (LinearLayout) rootView.findViewById(R.id.layoutBestOfBBCAmerica);
		layoutBestOfDWM2009 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfDWM2009);
		layoutBestOfDWM2014 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfDWM2014);
		layoutBestOfAVCTVC10 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfAVCTVC10);
		layoutBestOfIo9 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfIo9);
		layoutBestOfLMMyles = (LinearLayout) rootView.findViewById(R.id.layoutBestOfLMMyles);
		layoutBestOfBahn = (LinearLayout) rootView.findViewById(R.id.layoutBestOfBahn);

		submitLayout = (LinearLayout) rootView.findViewById(R.id.save_buttons_layout);
		textviewErrorItunes = (TextView) rootView.findViewById(R.id.textViewErrorITunes);
		amazonImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonAmazon);
		iTunesImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonITunes);

		saveButton = (Button) rootView.findViewById(R.id.button_save_user_changes);
        returnToListButton = (Button) rootView.findViewById(R.id.button_return_to_list);
		resetButton = (Button) rootView.findViewById(R.id.button_reset);
	}

	@Override
	public void onClick(View view) {
//		if (view == resetButton) {
//			resetAllFields();
//			}
		if (view == imageButtonBestOfBahn) {
			searchTerm.setBestOfLists("Bahn");
			searchTerm.setCameFromSearchResult(true);
		}
		else if (view == imageButtonBestOfBBCAmerica) {
			searchTerm.setBestOfLists("BBCAmerica");
			searchTerm.setCameFromSearchResult(true);
		}
		else if (view == imageButtonBestOfDWM2009) {
			searchTerm.setBestOfLists("DWM2009");
			searchTerm.setCameFromSearchResult(true);
		}
		else if (view == imageButtonBestOfDWM2014) {
			searchTerm.setBestOfLists("DWM2014");
			searchTerm.setCameFromSearchResult(true);
		}
		else if (view == imageButtonBestOfAVCTVC10) {
			searchTerm.setBestOfLists("AVCTVC10");
			searchTerm.setCameFromSearchResult(true);
		}
		else if (view == imageButtonBestOfIo9) {
			searchTerm.setBestOfLists("Io9");
			searchTerm.setCameFromSearchResult(true);
		}
		else if (view == imageButtonBestOfLMMyles) {
			searchTerm.setBestOfLists("LMMyles");
			searchTerm.setCameFromSearchResult(true);
		}
		else if (view == returnToListButton||view == saveButton) {
				if (view == saveButton) {
					// this saves whatever user-editable data was changed
					userTVStoryInfo.setUserReview(EdtxtMyNotes.getText().toString());
//					userTVStoryInfo.setUserStarRatingNumber(userStarRating.getRating());
					userTVStoryInfo.setIveSeenIt(seenIt.isChecked());
					userTVStoryInfo.setiWantToSeeIt(wantToSeeIt.isChecked());
					userTVStoryInfo.setiOwnIt(iOwnIt.isChecked());
					userTVStoryInfo.setUserAtoF(0);
					allUserTVStoryInfo.set(userTVStoryInfo.getStoryID()-1,userTVStoryInfo);

					task = new UpdateUserTVStoryInfoTask(getActivity());
					task.execute((Void) null);
					}
				}

		// goes back to list of all stories
		if (searchTerm.cameFromSearchResult()==false)
		{ // goes back to the consecutive chronological all-episodes list
			MainActivity activity = (MainActivity) getActivity();
			activity.onSaveButtonClicked();
			mSaveClicked.onSaveButtonClicked();
		} else if (searchTerm.cameFromSearchResult())
		{ // goes back to previously called search result OR to a nonconsecutive list such as a best-of list
			MainActivity activity = (MainActivity) getActivity();
			activity.onSearchButtonClicked(searchTerm);
			mSearchClicked.onSearchButtonClicked(searchTerm);
		}

	}


    // Container Activity must implement this interface
    public interface OnSaveButtonClickedListener {
        void onSaveButtonClicked();
    }
	// Container Activity must implement this interface
	public interface OnSearchButtonClickedListener {
		void onSearchButtonClicked(SearchTerm searchTerm);
	}



	private void setValue() {
		if (TVStory != null) {
			// this method sets the values seen on this page to match the TVStory being called
			tvstoryTitle.setText(TVStory.getStoryID()  + ": " + TVStory.getTitle()); // story title, and storyID
			tvstorySeasonInfo.setText(TVStory.getYearProduced()  + ". " + TVStory.getSeason() + " #" + TVStory.getSeasonStoryNumber() + " (" + TVStory.getEra()  + " era). " + TVStory.getEpisodes() + " episodes. " + (TVStory.getEpisodes()*TVStory.getEpisodeLength()) + "minutes."); // era, yearProduced, season, seasonStoryNumber, episode, episodeLength
			tvstorySynopsis.setText(TVStory.getSynopsis());
			// The following three lines plus the whichDoctor() method sets mention of Doctor in cast list. Will be implemented differently when DWCharacter class is implemented
 			tvstoryCastAndCrew.setText(TVStory.getDoctor() + ", " + TVStory.getOtherCast() + ", " + TVStory.getCrew());
			int whichDoctor = TVStory.getDoctor();
			whichDoctorIsIt(whichDoctor);

			Resources res = getContext().getResources();
			TypedArray tvstoryImages = res.obtainTypedArray(R.array.tvstoryImages);
			Drawable drawable = tvstoryImages.getDrawable(TVStory.getStoryID() - 1);
			tvstoryImage.setImageDrawable(drawable);
		}
		if (userTVStoryInfo != null) {
			// This is where UserTVStoryInfo is set for display
			seenIt.setChecked(userTVStoryInfo.haveISeenIt());
			wantToSeeIt.setChecked(userTVStoryInfo.doiWantToSeeIt());
			iOwnIt.setChecked(userTVStoryInfo.doiOwnIt());
//			userStarRating.setRating(userTVStoryInfo.getUserAtoF());
			EdtxtMyNotes.setText(userTVStoryInfo.getUserReview());
		}
	}

		@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented the callback interface. If not, it throws an exception
        try {
            mSaveClicked = (OnSaveButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSaveButtonClickedListener");
        }
		try {
			mSearchClicked = (OnSearchButtonClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnSearchButtonClickedListener");
		}
    }

    public class UpdateUserTVStoryInfoTask extends AsyncTask<Void, Void, Long> {

		private final WeakReference<Activity> activityWeakRef;

		public UpdateUserTVStoryInfoTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected Long doInBackground(Void... arg0) {
			long result = tvstoryDAO.update(userTVStoryInfo);
			return result;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				if (result != -1)
					Toast.makeText(activityWeakRef.get(), "userTVStoryInfo Saved", Toast.LENGTH_LONG).show();
			}
		}
	}

	public class RequestITunesSearchResultTask extends AsyncTask<String, String, String> {
		private final WeakReference<Activity> activityWeakRef;
		public RequestITunesSearchResultTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected String doInBackground(String...urls) {
			String responseString = null;
			try {
				URL url = new URL(urls[0]); // use the first argument supplied
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				InputStream responseStream = new BufferedInputStream(connection.getInputStream());
				InputStreamReader streamReader = new InputStreamReader(responseStream);
				StringBuffer buffer = new StringBuffer();
				int c;
				while ( (c = streamReader.read() ) != -1) {
					// cast c to a char and append to the buffer
					buffer.append( (char) c);
				}
				responseString = buffer.toString();
				Log.i("ITUNES", "String is " + responseString);
			} catch (Exception e) {
				Log.e(TAG, "error doinbackground", e);
				// todo Catch and handle exceptions separately - for parsing the URL, for the conection, for processing the response stream
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, "starting onpostexecute");
			if (result != null) {
				try {
					JSONObject object = new JSONObject(result);
					JSONArray resultsArray = object.getJSONArray("results");
					JSONObject iTunesTopResult = resultsArray.getJSONObject(0);
					String collectionViewUrl = iTunesTopResult.getString("collectionViewUrl");
					iTunesCollectionViewUrl = collectionViewUrl;
					String longDescription = iTunesTopResult.getString("longDescription");
					tvstorySynopsis.setText(longDescription + " (Synopsis taken from iTunes.)");
					String artworkUrl100 = iTunesTopResult.getString("artworkUrl100");
					textviewErrorItunes.setVisibility(View.VISIBLE);
					textviewErrorItunes.setText(collectionViewUrl);
				} catch (JSONException e) {
					Log.e(TAG, "parsing error, check schema?", e);
					textviewErrorItunes.setVisibility(View.VISIBLE);
					textviewErrorItunes.setText("Error fetching iTunes result");
				} catch (Exception e) {
					Log.e(TAG,"exception",e);
				}
			}
			else {
				textviewErrorItunes.setVisibility(View.VISIBLE);
				textviewErrorItunes.setText("Error fetching iTunes result");
				Log.e(TAG, "Result was null, check doInBackground for errors");
			}
		}
	}

	//gets the key from the raw file.
	private String getKeyFromRawResource(String requestedKey) {
		InputStream keyStream = null;
		if (requestedKey.equals("key")) {
			keyStream = this.getResources().openRawResource(R.raw.key);
		}
		else if (requestedKey.equals("APPLICATION_KEY")) {
			keyStream = this.getResources().openRawResource(R.raw.amazon_application_key);
		}


		BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
		try{
			String key = keyStreamReader.readLine();
			return key;
		}
		catch (IOException e){
			Log.e(TAG, "Error reading from raw resource file", e);
			return null;
		}
	}

	// used for Amazon Mobile Associates
	private class MAAWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try {
				LinkService linkService = AssociatesAPI.getLinkService();
				return linkService.overrideLinkInvocation(view, url);
			} catch (NotInitializedException e) {
				Log.v(TAG, "NotInitializedException error");
			}
			return false;
		}
	}


	private void whichDoctorIsIt(int whichDoctor){
		if (whichDoctor==1) {
			tvstoryCastAndCrew.setText("First Doctor (William Hartnell)");
		}
		if (whichDoctor==2) {
			tvstoryCastAndCrew.setText("Second Doctor (Patrick Troughton)");
		}
		if (whichDoctor==3) {
			tvstoryCastAndCrew.setText("Third Doctor (Jon Pertwee)");
		}
		if (whichDoctor==4) {
			tvstoryCastAndCrew.setText("Fourth Doctor (Tom Baker)");
		}
		if (whichDoctor==5) {
			tvstoryCastAndCrew.setText("Fifth Doctor (Peter Davison)");
		}
		if (whichDoctor==6) {
			tvstoryCastAndCrew.setText("Sixth Doctor (Colin Baker)");
		}
		if (whichDoctor==7) {
			tvstoryCastAndCrew.setText("Seventh Doctor (Sylvester McCoy)");
		}
		if (whichDoctor==8) {
			tvstoryCastAndCrew.setText("Eighth Doctor (Paul McGann)");
		}
		if (whichDoctor==9) {
			tvstoryCastAndCrew.setText("Ninth Doctor (Christopher Ecclestone)");
		}
		if (whichDoctor==10) {
			tvstoryCastAndCrew.setText("Tenth Doctor (David Tennant)");
		}
		if (whichDoctor==11) {
			tvstoryCastAndCrew.setText("Eleventh Doctor (Matt Smith)");
		}
		if (whichDoctor==12) {
			tvstoryCastAndCrew.setText("Twelfth Doctor (Peter Capaldi)");
		}
	}


	public void addListenerOnRatingBar() {
		//if rating value is changed, display the current rating value in the result (textview) automatically
		userStarRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				TVStory.setUserStarRatingNumber(rating);
			}
		});
	}

	protected void getRatings() {
		// somebodyLikesMe variable governs whether the ratings are visible: If nobody ranked this episode, no rankings show up!
		int somebodyLikesMe = 0;
		if (TVStory.getBestOfBahn()>0) {
			layoutBestOfBahn.setVisibility(View.VISIBLE);
			textviewBestOfBahn.setText("Time Vortex\nranking: #" + TVStory.getBestOfBahn());
			somebodyLikesMe++;
		} else {
			layoutBestOfBBCAmerica.setVisibility(View.GONE);
		}
		if (TVStory.getBestOfBBCAmerica()>0) {
			layoutBestOfBBCAmerica.setVisibility(View.VISIBLE);
			textviewBestOfBBCAmerica.setText("BBC Critics\nPoll 2013: #" + TVStory.getBestOfBBCAmerica());
			somebodyLikesMe++;
		} else {
			layoutBestOfBBCAmerica.setVisibility(View.GONE);
		}
		if (TVStory.getBestOfDWM2009()>0) {
			layoutBestOfDWM2009.setVisibility(View.VISIBLE);
			textviewBestOfDWM2009.setText("Doctor Who Magazine\n2009 readers poll: #" + TVStory.getBestOfDWM2009());
			somebodyLikesMe++;
		} else {
			layoutBestOfDWM2009.setVisibility(View.GONE);
		}
		if (TVStory.getBestOfDWM2014()>0) {
			layoutBestOfDWM2014.setVisibility(View.VISIBLE);
			textviewBestOfDWM2014.setText("Doctor Who Magazine\n2014 readers poll: #" + TVStory.getBestOfDWM2014());
			somebodyLikesMe++;
		} else {
			layoutBestOfDWM2014.setVisibility(View.GONE);
		}
		if (TVStory.getBestOfAVCTVC10()>0) {
			layoutBestOfAVCTVC10.setVisibility(View.VISIBLE);
			textviewBestOfAVCTVC10.setText("Recommended by\nChristopher Bahn");
			somebodyLikesMe++;
		} else {
			layoutBestOfAVCTVC10.setVisibility(View.GONE);
		}
		if (TVStory.getBestOfIo9()>0) {
			layoutBestOfIo9.setVisibility(View.VISIBLE);
			textviewBestOfIo9.setText("IO9 all-episode\nranking, 2013: #" + TVStory.getBestOfIo9());
			somebodyLikesMe++;
		} else {
			layoutBestOfIo9.setVisibility(View.GONE);
		}
		if (TVStory.getBestOfLMMyles()>0) {
			layoutBestOfLMMyles.setVisibility(View.VISIBLE);
			textviewBestOfLMMyles.setText("L.M. Myles all-episode\nranking, 2014: #" + TVStory.getBestOfLMMyles());
			somebodyLikesMe++;
		} else {
			layoutBestOfLMMyles.setVisibility(View.GONE);
		}
		if (somebodyLikesMe==0) {
			layoutAllBestOf.setVisibility(View.GONE);
		}
	}


}
