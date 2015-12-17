package christopherbahn.com.timevortex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
	private TextView tvstoryTitle;
	private TextView tvstorySeasonInfo;
	private TextView tvstorySynopsis;
	private TextView tvstoryCastAndCrew;
	private CheckBox seenIt;
	private CheckBox wantToSeeIt;
	private TextView TVuserStarRatingTitle;
	private RatingBar userStarRating;
	private float userStarRatingNumber; // app user's rating of the episode
	// TODO compile and add "professional" ratings based on critical reviews and best-of lists

	private TextView TVMyNotesTitle;
	private ImageView tvstoryImage;
	private EditText EdtxtMyNotes;


	private TextView textviewBestOfBBCAmerica;
	private TextView textviewBestOfDWM2009;
	private TextView textviewBestOfDWM2014;
	private TextView textviewBestOfAVCTVC10;
	private TextView textviewBestOfIo9;
	private ImageButton imageButtonBestOfBBCAmerica;
	private ImageButton imageButtonButtonBestOfDWM2009;
	private ImageButton imageButtonButtonBestOfDWM2014;
	private ImageButton imageButtonButtonBestOfAVCTVC10;
	private ImageButton imageButtonButtonBestOfIo9;

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

    OnSaveButtonClickedListener mSaveClicked;

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	DatePickerDialog datePickerDialog;
	Calendar dateCalendar;

	private TVStory TVStory;
	private SearchTerm searchTerm;
	private TVStoryDAO tvstoryDAO;
	private UpdateTVStoryTask task;
	OnSearchButtonClickedListener mSearchClicked;

	public static final String ARG_ITEM_ID = "tvstory_fullpage_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		searchTerm.setCameFromSearchResult(false); // automatically flips to true if the user came from anything but the all-episodes list, via SearchListFragment rather than ListFragment
		tvstoryDAO = new TVStoryDAO(getActivity());
		Bundle bundle = this.getArguments();
		TVStory = bundle.getParcelable("selectedTVStory");
		searchTerm = bundle.getParcelable("searchTerm");
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

		//todo move to its own method, or to listener ---STORE TO SEARCH IMAGEBUTTONS---
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
				TVStory.setSeenIt(isChecked);
			}
		});
		wantToSeeIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				TVStory.setWantToSeeIt(isChecked);
			}
		});
		addListenerOnRatingBar();
		imageButtonBestOfBBCAmerica.setOnClickListener(this);
		imageButtonButtonBestOfDWM2009.setOnClickListener(this);
		imageButtonButtonBestOfDWM2014.setOnClickListener(this);
		imageButtonButtonBestOfAVCTVC10.setOnClickListener(this);
		imageButtonButtonBestOfIo9.setOnClickListener(this);
	}

	protected void resetAllFields() {
//		EdtxtEnterTitle.setText("");
//		EdtxtEnterTextfield.setText("");
//		EdtxtEnterHashtags.setText("");

	}

	private void setTVStory() {// todo this would create a new episode, which we don't want. I should erase this, yes?
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
		TVuserStarRatingTitle = (TextView) rootView.findViewById(R.id.textview_myrating);
		userStarRating = (RatingBar) rootView.findViewById(R.id.UserStarRatingBar);
		TVMyNotesTitle = (TextView) rootView.findViewById(R.id.textview_mynotes_title);
		EdtxtMyNotes = (EditText) rootView.findViewById(R.id.mynotes_edittext);

		imageButtonBestOfBBCAmerica = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogobbc);
		imageButtonButtonBestOfDWM2009 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogodwm2009);
		imageButtonButtonBestOfDWM2014 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogodwm2014);
		imageButtonButtonBestOfAVCTVC10 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogoavclub);
		imageButtonButtonBestOfIo9 = (ImageButton) rootView.findViewById(R.id.imageButtonMedialogoio9);
		textviewBestOfBBCAmerica = (TextView) rootView.findViewById(R.id.textviewBestOfBBCAmerica);
		textviewBestOfDWM2009 = (TextView) rootView.findViewById(R.id.textviewBestOfDWM2009);
		textviewBestOfDWM2014 = (TextView) rootView.findViewById(R.id.textviewBestOfDWM2014);
		textviewBestOfAVCTVC10 = (TextView) rootView.findViewById(R.id.textviewBestOfAVCTVC10);
		textviewBestOfIo9 = (TextView) rootView.findViewById(R.id.textviewBestOfIo9);
		layoutAllBestOf = (LinearLayout) rootView.findViewById(R.id.layoutAllBestOf);
		layoutBestOfBBCAmerica = (LinearLayout) rootView.findViewById(R.id.layoutBestOfBBCAmerica);
		layoutBestOfDWM2009 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfDWM2009);
		layoutBestOfDWM2014 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfDWM2014);
		layoutBestOfAVCTVC10 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfAVCTVC10);
		layoutBestOfIo9 = (LinearLayout) rootView.findViewById(R.id.layoutBestOfIo9);

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
		if (view == imageButtonBestOfBBCAmerica) {
			searchTerm.setBestOfLists("BBCAmerica");
		}
		else if (view == imageButtonButtonBestOfDWM2009) {
			searchTerm.setBestOfLists("DWM2009");
		}
		else if (view == imageButtonButtonBestOfDWM2014) {
			searchTerm.setBestOfLists("DWM2014");
		}
		else if (view == imageButtonButtonBestOfAVCTVC10) {
			searchTerm.setBestOfLists("AVCTVC10");
		}
		else if (view == imageButtonButtonBestOfIo9) {
			searchTerm.setBestOfLists("Io9");
		}
		else if (view == returnToListButton||view == saveButton) {
				if (view == saveButton) {
					// this saves whatever user-editable data was changed
					TVStory.setUserReview(EdtxtMyNotes.getText().toString());
					TVStory.setUserStarRatingNumber(userStarRating.getRating());
					TVStory.setSeenIt(seenIt.isChecked());
					TVStory.setWantToSeeIt(wantToSeeIt.isChecked());
					TVStory.setUserStarRatingNumber(userStarRating.getRating());

					task = new UpdateTVStoryTask(getActivity());
					task.execute((Void) null);
					}
				}

		// goes back to list of all stories
		if (searchTerm.cameFromSearchResult()==false)
		{ MainActivity activity = (MainActivity) getActivity();
			activity.onSaveButtonClicked();
			mSaveClicked.onSaveButtonClicked();
		} else if (searchTerm.cameFromSearchResult())
		{ // goes back to previously called search result
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
			// todo if/then statements to catch singular/plural "episode/episodes"
			tvstorySeasonInfo.setText(TVStory.getYearProduced()  + ". " + TVStory.getSeason() + " #" + TVStory.getSeasonStoryNumber() + " (" + TVStory.getEra()  + " era). " + TVStory.getEpisodes() + " episodes. " + (TVStory.getEpisodes()*TVStory.getEpisodeLength()) + "minutes."); // era, yearProduced, season, seasonStoryNumber, episode, episodeLength
			tvstorySynopsis.setText(TVStory.getSynopsis());
			// todo if you want to limit the size of the synopsis in the future, here's the code
//        if (TVStory.getSynopsis().length() > 100) {
//            holder.tvstorySynopsis.setText(TVStory.getSynopsis().substring(0, 97) + "...");
//        }
			// todo getDoctor should return the text name of the character, not the numeral. getOtherCast should return only shortNames in the ListView. create a toString()?
			// todo getCrew should return the writer only in the listview.
			tvstoryCastAndCrew.setText(TVStory.getDoctor() + ", " + TVStory.getOtherCast() + ", " + TVStory.getCrew());
			seenIt.setChecked(TVStory.seenIt());
			wantToSeeIt.setChecked(TVStory.wantToSeeIt());

			userStarRating.setRating(TVStory.getUserStarRatingNumber());
			EdtxtMyNotes.setText(TVStory.getUserReview());

			int whichDoctor = TVStory.getDoctor();
			whichDoctorIsIt(whichDoctor);



		}
	}

		@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
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

    public class UpdateTVStoryTask extends AsyncTask<Void, Void, Long> {

		private final WeakReference<Activity> activityWeakRef;

		public UpdateTVStoryTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected Long doInBackground(Void... arg0) {
			long result = tvstoryDAO.update(TVStory);
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
					String artworkUrl100 = iTunesTopResult.getString("artworkUrl100"); // todo use this to replace default image
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


	// todo A kludgy and temporary way to change the image and cast info. Will be rewritten!
	private void whichDoctorIsIt(int whichDoctor){
		if (whichDoctor==1) {
			tvstoryCastAndCrew.setText("First Doctor (William Hartnell)");
			tvstoryImage.setImageResource(R.drawable.logodoctor01);
		}
		if (whichDoctor==2) {
			tvstoryCastAndCrew.setText("Second Doctor (Patrick Troughton)");
			tvstoryImage.setImageResource(R.drawable.logodoctor02);
		}
		if (whichDoctor==3) {
			tvstoryCastAndCrew.setText("Third Doctor (Jon Pertwee)");
			tvstoryImage.setImageResource(R.drawable.logodoctor03);
		}
		if (whichDoctor==4) {
			tvstoryCastAndCrew.setText("Fourth Doctor (Tom Baker)");
			tvstoryImage.setImageResource(R.drawable.logodoctor04);
		}
		if (whichDoctor==5) {
			tvstoryCastAndCrew.setText("Fifth Doctor (Peter Davison)");
			tvstoryImage.setImageResource(R.drawable.logodoctor05);
		}
		if (whichDoctor==6) {
			tvstoryCastAndCrew.setText("Sixth Doctor (Colin Baker)");
			tvstoryImage.setImageResource(R.drawable.logodoctor06);
		}
		if (whichDoctor==7) {
			tvstoryCastAndCrew.setText("Seventh Doctor (Sylvester McCoy)");
			tvstoryImage.setImageResource(R.drawable.logodoctor07);
		}
		if (whichDoctor==8) {
			tvstoryCastAndCrew.setText("Eighth Doctor (Paul McGann)");
			tvstoryImage.setImageResource(R.drawable.logodoctor08);
		}
		if (whichDoctor==9) {
			tvstoryCastAndCrew.setText("Ninth Doctor (Christopher Ecclestone)");
			tvstoryImage.setImageResource(R.drawable.logodoctor09);
		}
		if (whichDoctor==10) {
			tvstoryCastAndCrew.setText("Tenth Doctor (David Tennant)");
			tvstoryImage.setImageResource(R.drawable.logodoctor10);
		}
		if (whichDoctor==11) {
			tvstoryCastAndCrew.setText("Eleventh Doctor (Matt Smith)");
			tvstoryImage.setImageResource(R.drawable.logodoctor11);
		}
		if (whichDoctor==12) {
			tvstoryCastAndCrew.setText("Twelfth Doctor (Peter Capaldi)");
			tvstoryImage.setImageResource(R.drawable.logodoctor12);
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
		int somebodyLikesMe = 0;
		if (TVStory.getBestOfBBCAmerica()>0) {
			layoutBestOfBBCAmerica.setVisibility(View.VISIBLE);
			textviewBestOfBBCAmerica.setText("BBC Critics\nPoll 2013: #" + TVStory.getBestOfBBCAmerica());
			somebodyLikesMe++;
		} else {
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
		if (somebodyLikesMe==0) {
			layoutAllBestOf.setVisibility(View.GONE);
		}
	}


}
