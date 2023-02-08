package nl.tschuller.portamed.ui.overview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import nl.tschuller.portamed.MainActivity;
import nl.tschuller.portamed.Medicine;
import nl.tschuller.portamed.R;
import nl.tschuller.portamed.databinding.FragmentOverviewBinding;

/**
 * main class for the overview tab, binds recycler to its adapter
 */
public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;
    private RecyclerView rvMedicines;
    public ArrayList<Medicine> medicines;
    View v;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        rvMedicines = (RecyclerView) binding.getRoot().findViewById(R.id.medicineListRecyclerView);

        medicines = ((MainActivity)getActivity()).getMedicines();

        MedicineListAdapter adapter = new MedicineListAdapter(medicines);
        rvMedicines.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMedicines.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}