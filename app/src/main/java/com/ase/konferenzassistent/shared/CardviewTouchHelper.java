package com.ase.konferenzassistent.shared;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.shared.interfaces.CardviewTouchHelperAdapter;
import com.ase.konferenzassistent.R;

import java.util.Objects;

//Tutorial: https://www.youtube.com/watch?v=uvzP8KTz4Fg
public class CardviewTouchHelper extends ItemTouchHelper.Callback {

    private final CardviewTouchHelperAdapter cAdapter;

    public CardviewTouchHelper(CardviewTouchHelperAdapter cAdapter) {
        this.cAdapter = cAdapter;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.heller_corona_blue));
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Objects.requireNonNull(viewHolder).itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.corona_blue));
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int drageFlags = 0;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(drageFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        cAdapter.onItemSwiped(viewHolder.getAdapterPosition());
    }
}
