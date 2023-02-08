package nl.tschuller.portamed.ui.overview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.tschuller.portamed.Medicine;
import nl.tschuller.portamed.R;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView friendlyNameTextView;
        public TextView activeIngredientTextView;
        public TextView dosageFormTextView;
        public ImageView medicineOverviewPreviewImageImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            friendlyNameTextView = (TextView) itemView.findViewById(R.id.friendlyName);
            activeIngredientTextView = (TextView) itemView.findViewById(R.id.activeIngredient);
            dosageFormTextView = (TextView) itemView.findViewById(R.id.dosageForm);
            medicineOverviewPreviewImageImageView = (ImageView) itemView.findViewById(R.id.medicineOverviewPreviewImage);
        }
    }

    private List<Medicine> mMedicines;
    private View medicineView;

    public MedicineListAdapter(List<Medicine> medicines){
        mMedicines = medicines;
    }

    @NonNull
    @Override
    public MedicineListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        medicineView = inflater.inflate(R.layout.fragment_medicine_item, parent, false);

        return new ViewHolder(medicineView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = mMedicines.get(position);

        TextView friendlyNameView = holder.friendlyNameTextView;
        TextView activeIngredientView = holder.activeIngredientTextView;
        TextView dosageFormView = holder.dosageFormTextView;
        ImageView previewImage = holder.medicineOverviewPreviewImageImageView;

        friendlyNameView.setText(medicine.getFriendlyName());
        activeIngredientView.setText(medicine.getActiveIngredient());
        dosageFormView.setText(medicine.getDosageForm());
        previewImage.setImageBitmap(medicine.getBoxExample());

        int offwhite = ContextCompat.getColor(medicineView.getContext(), R.color.offwhite);
        int chambray = ContextCompat.getColor(medicineView.getContext(), R.color.chambray);
        if (position % 2 != 0) {
            medicineView.setBackgroundColor(offwhite);
            friendlyNameView.setTextColor(chambray);
            activeIngredientView.setTextColor(chambray);
            dosageFormView.setTextColor(chambray);
        }
    }

    @Override
    public int getItemCount() {
        return mMedicines.size();
    }
}
