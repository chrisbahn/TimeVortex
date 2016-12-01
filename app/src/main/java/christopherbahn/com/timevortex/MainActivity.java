package christopherbahn.com.timevortex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



// todo  DWCharacter and DWCrew objects which represent people who worked on the series. Used to populate cast lists, character profiles, and implement certain kinds of search
// todo add images. Picture thumbnail on all TVStory, DWCharacter and DWCrew objects.
// todo Ranked lists of episodes. 1) DIY one made by user. Ideally, this would be a drag-and-drop list of titles that the user can move a selected item up and down. 2) "Professional" rankings: DWMagazine, io9, etc., all of which get pulled into an aggregate best-of list.


// ADAPTED FROM Project 2: Inspirer
// Code to close a fragment adapted from this page: http://stackoverflow.com/a/18110614
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CustomTVStoryDialogFragment.TVStoryDialogFragmentListener,
        AboutDoctorWhoFragment.OnSearchButtonClickedListener,
        TVStoryFullPageFragment.OnSaveButtonClickedListener,
        TVStoryFullPageFragment.OnSearchButtonClickedListener,
        TVStoryListFragment.OnListItemClickedListener,
        TVStorySearchFragment.OnSearchButtonClickedListener,
        TVStorySearchListFragment.onSearchListItemClickedListener {

	private Fragment contentFragment;
	private TVStoryListFragment TVStoryListFragment;
    private TVStorySearchFragment TVStorySearchFragment;
    private TVStorySearchListFragment TVStorySearchListFragment;
    private TVStoryFullPageFragment TVStoryFullPageFragment;
    private AboutDoctorWhoFragment AboutDoctorWhoFragment;
    private Button gotoMainSearchListButton;
    private Button gotoSearchPageButton;
    private Button getRandomEpisodeButton;
    private Button AboutDoctorWhoButton;
    private TextView UIExplainer;
    private TVStoryDAO tvstoryDAO;
    private ArrayList<TVStory> tempMatrix = new ArrayList<TVStory>();
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private FBTVStoryListAdapter mFBTVStoryListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        gotoMainSearchListButton = (Button) findViewById(R.id.gotomainsearchlist_button);
        gotoSearchPageButton = (Button) findViewById(R.id.gotosearchpage_button);
        getRandomEpisodeButton = (Button) findViewById(R.id.getrandom_button);
        AboutDoctorWhoButton = (Button) findViewById(R.id.aboutdoctorwho_button);
        UIExplainer = (TextView) findViewById(R.id.app_ui_explainer_tv);
        tvstoryDAO = new TVStoryDAO(this);
        SearchTerm searchTerm = new SearchTerm();
        final ArrayList<TVStory> allTVStories = new ArrayList<TVStory>();
        searchTerm.setCameFromSearchResult(false);

        gotoMainSearchListButton.setOnClickListener(this);
        gotoSearchPageButton.setOnClickListener(this);
        getRandomEpisodeButton.setOnClickListener(this);
        AboutDoctorWhoButton.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

		/*
		 * This is called when orientation is changed.
		 */
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("content")) {
				String content = savedInstanceState.getString("content");
                if (content.equals(TVStorySearchFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(TVStorySearchFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.search_tvstories);
                        contentFragment = fragmentManager.findFragmentByTag(TVStorySearchFragment.ARG_ITEM_ID);
                        UIExplainer.setVisibility(View.GONE);
                    }
                }
                else if (content.equals(TVStorySearchListFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(TVStorySearchListFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.search_results);
                        contentFragment = fragmentManager.findFragmentByTag(TVStorySearchListFragment.ARG_ITEM_ID);
                        UIExplainer.setVisibility(View.VISIBLE);
                    }
                } else if (content.equals(TVStoryFullPageFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(TVStorySearchListFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.search_results);
                        contentFragment = fragmentManager.findFragmentByTag(TVStorySearchListFragment.ARG_ITEM_ID);
                        UIExplainer.setVisibility(View.GONE);
                    }
                } else if (content.equals(AboutDoctorWhoFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(AboutDoctorWhoFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.aboutdoctorwho_title);
                        contentFragment = fragmentManager.findFragmentByTag(AboutDoctorWhoFragment.ARG_ITEM_ID);
                        UIExplainer.setVisibility(View.GONE);
                    }
                }
            }
			if (fragmentManager.findFragmentByTag(TVStoryListFragment.ARG_ITEM_ID) != null) {
                TVStoryListFragment = (TVStoryListFragment) fragmentManager.findFragmentByTag(TVStoryListFragment.ARG_ITEM_ID);
				contentFragment = TVStoryListFragment;
                UIExplainer.setVisibility(View.VISIBLE);
			}
		} else {
            TVStoryListFragment = new TVStoryListFragment();
			setFragmentTitle(R.string.app_name);
//            SearchTerm searchTerm = new SearchTerm();
            searchTerm.setCameFromSearchResult(false);
            Bundle bundle=new Bundle();
            bundle.putParcelable("searchTerm", searchTerm);
            TVStoryListFragment.setArguments(bundle);
			switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
            UIExplainer.setVisibility(View.VISIBLE);
		}

        // todo This method governs importation of txtfile into SQLite database, which will become irrelevant once Firebase is done. Currently this line is commented out UNLESS a change to the original textfile database occurs and needs to be ported into the program. (Don't forget to change the DATABASE_VERSION number in DatabaseHelper.)
//        loadListofAllStoriesTextFile();

        // <%%%BEGIN FIREBASE SETUP%%%>
        // Write a message to the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference TVStoryRef = mDatabase.getReference("TVStory");
        DatabaseReference DWCharacterRef = mDatabase.getReference("DWCharacter");
        DatabaseReference DWCrewRef = mDatabase.getReference("DWCrew");
        DWCharacterRef.setValue("Third Doctor");
        DWCrewRef.setValue("Jon Pertwee");
        loadListofAllStoriesTextFileIntoFirebase();

        // FIREBASE CODE BEGINS HERE

        // Attach a listener to read the data at our posts reference
        TVStoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<TVStory> tempallTVStories = new ArrayList<TVStory>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TVStory TVStory = postSnapshot.getValue(TVStory.class);
                    Toast.makeText(getBaseContext(), TVStory.getTitle(), Toast.LENGTH_LONG).show();
                    tempallTVStories.add(TVStory);
                }
                allTVStories.addAll(tempallTVStories);
                // Build the adapter
                ListAdapter adapter = new TVStoryListAdapter(getBaseContext(), allTVStories);
                // Configure the list view
                ListView listView = (ListView) findViewById(R.id.firebaseListView);
                listView.setAdapter(adapter);
                System.out.println("FIREBASE TVSTORY LIST UPDATED");
                Toast.makeText(getBaseContext(), "FIREBASE TVSTORY LIST UPDATED!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        // FIREBASE CODE ENDS HERE


//         TODO Connect allTVStories to TVStoryListAdapter via TVStoryDAO. Although the following five lines DO work to make a Firebase instant-update list, search capability is a real problem, and TVStory info will never change as a result of user interaction, only by admin update. So, switching it to something more like old system.
        final ListView firebaseListView = (ListView) findViewById(R.id.firebaseListView);
        ListAdapter fbadapter = new FBTVStoryListAdapter(TVStoryRef, TVStory.class, R.layout.test_firebase_list_item, this)
        {
        };
        firebaseListView.setAdapter(fbadapter);


        System.out.println();
        Toast.makeText(getBaseContext(), "FIREBASE says allTVStories has this many elements: " + allTVStories.size(), Toast.LENGTH_LONG).show();


    }

    @Override
	protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof TVStoryFullPageFragment) {
            outState.putString("content", TVStoryFullPageFragment.ARG_ITEM_ID);
        } else if (contentFragment instanceof AboutDoctorWhoFragment) {
            outState.putString("content", AboutDoctorWhoFragment.ARG_ITEM_ID);
        } else if (contentFragment instanceof TVStorySearchFragment) {
            outState.putString("content", TVStorySearchFragment.ARG_ITEM_ID);
        } else if (contentFragment instanceof TVStorySearchListFragment) {
            outState.putString("content", TVStorySearchListFragment.ARG_ITEM_ID);
        } else {
			outState.putString("content", TVStoryListFragment.ARG_ITEM_ID);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
            case R.id.action_aboutdoctorwho:
                setFragmentTitle(R.string.aboutdoctorwho_button_label);
                AboutDoctorWhoFragment = new AboutDoctorWhoFragment();
                switchContent(AboutDoctorWhoFragment, AboutDoctorWhoFragment.ARG_ITEM_ID);
                Toast.makeText(this, "What is Doctor Who and where to start watching it!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_gotomainlist:
                TVStoryListFragment = new TVStoryListFragment();
                setFragmentTitle(R.string.app_name);
                SearchTerm searchTerm = new SearchTerm();
                searchTerm.setCameFromSearchResult(false);
                Bundle bundle=new Bundle();
                bundle.putParcelable("searchTerm", searchTerm);
                TVStoryListFragment.setArguments(bundle);
                switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
                return true;
            case R.id.action_search:
                setFragmentTitle(R.string.gotosearchpage_button_label);
                TVStorySearchFragment = new TVStorySearchFragment();
                Toast.makeText(this, "Search a TVStory!", Toast.LENGTH_LONG).show();
                switchContent(TVStorySearchFragment, TVStorySearchFragment.ARG_ITEM_ID);
                return true;
            case R.id.action_random:
                setFragmentTitle(R.string.getrandom_button_label);
                Random randomizer = new Random();
                TVStory tvStory = tvstoryDAO.getTVStory(randomizer.nextInt(tvstoryDAO.getAllTVStories().size()));
                searchTerm = new SearchTerm();
                searchTerm.setCameFromSearchResult(false);
                Toast.makeText(this, "Get a random episode (and beware the Black Guardian!)", Toast.LENGTH_LONG).show();
                onListItemClicked(tvStory, searchTerm);
                return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * TVStoryListFragment is the home fragment and is not added to the back stack.
	 */
	public void switchContent(Fragment fragment, String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		while (fragmentManager.popBackStackImmediate());

		if (fragment != null) {
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.content_frame, fragment, tag);
			//  TVStoryFullPageFragment, TVStorySearchFragment and TVStorySearchListFragment are added to the back stack.
			if (!(fragment instanceof TVStoryListFragment)) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			contentFragment = fragment;
		}
	}

	protected void setFragmentTitle(int resourceId) {
		setTitle(resourceId);
		getSupportActionBar().setTitle(resourceId);

	}

	/*
	 * We call super.onBackPressed(); when the stack entry count is > 0. if it
	 * is instanceof TVStoryListFragment or if the stack entry count is == 0, then
	 * we prompt the user whether to quit the app or not by displaying dialog.
	 * In other words, from TVStoryListFragment on back press it quits the app.
	 */
	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			super.onBackPressed();
		} else if (contentFragment instanceof TVStoryListFragment
				|| fm.getBackStackEntryCount() == 0) {
            //Shows an alert dialog on quit
			onShowQuitDialog();
		}
	}

	public void onShowQuitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);

		builder.setMessage("Do You Want To Quit?");
		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
		builder.create().show();
	}

@Override
public void onFinishDialog() {
        if (TVStoryListFragment != null) {
        TVStoryListFragment.updateView();
        }
        }

    // called when search button is clicked on TVStorySearchFragment
    @Override
    public void onSearchButtonClicked(SearchTerm searchTerm) {
        // this method calls up a list of selected episodes based on search criteria
        TVStorySearchListFragment = new TVStorySearchListFragment();
        setFragmentTitle(R.string.search_results);
        Bundle bundle=new Bundle();
        bundle.putParcelable("searchTerm", searchTerm);
        TVStorySearchListFragment.setArguments(bundle);
        switchContent(TVStorySearchListFragment, TVStorySearchListFragment.ARG_ITEM_ID);
    }

    // called when a user saves changes to userlists
    public void onSaveButtonClicked() {
        TVStoryListFragment = new TVStoryListFragment();
        setFragmentTitle(R.string.app_name);
        SearchTerm searchTerm = new SearchTerm();
        searchTerm.setCameFromSearchResult(false);
        Bundle bundle=new Bundle();
        bundle.putParcelable("searchTerm", searchTerm);
        TVStoryListFragment.setArguments(bundle);
        switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
    }

    // helps move from the primary all-episodes list to a single-page view of one story
    public void onListItemClicked(TVStory tvStory, SearchTerm searchTerm) {
        TVStoryFullPageFragment = new TVStoryFullPageFragment();
        setFragmentTitle(R.string.app_name);
        Bundle bundle=new Bundle();
        bundle.putParcelable("selectedTVStory", tvStory);
        bundle.putParcelable("searchTerm", searchTerm);
        TVStoryFullPageFragment.setArguments(bundle);
        switchContent(TVStoryFullPageFragment, TVStoryFullPageFragment.ARG_ITEM_ID);
    }

    // helps move from the primary all-episodes list to a single-page view of one story
    public void onSearchListItemClicked(TVStory tvStory, SearchTerm searchTerm) {
        TVStoryFullPageFragment = new TVStoryFullPageFragment();
        setFragmentTitle(R.string.app_name);
        Bundle bundle=new Bundle();
        bundle.putParcelable("selectedTVStory", tvStory);
        bundle.putParcelable("searchTerm", searchTerm);
        TVStoryFullPageFragment.setArguments(bundle);
        switchContent(TVStoryFullPageFragment, TVStoryFullPageFragment.ARG_ITEM_ID);
    }

    @Override
    public void onClick(View view) {
        if (view == gotoMainSearchListButton) {
            TVStoryListFragment = new TVStoryListFragment();
            setFragmentTitle(R.string.app_name);
            SearchTerm searchTerm = new SearchTerm();
            searchTerm.setCameFromSearchResult(false);
            Bundle bundle=new Bundle();
            bundle.putParcelable("searchTerm", searchTerm);
            TVStoryListFragment.setArguments(bundle);
            switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
        } else if (view == gotoSearchPageButton) {
            setFragmentTitle(R.string.gotosearchpage_button_label);
            TVStorySearchFragment = new TVStorySearchFragment();
            switchContent(TVStorySearchFragment, TVStorySearchFragment.ARG_ITEM_ID);
            Toast.makeText(this, "Search a TVStory!", Toast.LENGTH_LONG).show();
        } else if (view == getRandomEpisodeButton) {
            setFragmentTitle(R.string.getrandom_button_label);
            Random randomizer = new Random();
            TVStory tvStory = tvstoryDAO.getTVStory(randomizer.nextInt(tvstoryDAO.getAllTVStories().size()));
            SearchTerm searchTerm = new SearchTerm();
            searchTerm.setCameFromSearchResult(false);
            Toast.makeText(this, "Get a random episode (and beware the Black Guardian!)", Toast.LENGTH_LONG).show();
            onListItemClicked(tvStory, searchTerm);
        } else if (view == AboutDoctorWhoButton) {
            setFragmentTitle(R.string.aboutdoctorwho_button_label);
            AboutDoctorWhoFragment = new AboutDoctorWhoFragment();
            switchContent(AboutDoctorWhoFragment, AboutDoctorWhoFragment.ARG_ITEM_ID);
            Toast.makeText(this, "What is Doctor Who and where to start watching it!", Toast.LENGTH_LONG).show();
        }
    }



// Loads data into SQLite db - now irrelevant
    public void loadListofAllStoriesTextFile() {
        // Imports data from Raw textfile(s) into the SQLite database, via an intermediary ArrayList<String[]>, which then builds TVStory objects, which are then added to the DB,
        // Solution partially found here: http://stackoverflow.com/questions/32440604/how-to-read-a-txt-file-line-by-line-with-a-sqlite-script
        ArrayList<String[]> listofAllStoriesData = new ArrayList<String[]>();

        try
        {
            InputStream fIn = getResources().openRawResource(getResources().getIdentifier("raw/listofallstories", "raw", getPackageName()));
//            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));

            String line = null;
            while((line = reader.readLine()) != null) {
                String[] storyLine = line.split(";;;;;;;");
//                line = reader.readLine();
                // Read each line of the original data into an ArrayList
                listofAllStoriesData.add(storyLine);
                System.out.println("InputStream:" + storyLine[0] + ": " + storyLine[1]);

            }
            reader.close();
                        for (String[] matrixFile : listofAllStoriesData) {
                            TVStory story = new TVStory();
                            story.setStoryID(Integer.parseInt(matrixFile[0]));
                            story.setTitle(matrixFile[1]);
                            story.setDoctor(Integer.parseInt(matrixFile[2]));
                            story.setEra(matrixFile[3]);
                            story.setSeason(matrixFile[4]);
                            story.setSeasonStoryNumber(Integer.parseInt(matrixFile[5]));
                            story.setEpisodes(Integer.parseInt(matrixFile[6]));
                            story.setEpisodeLength(Integer.parseInt(matrixFile[7]));
                            story.setYearProduced(Integer.parseInt(matrixFile[8]));
                            story.setOtherCast(matrixFile[9]);
                            story.setSynopsis(matrixFile[10]);
                            story.setCrew(matrixFile[11]);
//                            story.setSeenIt(Boolean.parseBoolean(matrixFile[12]));
//                            story.setWantToSeeIt(Boolean.parseBoolean(matrixFile[13]));
                            story.setASIN(matrixFile[12]);
                            story.setBestOfBBCAmerica(Integer.parseInt(matrixFile[13]));
                            story.setBestOfDWM2009(Integer.parseInt(matrixFile[14]));
                            story.setBestOfDWM2014(Integer.parseInt(matrixFile[15]));
                            story.setBestOfAVCTVC10(Integer.parseInt(matrixFile[16]));
                            story.setBestOfIo9(Integer.parseInt(matrixFile[17]));
                            story.setBestOfLMMyles(Integer.parseInt(matrixFile[18]));
                            story.setBestOfBahn(Integer.parseInt(matrixFile[19]));
                            story.setTvstoryImage(matrixFile[20]);
                            tempMatrix.add(story);
                            System.out.println("tempMatrix:" + story);
                        }
            //---port information in listofAllStoriesData into SQLite DB
            for (TVStory matrixFile : tempMatrix) {
                tvstoryDAO.save(matrixFile);
                System.out.println(matrixFile.getTitle());
            }
            Toast.makeText(getBaseContext(), "File loaded successfully!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void loadListofAllStoriesTextFileIntoFirebase() {
        // Imports data from Raw textfile(s) into the Firebase database, via an intermediary ArrayList<String[]>, which then builds TVStory objects, which are then added to the DB,
        // Solution partially found here: http://stackoverflow.com/questions/32440604/how-to-read-a-txt-file-line-by-line-with-a-sqlite-script
        ArrayList<String[]> listofAllStoriesData = new ArrayList<String[]>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

            try
            {
                InputStream fIn = getResources().openRawResource(getResources().getIdentifier("raw/listofallstories", "raw", getPackageName()));
//            InputStreamReader isr = new InputStreamReader(fIn);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
                String line = null;
                while((line = reader.readLine()) != null) {
                    String[] storyLine = line.split(";;;;;;;");
//                line = reader.readLine();
                    // Read each line of the original data into an ArrayList
                    listofAllStoriesData.add(storyLine);
//                    System.out.println("InputStream:" + storyLine[0] + ": " + storyLine[1]);
                }
                reader.close();
                for (String[] matrixFile : listofAllStoriesData) {
                    TVStory story = new TVStory();
                    story.setStoryID(Integer.parseInt(matrixFile[0]));
                    story.setTitle(matrixFile[1]);
                    story.setDoctor(Integer.parseInt(matrixFile[2]));
                    story.setEra(matrixFile[3]);
                    story.setSeason(matrixFile[4]);
                    story.setSeasonStoryNumber(Integer.parseInt(matrixFile[5]));
                    story.setEpisodes(Integer.parseInt(matrixFile[6]));
                    story.setEpisodeLength(Integer.parseInt(matrixFile[7]));
                    story.setYearProduced(Integer.parseInt(matrixFile[8]));
                    story.setOtherCast(matrixFile[9]);
                    story.setSynopsis(matrixFile[10]);
                    story.setCrew(matrixFile[11]);
//                            story.setSeenIt(Boolean.parseBoolean(matrixFile[12]));
//                            story.setWantToSeeIt(Boolean.parseBoolean(matrixFile[13]));
                    story.setASIN(matrixFile[12]);
                    story.setBestOfBBCAmerica(Integer.parseInt(matrixFile[13]));
                    story.setBestOfDWM2009(Integer.parseInt(matrixFile[14]));
                    story.setBestOfDWM2014(Integer.parseInt(matrixFile[15]));
                    story.setBestOfAVCTVC10(Integer.parseInt(matrixFile[16]));
                    story.setBestOfIo9(Integer.parseInt(matrixFile[17]));
                    story.setBestOfLMMyles(Integer.parseInt(matrixFile[18]));
                    story.setBestOfBahn(Integer.parseInt(matrixFile[19]));
                    story.setTvstoryImage(matrixFile[20]);
                    tempMatrix.add(story);
                    System.out.println("tempMatrix: " + story);
                }

                //---port information in listofAllStoriesData into Firebase DB
                for (TVStory matrixFile : tempMatrix) {
                // Write a message to the Firebase database
                String storyId = String.valueOf(matrixFile.getStoryID());
                database.getReference("TVStory/" + storyId).setValue(matrixFile);
            }
            Toast.makeText(getBaseContext(), "File loaded successfully!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
}










}
