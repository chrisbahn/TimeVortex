package christopherbahn.com.timevortex;

/**
 * Created by christopherbahn on 11/27/16.
 */

// modified from https://github.com/firebase/AndroidChat/blob/master/app/src/main/java/com/firebase/androidchat/ChatListAdapter.java
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.Query;


public class FBTVStoryListAdapter extends FirebaseListAdapter<TVStory> {

    private String mUsername;
    private ImageView doctorImage;
    private ImageView tvstoryImage;
    private TextView tvstoryTitle;
    private TextView tvstorySeasonInfo;
    private TextView tvstorySynopsis;
    private TextView tvstoryCastAndCrew;
    private CheckBox seenIt;
    private CheckBox wantToSeeIt;
    private RatingBar userStarRating;
    int[] doctorImages = {R.drawable.doctor01, R.drawable.doctor02, R.drawable.doctor03, R.drawable.doctor04, R.drawable.doctor05, R.drawable.doctor06, R.drawable.doctor07, R.drawable.doctor08, R.drawable.doctor09, R.drawable.doctor10, R.drawable.doctor11, R.drawable.doctor12};
    int[] logoImages = {R.drawable.logodoctor01, R.drawable.logodoctor02, R.drawable.logodoctor03, R.drawable.logodoctor04, R.drawable.logodoctor05, R.drawable.logodoctor06, R.drawable.logodoctor07, R.drawable.logodoctor08, R.drawable.logodoctor09, R.drawable.logodoctor10, R.drawable.logodoctor11, R.drawable.logodoctor12};
    private Activity mActivity;

    public FBTVStoryListAdapter(Query ref, Class<TVStory> mModelClass, int layout, Activity activity) {
        super(ref, TVStory.class, layout, activity);
//        activity = getActivity();
        mActivity = activity;
//        this.mUsername = mUsername;
    }

    @Override
    protected void populateView(View view, TVStory TVStory)
    {
        ((ImageView)view.findViewById(R.id.list_item_doctor_image)).setImageResource(doctorImages[TVStory.getDoctor()-1]);
//         This gets the image for each TVStory.
                Resources res = mActivity.getResources();
                TypedArray tvstoryImages = res.obtainTypedArray(R.array.tvstoryImages);
        Drawable drawable = Drawable.createFromPath(TVStory.getTvstoryImage());
        ((ImageView)view.findViewById(R.id.list_item_story_image)).setImageResource(doctorImages[TVStory.getDoctor()-1]);
        ((TextView)view.findViewById(R.id.txt_listitem_title)).setText(TVStory.getStoryID()  + ": " + TVStory.getTitle());
        ((TextView)view.findViewById(R.id.txt_listitem_seasoninfo)).setText(TVStory.getYearProduced()  + ". " + TVStory.getSeason() + " #" + TVStory.getSeasonStoryNumber() + " (" + TVStory.getEra()  + " era). " + TVStory.getEpisodes() + " episodes. " + (TVStory.getEpisodes()*TVStory.getEpisodeLength()) + " minutes.");
        ((TextView)view.findViewById(R.id.txt_listitem_synopsis)).setText(TVStory.getSynopsis());
//        tvstorySynopsis.setVisibility(View.GONE); // not visible in main (all-items) list



    }



}