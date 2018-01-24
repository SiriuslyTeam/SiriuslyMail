package edu.sirius.android.siriuslymail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import static edu.sirius.android.siriuslymail.PostService.SUCCESS_LOAD_MESSAGES;
import static edu.sirius.android.siriuslymail.PostService.SUCCESS_LOGIN;
import static edu.sirius.android.siriuslymail.PostServiceActions.GET_FOLDERS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    AlertDialog.Builder alertDialog;
    Context context;
    NavigationView navigationView;

    private final int IDD_CHANGE_ACCOUNT = 0;

    BroadcastReceiver foldersBroadcastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getExtras().getBoolean(SUCCESS_LOGIN);
            if (success) {
                final Menu menu = navigationView.getMenu();
                List<String> a = DataBaseHelperFolder.getInstance(MainActivity.this).getFolder(context, UsersManager.getInstance().getActiveUser().getEmail());
                for (String i : a) {
                    menu.add(i);
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = MessagesFragment.newInstance("INBOX");
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            } else {
                Toast toast = Toast.makeText(context, "can not load", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(foldersBroadcastReciever, new IntentFilter(GET_FOLDERS));
        PostServiceActions.getFolders(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(foldersBroadcastReciever);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_change) {
            showDialog(IDD_CHANGE_ACCOUNT);
        } else if (id == R.id.nav_logout) {
            logout();
        } else {
            Fragment fragment = MessagesFragment.newInstance(item.getTitle().toString());
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            item.setChecked(true);
//        setTitle(menuItem.getTitle());
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void logout() {
        //todo db logout
        UsersManager.getInstance().clear();
        finish();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case IDD_CHANGE_ACCOUNT:
                List<String> strings = UsersManager.getInstance().getUsersEmail();
                strings.add("Add another account");
                final String[] mAccounts =  strings.toArray(new String[strings.size()]);
                int idAccountNow = 0;
                for (int i = 0; i < mAccounts.length; ++i) {
                    if (Objects.equals(mAccounts[i], UsersManager.getInstance().getActiveUser().getEmail())) {
                        idAccountNow = i;
                    }
                }
                final int[] idChosen = new int[1];
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final int finalIdAccountNow = idAccountNow;
                builder.setTitle("Select account")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (idChosen[0] == mAccounts.length - 1) {
                                            finish();
                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        } else if (idChosen[0] != finalIdAccountNow) {
                                            //TODO add all information about new account
                                            finish();
                                            UsersManager.getInstance().updateActiveUser(UsersManager.getInstance().getUsersEmail().get(idChosen[0]));
                                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setSingleChoiceItems(mAccounts, -1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        idChosen[0] = item;
                                    }
                                });
                builder.setCancelable(false);
                return builder.create();
            default:
                return null;
        }
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }
}
