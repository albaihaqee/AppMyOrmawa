package com.inovarka.myormawa.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.inovarka.myormawa.R;
import com.inovarka.myormawa.models.Organization;
import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder> {

    private List<Organization> organizations;
    private OnOrganizationClickListener listener;

    public interface OnOrganizationClickListener {
        void onOrganizationClick(Organization organization);
    }

    public OrganizationAdapter(List<Organization> organizations, OnOrganizationClickListener listener) {
        this.organizations = organizations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_organization, parent, false);
        return new OrganizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationViewHolder holder, int position) {
        Organization organization = organizations.get(position);
        holder.bind(organization);
    }

    @Override
    public int getItemCount() {
        return organizations.size();
    }

    public void updateOrganizations(List<Organization> newOrganizations) {
        this.organizations = newOrganizations;
        notifyDataSetChanged();
    }

    class OrganizationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgLogo;
        private final TextView txtName;
        private final TextView txtCategory;
        private final TextView txtMemberCount;
        private final TextView txtDescription;

        public OrganizationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.img_organization_logo);
            txtName = itemView.findViewById(R.id.txt_organization_name);
            txtCategory = itemView.findViewById(R.id.txt_organization_category);
            txtMemberCount = itemView.findViewById(R.id.txt_member_count);
            txtDescription = itemView.findViewById(R.id.txt_organization_description);
        }

        public void bind(Organization organization) {
            txtName.setText(organization.getName());
            txtCategory.setText(organization.getCategory());
            txtMemberCount.setText(organization.getMemberCount() + " anggota");
            txtDescription.setText(organization.getDescription());

            setCategoryStyle(organization.getCategory());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrganizationClick(organization);
                }
            });
        }

        private void setCategoryStyle(String category) {
            GradientDrawable background = new GradientDrawable();
            background.setCornerRadius(6 * itemView.getResources().getDisplayMetrics().density);

            String bgColor;
            String textColor;

            switch (category) {
                case "Lembaga":
                    bgColor = "#E5E5E5";
                    textColor = "#455A64";
                    break;
                case "Akademik":
                    bgColor = "#E8EEFF";
                    textColor = "#2C4EEF";
                    break;
                case "Rohani":
                    bgColor = "#A9FEAF";
                    textColor = "#388E3C";
                    break;
                case "Minat":
                    bgColor = "#FFECCE";
                    textColor = "#E65100";
                    break;
                case "Seni":
                    bgColor = "#FBDFFF";
                    textColor = "#7B1FA2";
                    break;
                case "Olahraga":
                    bgColor = "#FAD1D7";
                    textColor = "#C62828";
                    break;
                default:
                    bgColor = "#E8EEFF";
                    textColor = "#2C4EEF";
                    break;
            }

            background.setColor(Color.parseColor(bgColor));
            txtCategory.setBackground(background);
            txtCategory.setTextColor(Color.parseColor(textColor));
        }
    }
}