package christopherbahn.com.timevortex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
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
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static android.R.id.message;
import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
//        CustomTVStoryDialogFragment.TVStoryDialogFragmentListener,
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
    private ArrayList<TVStory> allTVStories = new ArrayList<TVStory>();
    private ArrayList<UserTVStoryInfo> allUserTVStoryInfo = new ArrayList<UserTVStoryInfo>();
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private FBTVStoryListAdapter mFBTVStoryListAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    private String mUserName;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            Log.d("mFirebaseUser == null","User not logged in: Must log in!");
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();
            Log.d("mFirebaseUser != null","User " + mUserId + " logged in: Welcome!");

        gotoMainSearchListButton = (Button) findViewById(R.id.gotomainsearchlist_button);
        gotoSearchPageButton = (Button) findViewById(R.id.gotosearchpage_button);
        getRandomEpisodeButton = (Button) findViewById(R.id.getrandom_button);
        AboutDoctorWhoButton = (Button) findViewById(R.id.aboutdoctorwho_button);
        UIExplainer = (TextView) findViewById(R.id.app_ui_explainer_tv);
        tvstoryDAO = new TVStoryDAO(this);
        SearchTerm searchTerm = new SearchTerm();
        final ArrayList<TVStory> allTVStories = new ArrayList<TVStory>();
        final ArrayList<UserTVStoryInfo> allUserTVStoryInfo = new ArrayList<UserTVStoryInfo>();
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
                } else if (content.equals(TVStorySearchListFragment.ARG_ITEM_ID)) {
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
            switchContentToTVStoryListFragment();
            UIExplainer.setVisibility(View.VISIBLE);
        }

        // <%%%BEGIN FIREBASE SETUP%%%>
        // Write a message to the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference TVStoryRef = mDatabase.getReference("TVStory");
        DatabaseReference DWCharacterRef = mDatabase.getReference("DWCharacter");
        DatabaseReference DWCrewRef = mDatabase.getReference("DWCrew");
        DWCharacterRef.setValue("Fourth Doctor");
        DWCrewRef.setValue("Tom Baker");
        DatabaseReference TestUserRef = mDatabase.getReference("Users").child("1");
        TestUserRef.child("username").setValue("chrisbahn");
        TestUserRef.child("email").setValue("a@b.com");
        DatabaseReference TestUserTVStoryInfoRef = mDatabase.getReference("Users").child("1").child("UserTVStoryInfo");

//        loadListofAllStoriesTextFileIntoFirebase(); // uncomment this line when there is a database change you want to port into Firebase
        // <%%%END FIREBASE SETUP%%%>

//        // Downloads TVStory node from Firebase, and creates the allTVStories ArrayList
        TVStoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<TVStory> tempallTVStories = new ArrayList<TVStory>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "on TVStory data change");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TVStory TVStory = postSnapshot.getValue(TVStory.class);
                    tempallTVStories.add(TVStory);
                }
                setAllTVStories(tempallTVStories);
                System.out.println("FIREBASE TVSTORY LIST UPDATED");

                // Following section creates a set of UserTVStoryInfo nodes for a User to match up with allTVStories. These are called on later to save UserTVStoryInfo data
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference TestUserTVStoryInfoRef = mDatabase.getReference("Users").child("1").child("UserTVStoryInfo");
//                // todo Problem: The UTVSInfo node and allUserTVStoryInfo creation work OK while the program is running, meaning that user reviews etc are saved throughout the program's lifetime, BUT because of the way this is set up, the node and ArrayList are both cleared and begun anew anytime the program is restarted. This issue can be fixed when User accounts are set up, because in that case I can force a new allUserTVStoryInfo node&ArrayList to be made only on creation of new User account, and otherwise it loads the User's saved allUserTVStoryInfo.
                for (TVStory TVStory : tempallTVStories) {
//                    Log.d("UTVSInfo creation: ", TVStory.toString());
                    DatabaseReference TestUserTVStoryInfoChildrenRef = mDatabase.getReference("Users").child("1").child("UserTVStoryInfo").child(String.valueOf(TVStory.getStoryID()));
                    TestUserTVStoryInfoChildrenRef.child("storyID").setValue(TVStory.getStoryID());
                    TestUserTVStoryInfoChildrenRef.child("iveSeenIt").setValue(false);
                    TestUserTVStoryInfoChildrenRef.child("iOwnIt").setValue(false);
                    TestUserTVStoryInfoChildrenRef.child("iWantToSeeIt").setValue(false);
                    TestUserTVStoryInfoChildrenRef.child("userReview").setValue("");
                    TestUserTVStoryInfoChildrenRef.child("userAtoF").setValue(0);
                    TestUserTVStoryInfoChildrenRef.child("numberRanking").setValue(0);
                    // Also creates a default set of ArrayList<UserTVStoryInfo> allUserTVStoryInfo, with everything set to zero/blank, which gives an anchoring point for the search function so we can avoid a maze of Listeners
                    UserTVStoryInfo utvi = new UserTVStoryInfo();
                    utvi.setStoryID(TVStory.getStoryID());
                    utvi.setIveSeenIt(false);
                    utvi.setiOwnIt(false);
                    utvi.setiWantToSeeIt(false);
                    utvi.setUserReview("");
                    utvi.setUserAtoF(0);
                    utvi.setNumberRanking(0);
                    allUserTVStoryInfo.add(utvi);
                }
                setAllUserTVStoryInfo(allUserTVStoryInfo);
                switchContentToTVStoryListFragment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            }
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
                switchContentToAboutDoctorWhoFragment();
                return true;
            case R.id.action_gotomainlist:
                switchContentToTVStoryListFragment();
                return true;
            case R.id.action_search:
                switchContentToSearchFragment();
                return true;
            case R.id.action_random:
                goToRandomTVStory();
                return true;
            case R.id.action_logout: {
                mFirebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*
	 * TVStoryListFragment is the home fragment and is not added to the back stack.
	 */
    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate()) ;

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

    // quits on backpress when in TVStoryListFragment
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

//@Override
//public void onFinishDialog() {
//        if (TVStoryListFragment != null) {
//        TVStoryListFragment.updateView();
//        }
//        }

    // called when search button is clicked on TVStorySearchFragment
    @Override
    public void onSearchButtonClicked(SearchTerm searchTerm) {
        // this method calls up a list of selected episodes based on search criteria
        TVStorySearchListFragment = new TVStorySearchListFragment();
        setFragmentTitle(R.string.search_results);
        Bundle bundle = new Bundle();
        bundle.putParcelable("searchTerm", searchTerm);
        bundle.putParcelableArrayList("allTVStories", allTVStories);
        bundle.putParcelableArrayList("allUserTVStoryInfo", allUserTVStoryInfo);
        TVStorySearchListFragment.setArguments(bundle);
        switchContent(TVStorySearchListFragment, TVStorySearchListFragment.ARG_ITEM_ID);
    }

    // called when a user saves changes to UserTVStoryInfo - goes back to main list
    public void onSaveButtonClicked() {
        switchContentToTVStoryListFragment();
    }

    // when you click on a TVStory in a list, this launches a single-TVstory FullPageFragment
    public void onListItemClicked(final TVStory tvStory, final SearchTerm searchTerm) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference TestUserTVStoryInfoRef = mDatabase.getReference("Users").child("1").child("UserTVStoryInfo");
        TestUserTVStoryInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "on User data change");
                UserTVStoryInfo userTVStoryInfo = dataSnapshot.child(String.valueOf(tvStory.getStoryID())).getValue(UserTVStoryInfo.class);
                TVStoryFullPageFragment = new TVStoryFullPageFragment();
                setFragmentTitle(R.string.app_name);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedTVStory", tvStory);
                bundle.putParcelable("searchTerm", searchTerm);
                bundle.putParcelable("userTVStoryInfo", userTVStoryInfo);
                bundle.putParcelableArrayList("allUserTVStoryInfo", allUserTVStoryInfo);
                TVStoryFullPageFragment.setArguments(bundle);
                System.out.println("In MainActivity onListItemClicked, userTVStoryInfo's user review says: " + userTVStoryInfo.getUserReview());
                Log.d("onListItemClicked", tvStory.toString());
                switchContent(TVStoryFullPageFragment, TVStoryFullPageFragment.ARG_ITEM_ID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    // helps move from the primary all-episodes list to a single-page view of one story
    public void onSearchListItemClicked(final TVStory tvStory, final SearchTerm searchTerm) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference TestUserTVStoryInfoRef = mDatabase.getReference("Users").child("1").child("UserTVStoryInfo");
        TestUserTVStoryInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "on User data change");
                UserTVStoryInfo userTVStoryInfo = dataSnapshot.child(String.valueOf(tvStory.getStoryID())).getValue(UserTVStoryInfo.class);
                TVStoryFullPageFragment = new TVStoryFullPageFragment();
                setFragmentTitle(R.string.app_name);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedTVStory", tvStory);
                bundle.putParcelable("searchTerm", searchTerm);
                bundle.putParcelable("userTVStoryInfo", userTVStoryInfo);
                bundle.putParcelableArrayList("allUserTVStoryInfo", allUserTVStoryInfo);
                TVStoryFullPageFragment.setArguments(bundle);
                Log.d("onListItemClicked", tvStory.toString());
                System.out.println("In MainActivity onListItemClicked, userTVStoryInfo's user review says: " + userTVStoryInfo.getUserReview());
                switchContent(TVStoryFullPageFragment, TVStoryFullPageFragment.ARG_ITEM_ID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == gotoMainSearchListButton) {
            switchContentToTVStoryListFragment();
        } else if (view == gotoSearchPageButton) {
            switchContentToSearchFragment();
        } else if (view == getRandomEpisodeButton) {
            goToRandomTVStory();
        } else if (view == AboutDoctorWhoButton) {
            switchContentToAboutDoctorWhoFragment();
        }
    }

    public void loadListofAllStoriesTextFileIntoFirebase() {
        // Imports data from Raw textfile(s) into the Firebase database, via an intermediary ArrayList<String[]>, which then builds TVStory objects, which are then added to the DB,
        ArrayList<String[]> listofAllStoriesData = new ArrayList<String[]>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        try {
            InputStream fIn = getResources().openRawResource(getResources().getIdentifier("raw/listofallstories", "raw", getPackageName()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] storyLine = line.split(";;;;;;;");
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
//                    System.out.println("tempMatrix: " + story);
            }

            //---port information in listofAllStoriesData into Firebase DB
            for (TVStory matrixFile : tempMatrix) {
                // Write a message to the Firebase database
                String storyId = String.valueOf(matrixFile.getStoryID());
                database.getReference("TVStory/" + storyId).setValue(matrixFile);
            }
            Toast.makeText(getBaseContext(), "File loaded successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public ArrayList<TVStory> getAllTVStories() {
        return this.allTVStories;
    }

    public void setAllTVStories(ArrayList<TVStory> allTVStories) {
        this.allTVStories = allTVStories;
    }

    public ArrayList<UserTVStoryInfo> getAllUserTVStoryInfo() {
        return this.allUserTVStoryInfo;
    }

    public void setAllUserTVStoryInfo(ArrayList<UserTVStoryInfo> allUserTVStoryInfo) {
        this.allUserTVStoryInfo = allUserTVStoryInfo;
    }

    public void switchContentToTVStoryListFragment() {
        TVStoryListFragment = new TVStoryListFragment();
        setFragmentTitle(R.string.app_name);
        SearchTerm searchTerm = new SearchTerm();
        searchTerm.setCameFromSearchResult(false);
        Bundle bundle = new Bundle();
        bundle.putParcelable("searchTerm", searchTerm);
        bundle.putParcelableArrayList("allTVStories", allTVStories);
        bundle.putParcelableArrayList("allUserTVStoryInfo", allUserTVStoryInfo);
        TVStoryListFragment.setArguments(bundle);
        switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
    }

    public void goToRandomTVStory() {
        setFragmentTitle(R.string.getrandom_button_label);
        Random randomizer = new Random();
        TVStory tvStory = allTVStories.get(randomizer.nextInt(allTVStories.size()));
        UserTVStoryInfo userTVStoryInfo = allUserTVStoryInfo.get(tvStory.getStoryID()-1);
        SearchTerm searchTerm = new SearchTerm();
        searchTerm.setCameFromSearchResult(false);
        searchTerm.setStoryID(randomizer.nextInt(allTVStories.size()));
        Toast.makeText(this, "Get a random episode (and beware the Black Guardian!)", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Randomized number: " + randomizer.nextInt(allTVStories.size()) + ". allTVStories.size(): " + allTVStories.size(), Toast.LENGTH_LONG).show();
        onListItemClicked(tvStory, searchTerm);
    }

    public void switchContentToAboutDoctorWhoFragment() {
        setFragmentTitle(R.string.aboutdoctorwho_button_label);
        AboutDoctorWhoFragment = new AboutDoctorWhoFragment();
        switchContent(AboutDoctorWhoFragment, AboutDoctorWhoFragment.ARG_ITEM_ID);
        Toast.makeText(this, "What is Doctor Who and where to start watching it!", Toast.LENGTH_LONG).show();
    }

    public void switchContentToSearchFragment() {
        setFragmentTitle(R.string.gotosearchpage_button_label);
        TVStorySearchFragment = new TVStorySearchFragment();
        Toast.makeText(this, "Search for a TVStory!", Toast.LENGTH_LONG).show();
        switchContent(TVStorySearchFragment, TVStorySearchFragment.ARG_ITEM_ID);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://christopherbahn.com"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
