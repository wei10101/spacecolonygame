package com.spacecolony.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.spacecolony.R;
import com.spacecolony.colony.CrewMember;
import com.spacecolony.ui.CrewDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {

    private final List<CrewMember> crewList;
    private OnCrewClickListener listener;

    private List<CrewMember> selectedList = new ArrayList<>();

    public interface OnCrewClickListener {
        void onClick(CrewMember crew);
    }

    public CrewAdapter(List<CrewMember> crewList, OnCrewClickListener listener) {
        this.crewList = crewList;
        this.listener = listener;
    }

    public List<CrewMember> getSelectedCrew() {
        return selectedList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRole, tvSkill, tvExp, tvEnergy;
        Button btnDetail;
        ImageView imgRole;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvSkill = itemView.findViewById(R.id.tvSkill);
            tvExp = itemView.findViewById(R.id.tvExp);
            tvEnergy = itemView.findViewById(R.id.tvEnergy);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            imgRole = itemView.findViewById(R.id.imgRole);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CrewMember crew = crewList.get(position);

        holder.tvName.setText(crew.getName());
        holder.tvRole.setText(crew.getSpecialization());
        holder.tvSkill.setText("Skill: " + crew.getSkillLevel());
        holder.tvExp.setText("Exp: " + crew.getExperience());
        holder.tvEnergy.setText("Energy: " + crew.getEnergy());

        String role = crew.getSpecialization();
        int resId;

        switch (role) {
            case "Pilot":
                resId = R.drawable.pilot;
                break;
            case "Engineer":
                resId = R.drawable.engineer;
                break;
            case "Medic":
                resId = R.drawable.medic;
                break;
            case "Scientist":
                resId = R.drawable.scientist;
                break;
            case "Soldier":
                resId = R.drawable.soldier;
                break;
            default:
                resId = R.drawable.pilot;
        }

        holder.imgRole.setImageResource(resId);

        if (selectedList.contains(crew)) {
            holder.itemView.setBackgroundResource(android.R.color.holo_blue_light);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.crew_item_bg);
        }

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            CrewMember clicked = crewList.get(pos);

            if (selectedList.contains(clicked)) {
                selectedList.remove(clicked);
            } else {

                if (selectedList.size() >= 3) {
                    Toast.makeText(v.getContext(), "Maximum 3 crew allowed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedList.add(clicked);
            }

            notifyDataSetChanged();

            if (listener != null) {
                listener.onClick(clicked);
            }
        });

        holder.btnDetail.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            Intent intent = new Intent(v.getContext(), CrewDetailActivity.class);
            intent.putExtra("crewId", crewList.get(pos).getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }
}