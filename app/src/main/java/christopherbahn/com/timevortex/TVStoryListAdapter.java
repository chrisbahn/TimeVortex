package christopherbahn.com.timevortex;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

// todo Can the ListFragment and SearchListFragment stuff be combined? They're doing a lot of the same work, and you have multiple kinds of searching planned

public class TVStoryListAdapter extends ArrayAdapter<TVStory> {

	private Context context;
	List<TVStory> TVStories;
	int[] doctorImages = {R.drawable.doctor01, R.drawable.doctor02, R.drawable.doctor03, R.drawable.doctor04, R.drawable.doctor05, R.drawable.doctor06, R.drawable.doctor07, R.drawable.doctor08, R.drawable.doctor09, R.drawable.doctor10, R.drawable.doctor11, R.drawable.doctor12};
	int[] logoImages = {R.drawable.logodoctor01, R.drawable.logodoctor02, R.drawable.logodoctor03, R.drawable.logodoctor04, R.drawable.logodoctor05, R.drawable.logodoctor06, R.drawable.logodoctor07, R.drawable.logodoctor08, R.drawable.logodoctor09, R.drawable.logodoctor10, R.drawable.logodoctor11, R.drawable.logodoctor12};

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	public TVStoryListAdapter(Context context, List<TVStory> TVStories) {
		super(context, R.layout.list_item, TVStories);
		this.context = context;
		this.TVStories = TVStories;
	}

	private class ViewHolder {
		ImageView doctorImage;
		ImageView tvstoryImage;
        TextView tvstoryTitle;
        TextView tvstorySeasonInfo;
        TextView tvstorySynopsis;
        TextView tvstoryCastAndCrew;
        CheckBox seenIt;
        CheckBox wantToSeeIt;
        RatingBar userStarRating;
        // TODO aggregate star rating
    }

    @Override
	public int getCount() {
		return TVStories.size();
	}

	@Override
	public TVStory getItem(int position) {
		return TVStories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();

			holder.doctorImage = (ImageView) convertView.findViewById(R.id.list_item_doctor_image);
			holder.tvstoryImage = (ImageView) convertView.findViewById(R.id.list_item_story_image);
			holder.tvstoryTitle = (TextView) convertView.findViewById(R.id.txt_listitem_title);
            holder.tvstorySeasonInfo = (TextView) convertView.findViewById(R.id.txt_listitem_seasoninfo);
            holder.tvstorySynopsis = (TextView) convertView.findViewById(R.id.txt_listitem_synopsis);
            holder.tvstoryCastAndCrew = (TextView) convertView.findViewById(R.id.txt_listitem_castandcrew);
            holder.seenIt = (CheckBox) convertView.findViewById(R.id.checkBox_listitem_seenit);
            holder.wantToSeeIt = (CheckBox) convertView.findViewById(R.id.checkBox_listitem_wanttoseeit);
            holder.userStarRating = (RatingBar) convertView.findViewById(R.id.UserStarRatingBar);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TVStory TVStory = (TVStory) getItem(position);
        holder.tvstoryTitle.setText(TVStory.getStoryID()  + ": " + TVStory.getTitle()); // story title, and maybe also storyID
        // todo if/then statements to catch singular/plural "episode/episodes"
        holder.tvstorySeasonInfo.setText(TVStory.getYearProduced()  + ". " + TVStory.getSeason() + " #" + TVStory.getSeasonStoryNumber() + " (" + TVStory.getEra()  + " era). " + TVStory.getEpisodes() + " episodes. " + (TVStory.getEpisodes()*TVStory.getEpisodeLength()) + " minutes."); // era, yearProduced, season, seasonStoryNumber, episode, episodeLength
        holder.tvstorySynopsis.setText(TVStory.getSynopsis());
		holder.tvstorySynopsis.setVisibility(View.GONE); // not visible in main (all-items) list
        // todo might be good to limit the size of the synopsis, but they're all blank now anyway so this is for later
//        if (TVStory.getSynopsis().length() > 100) {
//            holder.tvstorySynopsis.setText(TVStory.getSynopsis().substring(0, 97) + "...");
//        }
        // todo getDoctor should return the text name of the character, not the numeral. getOtherCast should return only shortNames in the ListView. create a toString()?
        // todo getCrew should return the writer only in the listview.
		holder.tvstoryCastAndCrew.setText(TVStory.getDoctor() + ", " + TVStory.getOtherCast() + ", " + TVStory.getCrew());
		holder.tvstoryCastAndCrew.setVisibility(View.GONE); // not visible in main (all-items) list

        holder.seenIt.setChecked(TVStory.seenIt());
		holder.seenIt.setVisibility(View.GONE); // not visible in main (all-items) list
		holder.seenIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    holder.seenIt.setSeenIt(isChecked);
			}
		});
        holder.wantToSeeIt.setChecked(TVStory.seenIt());
		holder.wantToSeeIt.setVisibility(View.GONE); // not visible in main (all-items) list
        holder.wantToSeeIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    holder.seenIt.setSeenIt(isChecked);
			}
		});
        holder.userStarRating.setRating(2);
		holder.userStarRating.setVisibility(View.GONE); // not visible in main (all-items) list

		holder.doctorImage.setImageResource(doctorImages[TVStory.getDoctor()-1]);

// This gets the image for each TVStory.
		Resources res = getContext().getResources();
		TypedArray tvstoryImages = res.obtainTypedArray(R.array.tvstoryImages);
		Drawable drawable = tvstoryImages.getDrawable(TVStory.getStoryID()-1);
		holder.tvstoryImage.setImageDrawable(drawable);


        return convertView;
	}


}
