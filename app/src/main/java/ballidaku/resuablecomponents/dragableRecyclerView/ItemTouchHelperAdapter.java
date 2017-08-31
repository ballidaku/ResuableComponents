package ballidaku.resuablecomponents.dragableRecyclerView;

/**
 * Created by brst-pc93 on 8/31/17.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}