package christopherbahn.com.timevortex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


// ADAPTED FROM Project 2: Inspirer

// todo  DWCharacter and DWCrew objects which represent people who worked on the series. Used to populate cast lists, character profiles, and implement certain kinds of search
// todo customizable sorted episode lists
// todo add images. Picture thumbnail on all TVStory, DWCharacter and DWCrew objects.
// todo Ranked lists of episodes. 1) DIY one made by user. Ideally, this would be a drag-and-drop list of titles that the user can move a selected item up and down. 2) "Professional" rankings: DWMagazine, io9, etc., all of which get pulled into an aggregate best-of list.
// todo build episode database, based off the TVStory functionality from Inspirer. Difference: No need for the dialog popup. Clicking entry on list should go directly to what is now "TVStoryAddFragment." TVStoryAddFragment should become the display page for an individual episode. The user can ONLY edit certain things like the star rating and the boolean seenIt/wantToSeeIt
// TODO COMPLETE CONVERSION TO TIMEVORTEX


// Code to close a fragment adapted from this page: http://stackoverflow.com/a/18110614
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomTVStoryDialogFragment.TVStoryDialogFragmentListener, TVStorySearchFragment.OnSearchButtonClickedListener, TVStoryAddFragment.OnSaveButtonClickedListener, TVStoryFullPageFragment.OnSaveButtonClickedListener, TVStoryListFragment.OnListItemClickedListener, TVStorySearchListFragment.OnListItemClickedListener {

	private Fragment contentFragment;
	private TVStoryListFragment TVStoryListFragment;
    private TVStoryAddFragment TVStoryAddFragment;
    private TVStorySearchFragment TVStorySearchFragment;
    private TVStorySearchListFragment TVStorySearchListFragment;
    private TVStoryFullPageFragment TVStoryFullPageFragment;
    private Button gotoMainSearchListButton;
    private Button searchNotesButton;
    private TextView UIExplainer;
    private TVStoryDAO tvstoryDAO;
    private ArrayList<TVStory> tempMatrix = new ArrayList<TVStory>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        gotoMainSearchListButton = (Button) findViewById(R.id.gotomainsearchlist_button);
        searchNotesButton = (Button) findViewById(R.id.gotosearchpage_button);
        UIExplainer = (TextView) findViewById(R.id.app_ui_explainer_tv);
        tvstoryDAO = new TVStoryDAO(this);

        gotoMainSearchListButton.setOnClickListener(this);
        searchNotesButton.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

		// todo put this in a method of its own, to get it out of the way?
		/*
		 * This is called when orientation is changed.
		 */
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("content")) {
				String content = savedInstanceState.getString("content");
                if (content.equals(TVStoryAddFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(TVStoryAddFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.add_note);
                        contentFragment = fragmentManager.findFragmentByTag(TVStoryAddFragment.ARG_ITEM_ID);
                    }
                }
                else if (content.equals(TVStorySearchFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(TVStorySearchFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.search_notes);
                        contentFragment = fragmentManager.findFragmentByTag(TVStorySearchFragment.ARG_ITEM_ID);
                    }
                }
                else if (content.equals(TVStorySearchListFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(TVStorySearchListFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.search_results);
                        contentFragment = fragmentManager.findFragmentByTag(TVStorySearchListFragment.ARG_ITEM_ID);
                    }
                } else if (content.equals(TVStoryFullPageFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(TVStoryFullPageFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.search_notes);
                        contentFragment = fragmentManager.findFragmentByTag(TVStoryFullPageFragment.ARG_ITEM_ID);
                    }
                }
            }
			if (fragmentManager.findFragmentByTag(TVStoryListFragment.ARG_ITEM_ID) != null) {
                TVStoryListFragment = (TVStoryListFragment) fragmentManager.findFragmentByTag(TVStoryListFragment.ARG_ITEM_ID);
				contentFragment = TVStoryListFragment;
			}
		} else {
            TVStoryListFragment = new TVStoryListFragment();
			setFragmentTitle(R.string.app_name);
			switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
//            loadListofAllStoriesTextFile();
		}

        // todo Currently this line is commented out UNLESS a change to the original textfile database occurs and needs to be ported into the program. (Don't forget to change the DATABASE_VERSION number in DatabaseHelper.) How do I make this work so that if the SQL database already exists, this doesn't load in a bunch of new data on top of what's there?
//        loadListofAllStoriesTextFile();

    }

    @Override
	protected void onSaveInstanceState(Bundle outState) { // todo addd fullpage here
        if (contentFragment instanceof TVStoryAddFragment) {
            outState.putString("content", TVStoryAddFragment.ARG_ITEM_ID);
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
			case R.id.action_add: // todo change this fully to searchmainlist
                TVStoryListFragment = new TVStoryListFragment();
                setFragmentTitle(R.string.app_name);
                switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
                return true;
            case R.id.action_search:
                setFragmentTitle(R.string.search_notes);
                TVStorySearchFragment = new TVStorySearchFragment();
                // todo If you can get it working, send getAllHashtags data in a Bundle via the following three commented-out lines
                Toast.makeText(this, "Search a TVStory!", Toast.LENGTH_LONG).show();
                switchContent(TVStorySearchFragment, TVStorySearchFragment.ARG_ITEM_ID);
                return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * We consider TVStoryListFragment as the home fragment and it is not added to
	 * the back stack.
	 */
	public void switchContent(Fragment fragment, String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		while (fragmentManager.popBackStackImmediate())
			;

		if (fragment != null) {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
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

    @Override
    public void onSearchButtonClicked(String searchField, String searchParameter) {
        // this method calls up a list of selected episodes based on search criteria
        TVStorySearchListFragment = new TVStorySearchListFragment();
        setFragmentTitle(R.string.search_results);
        Bundle bundle=new Bundle();
        bundle.putString("searchField", searchField);
        bundle.putString("searchParameter", searchParameter);
        TVStorySearchListFragment.setArguments(bundle);
        switchContent(TVStorySearchListFragment, TVStorySearchListFragment.ARG_ITEM_ID);
    }


    public void onSaveButtonClicked() {
        // todo this is really a "return to main list of all episodes" button. You could use onSearchButtonClicked or some third similarly-structured alternative to return users to search lists instead of the main.
        TVStoryListFragment = new TVStoryListFragment();
        setFragmentTitle(R.string.app_name);
        switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
    }

    // helps move from the primary all-episodes list to a single-page view of one story
    public void onListItemClicked(TVStory tvStory) {
        TVStoryFullPageFragment = new TVStoryFullPageFragment();
        setFragmentTitle(R.string.app_name);
        Bundle bundle=new Bundle();
        bundle.putParcelable("selectedTVStory", tvStory);
        TVStoryFullPageFragment.setArguments(bundle);
        switchContent(TVStoryFullPageFragment, TVStoryFullPageFragment.ARG_ITEM_ID);
    }


    @Override
    public void onClick(View view) {
        if (view == gotoMainSearchListButton) {
            TVStoryListFragment = new TVStoryListFragment();
            setFragmentTitle(R.string.app_name);
            switchContent(TVStoryListFragment, TVStoryListFragment.ARG_ITEM_ID);
        } else if (view == searchNotesButton) {
            setFragmentTitle(R.string.search_notes);
            TVStorySearchFragment = new TVStorySearchFragment();
            switchContent(TVStorySearchFragment, TVStorySearchFragment.ARG_ITEM_ID);
            Toast.makeText(this, "Search a TVStory!", Toast.LENGTH_LONG).show();
        }
    }




    public void loadListofAllStoriesTextFile() {
        // Imports data from Raw textfile(s) into the SQLite database, via an intermediary ArrayList<String[]>, which then builds TVStory objects, which are then added to the DB,
        // data is placed into coffeeData, a three-column array
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










}
