package za.ac.cput;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import za.ac.cput.domain.Objective;
import za.ac.cput.repository.impl.ObjectiveRepositoryImpl;
import za.ac.cput.repository.impl.StudentRepositoryImpl;
import za.ac.cput.utils.DBUtils;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView studentNameTextView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private String authenticatedUser;
    private StudentRepositoryImpl studentRepository;
    private String studentName;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        authenticatedUser = getIntent().getStringExtra(DBUtils.AUTHENTICATED_USER);
        studentRepository = new StudentRepositoryImpl(this);
        studentName = studentRepository.getCurrentStudentFirstName(authenticatedUser);
        replaceFragment(new HomeFragment());

        System.out.println(authenticatedUser);
        System.out.println(studentName);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.studentNameTextView);
        TextView currentDateTextView = headerView.findViewById(R.id.currentDateTextView);
        navUsername.setText("Hello " + studentName);

        LocalDate currentDate = LocalDate.now();
        currentDateTextView.setText(currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));

        toolbar = findViewById(R.id.materialToolBar);

        // open navigation drawer layout when clicking icon in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // handle item click in navigation view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);

                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {

                    case R.id.logoutNavMenu:
                        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                        break;

                    case R.id.donatePointsNavMenu:
                        replaceFragment(new DonatePointsFragment());
                        break;

                    case R.id.viewPointsNavMenu:
                        //replaceFragment(new ViewPointsFragment());
                        break;

                    case R.id.viewPointHistoryNavMenu:
                        replaceFragment(new PointsHistoryFragment());
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }


}