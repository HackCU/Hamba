package com.hamba.hambameet;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBarDrawerToggle;

import com.hamba.hambameet.DBAdapter;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    DBAdapter gradeDB;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String current_semester;
    ImageButton runCommand;
    int current_semester_int;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean firstRun = false;
        if(!notFirstRun()){
            current_semester_int =0;
            current_semester = "All Semesters";
            firstRun = true;
        }
        else{

        }
        onCreateToolbar();
        floatingButtonSetup();
        openDB();
        populateList(current_semester_int);
        registerListClickCallback();
        calculateGPA();
        if(firstRun){
            help("Welcome to GPA Keeper!");
        }
    }

    private boolean notFirstRun() {
        //Checks to see if the application is being run the first time
        TinyDB tinydb = new TinyDB(this);
        boolean notFirstRun = tinydb.getBoolean("First run");
        if(!notFirstRun){
            tinydb.putBoolean("First run",true);
            tinydb.putInt("Current semester int", 0);
            tinydb.putString("Current semester","All Semesters");
        }
        else{
            current_semester = tinydb.getString("Current semester");
            current_semester_int = tinydb.getInt("Current semester int");
        }
        return notFirstRun;
    }
    private void onCreateToolbar() {//Creates toolbar
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mDrawerList = (ListView)findViewById(android.R.id.list);
        final TinyDB tinydb = new TinyDB(this);
        current_semester = tinydb.getString("Current semester");
        current_semester_int = tinydb.getInt("Current semester int");
        toolbar.setTitle(current_semester);
        final ArrayList semesters_list_display = tinydb.getList("Semesters display");
        ArrayList<String> menu_list = new ArrayList<String>();
        menu_list.add("Add Semester +");
        menu_list.add("All Semesters");
        menu_list.addAll(semesters_list_display);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_list));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    createSemesterDialog(false,0,"",0,"");
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                else if(position == 1){
                    current_semester = (String) parent.getItemAtPosition(position);
                    current_semester_int = 0;
                    tinydb.putInt("Current semester int", current_semester_int);
                    tinydb.putString("Current semester",current_semester);
                    toolbar.setTitle(current_semester);
                    mDrawerLayout.closeDrawer(mDrawerList);
                    populateList(current_semester_int);
                    calculateGPA();
                }
                else {
                    current_semester = (String) parent.getItemAtPosition(position);
                    ArrayList semesters_list = tinydb.getListInt("Semesters");
                    int current = semesters_list_display.indexOf(current_semester);
                    current_semester_int = (int)semesters_list.get(current);
                    tinydb.putInt("Current semester int", current_semester_int);
                    tinydb.putString("Current semester",current_semester);
                    toolbar.setTitle(current_semester);
                    mDrawerLayout.closeDrawer(mDrawerList);
                    populateList(current_semester_int);
                    calculateGPA();
                }
            }
        });
        mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>1) {
                    current_semester = (String) parent.getItemAtPosition(position);
                    updateSemesterDialog(current_semester, position);
                }
                return false;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
                invalidateOptionsMenu();
                syncState();
            }
            public void onDrawerOpened(View v){
                super.onDrawerOpened(v);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    private void floatingButtonSetup() {
        runCommand = (ImageButton)findViewById(R.id.runCommand);
        runCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EventActivity.class));
            }
        });
    }



    private void createSemesterDialog(final boolean check_class,final long idDB,final String name,final float hours,final String classGrade){
        final TinyDB tinydb = new TinyDB(this);
        LayoutInflater inflater=this.getLayoutInflater();
        final AlertDialog.Builder semester = new AlertDialog.Builder(this);
        final View semester_view = inflater.inflate(R.layout.semester_dialog, null);
        semester.setTitle("Add a Semester");
        semester.setView(semester_view);
        final EditText editSemester = (EditText) semester_view.findViewById(R.id.editSemester);
        semester.setTitle("Add a Semester");
        semester.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String semester_name = editSemester.getText().toString();
                int semester_count = tinydb.getInt("Semester count");
                ArrayList semester_list = tinydb.getListInt("Semesters");
                ArrayList semester_list_display = tinydb.getList("Semesters display");
                semester_list_display.add(semester_name);
                semester_list.add(semester_count);
                tinydb.putListInt("Semesters", semester_list);
                tinydb.putList("Semesters display", semester_list_display);
                tinydb.putInt("Semester count", (semester_count + 1));
                tinydb.putInt("Current semester int", semester_count);
                tinydb.putString("Current semester",semester_name);
                calculateGPA();
                onCreateToolbar();
                if (!name.equals("")) {
                    gradeDB.updateRow(idDB, name, hours, classGrade, semester_count);
                    populateList(current_semester_int);
                    Toast.makeText(getApplicationContext(),"Class updated",Toast.LENGTH_SHORT).show();
                }
                if(check_class && name.equals("")){
                    Toast.makeText(getApplicationContext(),"Class added",Toast.LENGTH_SHORT).show();
                    populateList(current_semester_int);
                }
                if(!check_class){
                    Toast.makeText(getApplicationContext(),"Semester added",Toast.LENGTH_SHORT).show();
                    populateList(current_semester_int);
                    createClassDialog();
                }

            }
        });
        semester.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(check_class && name.equals("")){
                    int semester_count = tinydb.getInt("Semester count");
                    gradeDB.deleteSemester(semester_count);
                    Toast.makeText(getApplicationContext(),"Class not added",Toast.LENGTH_SHORT).show();
                }
                if(!name.equals("")){
                    Toast.makeText(getApplicationContext(),"Class not updated",Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog Semester = semester.create();
        Semester.show();
        onCreateToolbar();
        populateList(current_semester_int);
        calculateGPA();
    }

    private void updateSemesterDialog(final String semester_name, final int position){
        final TinyDB tinydb = new TinyDB(this);
        LayoutInflater inflater=this.getLayoutInflater();
        final AlertDialog.Builder semester = new AlertDialog.Builder(this);
        final View semester_view = inflater.inflate(R.layout.semester_dialog,null);
        semester.setTitle("Add a Semester");
        semester.setView(semester_view);
        final EditText editSemester = (EditText) semester_view.findViewById(R.id.editSemester);
        semester.setTitle("Add a semester");
        editSemester.setText(semester_name);
        semester.setTitle("Update Semester");
        semester.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String semester_name = editSemester.getText().toString();
                ArrayList semester_list_display = tinydb.getList("Semesters display");
                semester_list_display.set(position-2,semester_name);
                tinydb.putList("Semesters display", semester_list_display);
                tinydb.putString("Current semester",semester_name);
                onCreateToolbar();
            }
        });
        semester.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList semester_list = tinydb.getListInt("Semesters");
                ArrayList semester_list_display = tinydb.getList("Semesters display");
                int semester_int = semester_list_display.indexOf(semester_name);
                int semester_delete = (int)semester_list.get(semester_int);
                gradeDB.deleteSemester(semester_delete);
                semester_list_display.remove(semester_name);
                semester_list.remove(semester_int);
                tinydb.putList("Semesters display", semester_list_display);
                tinydb.putListInt("Semesters", semester_list);
                Toast.makeText(getApplicationContext(),"Semester deleted",Toast.LENGTH_SHORT).show();
                tinydb.putInt("Current semester int", 0);
                tinydb.putString("Current semester","All Semesters");
                onCreateToolbar();
                populateList(current_semester_int);
                calculateGPA();
            }
        });

        semester.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog Semester = semester.create();
        Semester.show();
        onCreateToolbar();
        populateList(current_semester_int);
        calculateGPA();
    }

    //Toolbar Stuff
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    private void help(final String title) {
        final AlertDialog.Builder help;
        help = new AlertDialog.Builder(this);
        help.setTitle(title);
        final TinyDB tinydb = new TinyDB(this);
        if(title.equals("Welcome to GPA Keeper!")) {
            help.setMessage("Lets begin!\n" +
                    "Click Ok to add your first semester and class\n"+
                    "PS: If your ever looking for help look in the options menu");
            ArrayList<Integer> semesters_list = new ArrayList<>();
            ArrayList<String> semesters_list_display = new ArrayList<>();
            tinydb.putInt("Semester count", 1);
            tinydb.putListInt("Semesters",semesters_list);
            tinydb.putList("Semesters display", semesters_list_display);
        }
        else if(title.equals("Help")){
            help.setMessage("Add a class by clicking on the red button towards the bottom right\n" +
                    "Edit or delete a current class by clicking on it\n" +
                    "Add a semester from the left navigation menu or while adding/updating a class\n"+
                    "Edit or delete a semester by long pressing on it from the left navigation menu");
        }
        help.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(title.equals("Welcome to GPA Keeper!")) {
                    createSemesterDialog(false,0,"",0,"");
                }
            }
        });
        AlertDialog Help = help.create();
        Help.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        gradeDB = new DBAdapter(this);
        gradeDB.open();
    }

    private void closeDB() {
        gradeDB.close();
    }
    private void registerListClickCallback() {
        ListView classes_list = (ListView) findViewById(R.id.listClasses);
        classes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Cursor cursor = gradeDB.getRow(id);
                if(cursor.moveToFirst()){
                    long idDB = cursor.getLong(DBAdapter.COL_ROWID);
                    String name = cursor.getString(DBAdapter.COL_NAME);
                    float hours = cursor.getFloat(DBAdapter.COL_HOURS);
                    String grade = cursor.getString(DBAdapter.COL_GRADE);
                    int semester = cursor.getInt(DBAdapter.COL_SEMESTER);
                    updateClassDialog(cursor,idDB,name,hours,grade,semester);
                    calculateGPA();
                }
                cursor.close();
            }
        });
    }



    public void populateList(int semester) {
        Cursor cursor;
        if(semester == 0) {
            cursor = gradeDB.getAllRows();
        }
        else{
            cursor = gradeDB.searchSemester(semester);
        }
        //Set up mapping from cursor to list view
        //Allow activity to manage lifecycle of  the cursor
        //Deprecated because it runs on the UI thread, okay for small queries
        startManagingCursor(cursor);
        String[] fromFieldNames = new String[]{DBAdapter.KEY_NAME, DBAdapter.KEY_HOURS, DBAdapter.KEY_GRADE};
        int[] toViewIDs = new int[]{R.id.textName_ans, R.id.textHours_ans, R.id.textGrade_ans};
        //Adapter to make columns in the DB to rows in the list
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,//context
                R.layout.classes_list,//Row layout template
                cursor,//cursor(set of DB to map)
                fromFieldNames,//CB column names
                toViewIDs);//View ids to put information in
        //Set the adapter for the list view
        ListView classes_list = (ListView) findViewById(R.id.listClasses);
        classes_list.setAdapter(myCursorAdapter);
    }
    public void calculateGPA() {
        TextView GPA = (TextView) findViewById(R.id.textGPA);
        double C_GPA = 0;
        double S_GPA = 0;
        float total_hours = 0;
        float semester_hours = 0;
        Cursor cursor = gradeDB.getAllRows();
        if(cursor.moveToFirst()){
            do {
                float hours = cursor.getFloat(DBAdapter.COL_HOURS);
                total_hours += hours;
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        Cursor cursor2 = gradeDB.getAllRows();
        if(cursor2.moveToFirst()){
            do {
                float hours = cursor2.getFloat(DBAdapter.COL_HOURS);
                String grade = cursor2.getString(DBAdapter.COL_GRADE);
                C_GPA += (convertGrade(grade)*hours)/total_hours;
            }
            while (cursor2.moveToNext());
            cursor2.close();
        }
        Cursor cursor3 = gradeDB.searchSemester(current_semester_int);
        if(cursor3.moveToFirst()){
            do {
                float hours = cursor3.getFloat(DBAdapter.COL_HOURS);
                semester_hours += hours;
            }
            while (cursor3.moveToNext());
            cursor3.close();
        }
        Cursor cursor4 = gradeDB.searchSemester(current_semester_int);
        if(cursor4.moveToFirst()){
            do {
                float hours = cursor4.getFloat(DBAdapter.COL_HOURS);
                String grade = cursor4.getString(DBAdapter.COL_GRADE);
                S_GPA += (convertGrade(grade)*hours)/semester_hours;
            }
            while (cursor4.moveToNext());
            cursor4.close();
        }
        if(C_GPA!=0&&current_semester_int!=0){
            if(S_GPA!=0) {
                GPA.setText("Cumulative GPA: " + Double.toString(((double) Math.round(C_GPA * 1000)) / 1000) + "\nSemester GPA: " + Double.toString(((double) Math.round(S_GPA * 1000)) / 1000));
            }
            else{
                GPA.setText("Cumulative GPA: " + Double.toString(((double) Math.round(C_GPA * 1000)) / 1000));
            }
        }
        else if(C_GPA!=0){
            GPA.setText("Cumulative GPA: " + Double.toString(((double) Math.round(C_GPA * 1000)) / 1000));
        }
        else{
            GPA.setText("");
        }
    }

    public void createClassDialog(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        LayoutInflater inflater=this.getLayoutInflater();
        final AlertDialog.Builder new_class_dialog = new AlertDialog.Builder(this);
        final View new_class_dialog_view = inflater.inflate(R.layout.new_class_dialog,null);
        new_class_dialog.setView(new_class_dialog_view);
        new_class_dialog.setTitle("Add a class");
        final EditText n = (EditText) new_class_dialog_view.findViewById(R.id.editClass);
        final EditText h = (EditText) new_class_dialog_view.findViewById(R.id.editHours);
        final Spinner s = (Spinner)  new_class_dialog_view.findViewById(R.id.spinner_semester);
        final Spinner g = (Spinner) new_class_dialog_view.findViewById(R.id.spinner_grade);
        final TinyDB tinydb = new TinyDB(getApplicationContext());
        ArrayList spinner_list = new ArrayList();
        spinner_list.add("New semester");
        spinner_list.addAll(tinydb.getList("Semesters display"));
        s.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinner_list));
        if (current_semester_int !=0) {
            ArrayList semester_list_display = tinydb.getList("Semesters display");
            int semester_int = semester_list_display.indexOf(current_semester);
            s.setSelection(semester_int+1);
        }
        new_class_dialog.setPositiveButton("Add",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        new_class_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = new_class_dialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                String name = "";
                int semester_int = 0;
                float hours = 0;
                boolean DidItWork = true;
                String classGrade = "";
                if (n.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter all values", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        hours = Float.parseFloat(h.getText().toString());
                    } catch (Exception e) {
                        DidItWork = false;
                    } finally {
                        if (!DidItWork) {
                            Toast.makeText(getApplicationContext(), "Enter all values", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            classGrade = g.getSelectedItem().toString();
                            name = n.getText().toString();
                            ArrayList semester_list = tinydb.getListInt("Semesters");
                            int s_position = s.getSelectedItemPosition();
                            if (s_position == 0){
                                semester_int = tinydb.getInt("Semester count");
                                long newclass = gradeDB.insertRow(name, hours, classGrade, semester_int);
                                dialog.dismiss();
                                createSemesterDialog(true,0,"",0,"");
                                wantToCloseDialog = true;
                            }
                            else {
                                semester_int = (int) semester_list.get(s_position - 1);
                                long newclass = gradeDB.insertRow(name, hours, classGrade, semester_int);
                                Toast.makeText(getApplicationContext(), "Class Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                populateList(current_semester_int);
                                calculateGPA();
                                wantToCloseDialog = true;
                            }
                        }
                    }
                }
                if(wantToCloseDialog)
                    dialog.dismiss();
            }
        });
    }
    public void updateClassDialog(Cursor cursor,final long idDB,String name,float hours,String grade, final int semester) {
        LayoutInflater inflater=this.getLayoutInflater();
        final AlertDialog.Builder update_class_dialog = new AlertDialog.Builder(this);
        final View update_class_dialog_view = inflater.inflate(R.layout.update_class_dialog,null);
        update_class_dialog.setView(update_class_dialog_view);
        update_class_dialog.setTitle("Update class");
        final EditText n = (EditText) update_class_dialog_view.findViewById(R.id.editClass);
        final EditText h = (EditText) update_class_dialog_view.findViewById(R.id.editHours);
        Spinner g = (Spinner) update_class_dialog_view.findViewById(R.id.spinner_grade);
        final Spinner s = (Spinner) update_class_dialog_view.findViewById(R.id.spinner_semester);
        final TinyDB tinydb = new TinyDB(getApplicationContext());
        ArrayList spinner_list = new ArrayList();
        final ArrayList semester_list = tinydb.getListInt("Semesters");
        spinner_list.add("New semester");
        spinner_list.addAll(tinydb.getList("Semesters display"));
        s.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinner_list));
        int semester_int = semester_list.indexOf(semester);
        s.setSelection(semester_int+1);
        ArrayAdapter gAdap = (ArrayAdapter) g.getAdapter();
        int spinnerPosition = gAdap.getPosition(grade);
        n.setText(name);
        h.setText(Float.toString(hours));
        g.setSelection(spinnerPosition);
        update_class_dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        update_class_dialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gradeDB.deleteRow(idDB);
                Toast.makeText(getApplicationContext(), "Class deleted", Toast.LENGTH_SHORT).show();
                populateList(current_semester_int);
                calculateGPA();
            }
        });
        update_class_dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = update_class_dialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                String name = "";
                float hours = 0;
                boolean DidItWork = true;
                Spinner gi = (Spinner) update_class_dialog_view.findViewById(R.id.spinner_grade);
                String classGrade = "";
                int selected_semester = 0;
                if (n.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter all values", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        hours = Float.parseFloat(h.getText().toString());
                    } catch (Exception e) {
                        DidItWork = false;
                    } finally {
                        if (!DidItWork) {
                            Toast.makeText(getApplicationContext(), "Enter all values", Toast.LENGTH_SHORT).show();
                        } else {
                            classGrade = gi.getSelectedItem().toString();
                            name = n.getText().toString();
                            selected_semester = s.getSelectedItemPosition();
                            if (selected_semester == 0){
                                dialog.dismiss();
                                createSemesterDialog(true,idDB,name,hours,classGrade);
                                wantToCloseDialog = true;
                                calculateGPA();
                                populateList(current_semester_int);
                            }
                            else
                            {
                                int semester_int = (int) semester_list.get(selected_semester - 1);
                                gradeDB.updateRow(idDB, name, hours, classGrade, semester_int);
                                Toast.makeText(getApplicationContext(), "Class Updated", Toast.LENGTH_SHORT).show();
                                populateList(current_semester_int);
                                wantToCloseDialog = true;
                                calculateGPA();
                                populateList(current_semester_int);
                            }
                        }
                    }
                }
                if (wantToCloseDialog) {
                    dialog.dismiss();
                }
            }
        });
    }
    private double convertGrade(String textGrade) {
        double classGrade;
        switch (textGrade) {
            case "A":
                classGrade = 4;
                break;
            case "A-":
                classGrade = 3.7;
                break;
            case "B+":
                classGrade = 3.3;
                break;
            case "B":
                classGrade = 3;
                break;
            case "B-":
                classGrade = 2.7;
                break;
            case "C+":
                classGrade = 2.3;
                break;
            case "C":
                classGrade = 2;
                break;
            case "C-":
                classGrade = 1.7;
                break;
            case "D+":
                classGrade = 1.3;
                break;
            case "D":
                classGrade = 1;
                break;
            case "D-":
                classGrade = .7;
                break;
            default:
                classGrade = 0;
        }
        return classGrade;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_discard) {
            AlertDialog.Builder confirm_delete = new AlertDialog.Builder(this);
            confirm_delete.setTitle("Delete Everything");
            confirm_delete.setMessage("Are you sure you want to clear the database?\nALL entered classes and semesters will be deleted");
            confirm_delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gradeDB.deleteAll();
                    TinyDB tinydb = new TinyDB(getApplicationContext());
                    ArrayList semesters_list_display = new ArrayList();
                    ArrayList semesters_list = new ArrayList();
                    tinydb.putList("Semesters display", semesters_list_display);
                    tinydb.putListInt("Semesters", semesters_list);
                    dialog.dismiss();
                    populateList(0);
                    current_semester = "All Semesters";
                    current_semester_int = 0;
                    onCreateToolbar();
                    calculateGPA();
                }
            });
            confirm_delete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            confirm_delete.show();
        }
        if (id == R.id.about) {
            AlertDialog.Builder dialogBuilder;
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("About");
            dialogBuilder.setMessage("Made by: Rishab Kanwal\nCurrent release: Version 1.0\nRelease date: Jan 7th 2015");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog about = dialogBuilder.create();
            about.show();
            return true;
        }
        if (id == R.id.help) {
            help("Help");
        }

        //Toolbar Stuff
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
