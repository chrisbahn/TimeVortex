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
	private TVStoryDAO tvstoryDAO;
	private UpdateTVStoryTask task;

	public static final String ARG_ITEM_ID = "tvstory_fullpage_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvstoryDAO = new TVStoryDAO(getActivity());
		Bundle bundle = this.getArguments();
		TVStory = bundle.getParcelable("selectedTVStory");
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





		textviewErrorItunes.setVisibility(View.GONE);

		//---STORE TO SEARCH IMAGEBUTTONS---
		// todo this if statement is not successfully changing the ASIN variable. But I have confirmed that searching by ASIN does work, so this may not be a big deal since you'll be pulling that number via a different method in the real program
			asin = TVStory.getASIN();

		amazonImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// todo Is this popping open a new page because it needs to be placed in the WebView field? either a) fix this so it does, or b) verify that the MainActivity nav won't go away when Amazon page opens, which will allow you to return to main program
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
				// intent to launch browser here // todo this broke when you moved the JSON request: iTunesCollectionViewUrl is not catching the variable
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
		if (view == resetButton) {
			resetAllFields();
		} else if (view == returnToListButton||view == saveButton) {
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
			MainActivity activity = (MainActivity) getActivity();
            activity.onSaveButtonClicked();
            mSaveClicked.onSaveButtonClicked();
		}
	}


    // Container Activity must implement this interface
    public interface OnSaveButtonClickedListener {
        void onSaveButtonClicked();
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
            throw new ClassCastException(activity.toString()
                    + " must implement onSaveButtonClickedListener");
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

	// todo shouldn't you need to add all four amazon keys? Only one is listed below. And yet the searching still works!
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

}
