package nl.tschuller.portamed.ui.add_medicine;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.util.ArrayList;

import nl.tschuller.portamed.MainActivity;
import nl.tschuller.portamed.Medicine;
import nl.tschuller.portamed.R;
import nl.tschuller.portamed.databinding.FragmentAddMedicineBinding;

public class AddMedicineFragment extends Fragment {

    private FragmentAddMedicineBinding binding;
    private ArrayList<Medicine> medicines;

    private EditText rvgInput;
    private EditText friendlyNameInput;
    private EditText brandInput;
    private EditText activeIngredientInput;
    private EditText dosageFormInput;
    private CharSequence[] options;

    private Button addMedicineButton;
    private Button addImageButton;
    private Bitmap selectedImage = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentAddMedicineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        medicines = ((MainActivity)getActivity()).getMedicines();

        assignEditTexts(root);

        options = new String[]{getString(R.string.take_photo), getString(R.string.pick_photo), getString(R.string.Cancel)};


        //react to changing input values (disable the now unused inputs)
        rvgInput.addTextChangedListener(rvgWatcher);
        activeIngredientInput.addTextChangedListener(manualWatcher);
        brandInput.addTextChangedListener(manualWatcher);
        dosageFormInput.addTextChangedListener(manualWatcher);


        return root;
    }

    /**
     * gets the elements from the xml view
     * @param view
     */
    private void assignEditTexts(View view){
        rvgInput = view.findViewById(R.id.rvg_number_input);
        friendlyNameInput = view.findViewById(R.id.friendly_name_input);
        brandInput = view.findViewById(R.id.brand_input);
        activeIngredientInput = view.findViewById(R.id.active_ingredient_input);
        dosageFormInput = view.findViewById(R.id.dosage_form_input);
        addMedicineButton = view.findViewById(R.id.add_medicine_button);
        addImageButton = view.findViewById(R.id.image_button);

        addMedicineButton.setOnClickListener(addButtonListener);
        addImageButton.setOnClickListener(imageButtonListener);
    }

    /**
     * Listen for and process the pressing of the add button on the add medicine page, adds the medicine to the arraylist
     */
    private final View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(getEditTextValue(friendlyNameInput).isEmpty()){
                AlertDialog.Builder noFriendlyNameAlertBuilder
                        = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.no_friendly_name_alert_title)
                        .setMessage(R.string.no_friendly_name_alert_message);

                noFriendlyNameAlertBuilder.show();
            }else {
                medicines.add(new Medicine(getEditTextValue(rvgInput).isEmpty() ? null : new BigInteger(getEditTextValue(rvgInput)),
                        getEditTextValue(friendlyNameInput),
                        getEditTextValue(activeIngredientInput),
                        getEditTextValue(brandInput),
                        null,
                        getEditTextValue(dosageFormInput),
                        selectedImage
                ));
                clearEditTexts();
                Toast medicineAddedToast = new Toast(getContext());
                medicineAddedToast.setText(R.string.medicine_added_toast);
                medicineAddedToast.show();
            }
        }
    };

    private final View.OnClickListener imageButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.image_dialog_title)
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i == 0){
                                //take with camera
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                            }else if(i == 1) {
                                //get from gallery
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
                            }else if(i == 2){
                                //cancel
                                dialogInterface.dismiss();
                            }
                        }
                    }).show();
        }


    };

    /**
     * Gets back the result from the image intent
     *
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = (Bitmap) data.getExtras().get("data");
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImageUri = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImageUri != null) {
                            Cursor cursor = getContext().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                selectedImage = BitmapFactory.decodeFile(picturePath);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private String getEditTextValue(EditText editText) {
     return editText.getText().toString();
    }

    private void clearEditTexts(){
        rvgInput.getText().clear();
        friendlyNameInput.getText().clear();
        brandInput.getText().clear();
        activeIngredientInput.getText().clear();
        dosageFormInput.getText().clear();
    }

    private final TextWatcher rvgWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for(EditText editText : new EditText[]{brandInput, activeIngredientInput, dosageFormInput}) {
                    editText.setEnabled(charSequence.toString().isEmpty());
                }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final TextWatcher manualWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean enabled = true;
            for(EditText editText : new EditText[]{brandInput, activeIngredientInput, dosageFormInput}) {
                if (!editText.getText().toString().isEmpty()) enabled = false;
            }
            rvgInput.setEnabled(enabled);
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