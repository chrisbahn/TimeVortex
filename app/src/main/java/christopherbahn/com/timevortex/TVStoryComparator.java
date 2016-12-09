package christopherbahn.com.timevortex;

import java.util.Comparator;

/**
 * Created by christopherbahn on 12/9/16.
 */

public class TVStoryComparator implements Comparator<TVStory> {

    public static final int COMPARE_BY_ID = 0;
    public static final int COMPARE_BY_BESTOFBBCAMERICA = 1;
    public static final int COMPARE_BY_BESTOFDWM2009 = 2;
    public static final int COMPARE_BY_BESTOFDWM2014 = 3;
    public static final int COMPARE_BY_BESTOFAVC10 = 4;
    public static final int COMPARE_BY_BESTOFIO9 = 5;
    public static final int COMPARE_BY_BESTOFLMMYLES = 6;
    public static final int COMPARE_BY_BESTOFBAHN = 7;

    private int compare_mode = COMPARE_BY_ID;

    public TVStoryComparator() {
    }

    public TVStoryComparator(int compare_mode) {
        this.compare_mode = compare_mode;
    }

    @Override
    public int compare(TVStory o1, TVStory o2) {
        switch (compare_mode) {
            case COMPARE_BY_BESTOFBBCAMERICA:
                return Integer.valueOf(o1.getBestOfBBCAmerica()).compareTo(Integer.valueOf(o2.getBestOfBBCAmerica()));
            case COMPARE_BY_BESTOFDWM2009:
                return Integer.valueOf(o1.getBestOfDWM2009()).compareTo(Integer.valueOf(o2.getBestOfDWM2009()));
            case COMPARE_BY_BESTOFDWM2014:
                return Integer.valueOf(o1.getBestOfDWM2014()).compareTo(Integer.valueOf(o2.getBestOfDWM2014()));
            case COMPARE_BY_BESTOFAVC10:
                return Integer.valueOf(o1.getBestOfAVCTVC10()).compareTo(Integer.valueOf(o2.getBestOfAVCTVC10()));
            case COMPARE_BY_BESTOFIO9:
                return Integer.valueOf(o1.getBestOfIo9()).compareTo(Integer.valueOf(o2.getBestOfIo9()));
            case COMPARE_BY_BESTOFLMMYLES:
                return Integer.valueOf(o1.getBestOfLMMyles()).compareTo(Integer.valueOf(o2.getBestOfLMMyles()));
            case COMPARE_BY_BESTOFBAHN:
                return Integer.valueOf(o1.getBestOfBahn()).compareTo(Integer.valueOf(o2.getBestOfBahn()));
            default:
                return Integer.valueOf(o1.getStoryID()).compareTo(Integer.valueOf(o2.getStoryID()));
        }
    }

}
