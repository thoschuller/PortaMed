package nl.tschuller.portamed;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import nl.tschuller.portamed.databinding.ActivityMainBinding;
import nl.tschuller.portamed.ui.add_medicine.AddMedicineFragment;
import nl.tschuller.portamed.ui.overview.OverviewFragment;

/**
 * Small application for keeping track of medicines
 *
 * @author Thomas Schuller
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Medicine> medicines = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_overview, R.id.navigation_add_medicine, R.id.navigation_reminders)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    /**
     * Main arraylist used for storing the added medicines during runtime
     * @return the main arraylist-object
     */
    public ArrayList<Medicine> getMedicines(){return medicines;}

}