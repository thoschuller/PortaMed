package nl.tschuller.portamed.ui.add_medicine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import nl.tschuller.portamed.R;
import nl.tschuller.portamed.databinding.FragmentAddMedicineBinding;

public class AddMedicineFragment extends Fragment {

    private FragmentAddMedicineBinding binding;

    private EditText rvgInput;
    private EditText friendlyNameInput;
    private EditText brandInput;
    private EditText activeIngredientInput;
    private EditText dosageFormInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentAddMedicineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        assignEditTexts(root);

        rvgInput.addTextChangedListener(rvgWatcher);


        return root;
    }

    private void assignEditTexts(View view){
        rvgInput = view.findViewById(R.id.rvg_number_input);
        friendlyNameInput = view.findViewById(R.id.friendly_name_input);
        brandInput = view.findViewById(R.id.brand_input);
        activeIngredientInput = view.findViewById(R.id.active_ingredient_input);
        dosageFormInput = view.findViewById(R.id.dosage_form_input);
    }

    private final TextWatcher rvgWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for(EditText editText : new EditText[]{brandInput, activeIngredientInput, dosageFormInput}) editText.setEnabled(charSequence.toString().isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}