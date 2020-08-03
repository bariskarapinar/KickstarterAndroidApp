package com.bkara.poilabs.classes;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bkara.poilabs.R;

/**
 * Created by bkara on 29/7/2020.
 *
 *
 *
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.VH> {

    private Context context;
    private Cursor dataCursor;
    private ItemClickListener itemClickListener;

    public ProjectListAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.dataCursor = cursor;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.kick_starter_project_info, parent, false);
        return new VH(view);
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {

        dataCursor.moveToPosition(position);


        int title_index = dataCursor.getColumnIndex(ProjectDb.KickEntry.KICK_TITLE);
        int pledged_index = dataCursor.getColumnIndex(ProjectDb.KickEntry.KICK_AMT_PLEDGED);
        int backers_index = dataCursor.getColumnIndex(ProjectDb.KickEntry.KICK_BACKERS);
        int no_of_days_index = dataCursor.getColumnIndex(ProjectDb.KickEntry.KICK_END_TIME);  /**/


        holder.tvProjectName.setText(dataCursor.getString(title_index));
        holder.tvPleadge.setText(context.getString(R.string.pledged_amount) + dataCursor.getInt(pledged_index));
        holder.tvBackers.setText(context.getString(R.string.backers_label) + dataCursor.getString(backers_index));
        holder.tvNoOfDaysTOGo.setText(context.getString(R.string.noOfDaysToGoLabel) + dataCursor.getString(no_of_days_index));

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }


    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tvProjectName;
        TextView tvPleadge;
        TextView tvBackers;
        TextView tvNoOfDaysTOGo;
        LinearLayout mainLayout;

        VH(View itemView) {
            super(itemView);

            tvProjectName = (TextView) itemView.findViewById(R.id.projectName);
            tvPleadge = (TextView) itemView.findViewById(R.id.pleadge);
            tvBackers = (TextView) itemView.findViewById(R.id.backers);
            tvNoOfDaysTOGo = (TextView) itemView.findViewById(R.id.noOfDaysToGo);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.main_layout);

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dataCursor.moveToPosition(getAdapterPosition());

                    if (itemClickListener != null)
                        itemClickListener.itemClicked(dataCursor);
                }
            });

        }
    }

    public interface ItemClickListener {
        void itemClicked(Cursor dataCursor);
    }

}