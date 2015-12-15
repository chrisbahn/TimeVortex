package christopherbahn.com.timevortex;

import android.app.Activity;
import android.content.Context;
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

// TODO COMPLETE CONVERSION TO TIMEVORTEX
// todo Can the ListFragment and SearchListFragment stuff be combined? They're doing a lot of the same work, and you have multiple kinds of searching planned

public class TVStoryListAdapter extends ArrayAdapter<TVStory> {

	private Context context;
	List<TVStory> TVStories;

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	public TVStoryListAdapter(Context context, List<TVStory> TVStories) {
		super(context, R.layout.list_item, TVStories);
		this.context = context;
		this.TVStories = TVStories;
	}

	private class ViewHolder {
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

			holder.tvstoryImage = (ImageView) convertView.findViewById(R.id.list_item_image);
			holder.tvstoryTitle = (TextView) convertView.findViewById(R.id.txt_listitem_title);
            holder.tvstorySeasonInfo = (TextView) convertView.findViewById(R.id.txt_listitem_seasoninfo);
            holder.tvstorySynopsis = (TextView) convertView.findViewById(R.id.txt_listitem_synopsis);
            holder.tvstoryCastAndCrew = (TextView) convertView.findViewById(R.id.txt_listitem_castandcrew);
            // todo do the checkboxes work as they should?
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
        holder.tvstorySeasonInfo.setText(TVStory.getYearProduced()  + ". " + TVStory.getSeason() + " #" + TVStory.getSeasonStoryNumber() + " (" + TVStory.getEra()  + " era). " + TVStory.getEpisodes() + " episodes. " + (TVStory.getEpisodes()*TVStory.getEpisodeLength()) + "minutes."); // era, yearProduced, season, seasonStoryNumber, episode, episodeLength
        holder.tvstorySynopsis.setText(TVStory.getSynopsis());
		holder.tvstorySynopsis.setVisibility(View.GONE); // not visible in main (all-items) list
        // todo might be good to limit the size of the synopsis, but they're all blank now anyway so this is for later
//        if (TVStory.getSynopsis().length() > 100) {
//            holder.tvstorySynopsis.setText(TVStory.getSynopsis().substring(0, 97) + "...");
//        }
        // todo getDoctor should return the text name of the character, not the numeral. getOtherCast should return only shortNames in the ListView. create a toString()?
        // todo getCrew should return the writer only in the listview.
        holder.tvstoryCastAndCrew.setText(TVStory.getDoctor() + ", " + TVStory.getOtherCast() + ", " + TVStory.getCrew());
        // todo make the checkboxes work
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
		// todo make the star rating work
        holder.userStarRating.setRating(2);
		holder.userStarRating.setVisibility(View.GONE); // not visible in main (all-items) list

		int whichDoctor = TVStory.getDoctor();
		whichDoctorIsIt(holder, whichDoctor);

        return convertView;
	}

	@Override
	public void add(TVStory TVStory) {
		TVStories.add(TVStory);
		notifyDataSetChanged();
		super.add(TVStory);
	}

	@Override
	public void remove(TVStory TVStory) {
		TVStories.remove(TVStory);
		notifyDataSetChanged();
		super.remove(TVStory);
	}

	// todo A kludgy and temporary way to change the image and cast info. Will be rewritten!
	private void whichDoctorIsIt(ViewHolder holder, int whichDoctor){
		if (whichDoctor==1) {
			holder.tvstoryCastAndCrew.setText("First Doctor (William Hartnell)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor01);
		}
		if (whichDoctor==2) {
			holder.tvstoryCastAndCrew.setText("Second Doctor (Patrick Troughton)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor02);
		}
		if (whichDoctor==3) {
			holder.tvstoryCastAndCrew.setText("Third Doctor (Jon Pertwee)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor03);
		}
		if (whichDoctor==4) {
			holder.tvstoryCastAndCrew.setText("Fourth Doctor (Tom Baker)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor04);
		}
		if (whichDoctor==5) {
			holder.tvstoryCastAndCrew.setText("Fifth Doctor (Peter Davison)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor05);
		}
		if (whichDoctor==6) {
			holder.tvstoryCastAndCrew.setText("Sixth Doctor (Colin Baker)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor06);
		}
		if (whichDoctor==7) {
			holder.tvstoryCastAndCrew.setText("Seventh Doctor (Sylvester McCoy)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor07);
		}
		if (whichDoctor==8) {
			holder.tvstoryCastAndCrew.setText("Eighth Doctor (Paul McGann)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor08);
		}
		if (whichDoctor==9) {
			holder.tvstoryCastAndCrew.setText("Ninth Doctor (Christopher Ecclestone)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor09);
		}
		if (whichDoctor==10) {
			holder.tvstoryCastAndCrew.setText("Tenth Doctor (David Tennant)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor10);
		}
		if (whichDoctor==11) {
			holder.tvstoryCastAndCrew.setText("Eleventh Doctor (Matt Smith)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor11);
		}
		if (whichDoctor==12) {
			holder.tvstoryCastAndCrew.setText("Twelfth Doctor (Peter Capaldi)");
			holder.tvstoryImage.setImageResource(R.drawable.logodoctor12);
		}
	}




}
