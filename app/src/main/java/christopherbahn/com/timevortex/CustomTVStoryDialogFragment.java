package christopherbahn.com.timevortex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

// TODO COMPLETE CONVERSION TO TIMEVORTEX
public class CustomTVStoryDialogFragment extends DialogFragment {

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
	// TODO aggregate star rating
    private TextView TVMyNotesTitle;
    private EditText EdtxtMyNotes;
    private TextView TVTextfield;
    private EditText EdtxtEnterTextfield;
    private TextView TVHashtags;
    private EditText EdtxtEnterHashtags;
    private TextView TVDatecreated;
    private TextView TVHasPhoto;
    private TextView TVId;

    private TVStory TVStory;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	
	TVStoryDAO tvstoryDAO;
	
	public static final String ARG_ITEM_ID = "tvstory_dialog_fragment";

	public interface TVStoryDialogFragmentListener {
		void onFinishDialog();
	}

	public CustomTVStoryDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        tvstoryDAO = new TVStoryDAO(getActivity());
		Bundle bundle = this.getArguments();
        TVStory = bundle.getParcelable("selectedTVStory");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View customDialogView = inflater.inflate(R.layout.fragment_fullpage_tvstory, null);
		builder.setView(customDialogView);

		tvstoryTitle = (TextView) customDialogView.findViewById(R.id.txt_fullpagetvstory_title);
		tvstorySeasonInfo = (TextView) customDialogView.findViewById(R.id.txt_fullpagetvstory_seasoninfo);
		tvstorySynopsis = (TextView) customDialogView.findViewById(R.id.txt_fullpagetvstory_synopsis);
		tvstoryCastAndCrew = (TextView) customDialogView.findViewById(R.id.txt_fullpagetvstory_castandcrew);
		seenIt = (CheckBox) customDialogView.findViewById(R.id.checkBox_listitem_seenit);
		wantToSeeIt = (CheckBox) customDialogView.findViewById(R.id.checkBox_listitem_wanttoseeit);
		TVuserStarRatingTitle = (TextView) customDialogView.findViewById(R.id.textview_myrating);
		userStarRating = (RatingBar) customDialogView.findViewById(R.id.UserStarRatingBar);
		TVMyNotesTitle = (TextView) customDialogView.findViewById(R.id.textview_mynotes_title);
        EdtxtMyNotes = (EditText) customDialogView.findViewById(R.id.mynotes_edittext);


		submitLayout = (LinearLayout) customDialogView.findViewById(R.id.save_buttons_layout);
		submitLayout.setVisibility(View.GONE);

		setValue();

		builder.setTitle(R.string.update_note);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.update,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

//                        TVStory.setTitle(EdtxtEnterTitle.getText().toString());
//                        TVStory.setTextField(EdtxtEnterTextfield.getText().toString());
//                        TVStory.setHashtags(EdtxtEnterHashtags.getText().toString());


                        long result = tvstoryDAO.update(TVStory);
                        if (result > 0) {
                            MainActivity activity = (MainActivity) getActivity();
                            activity.onFinishDialog();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Unable to update TVStory",
                                    Toast.LENGTH_SHORT).show();
                        }



										}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = builder.create();

		return alertDialog;
	}

	private void setValue() {
		if (TVStory != null) {
			// todo this is what you use to set the values seen on this page to match the TVStory being called
//            EdtxtEnterTitle.setText(TVStory.getTitle());
//            EdtxtEnterTextfield.setText(TVStory.getTextField());
//            EdtxtEnterHashtags.setText(TVStory.getHashtags());
//            TVDatecreated.setText("Date created: " + formatter.format(TVStory.getDateCreated()));
//            if (TVStory.hasPhoto()){
//                TVHasPhoto.setText("This TVStory has an attached image. Functionality TBA.");
//            } else {
//                TVHasPhoto.setText("No photo associated with this TVStory.");
//            }
//            TVId.setText("ID: " + TVStory.getId());
        }


	}
}
