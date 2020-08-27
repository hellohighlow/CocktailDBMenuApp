package com.example.cocktailmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocktailmenu.Cocktail.Cocktail;
import com.example.cocktailmenu.Cocktail.CocktailDBAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which manages the main activity / the DataTable
 */
public class MainActivity extends AppCompatActivity {

  private TableLayout tableLayout;
  private static ArrayList<Cocktail> cocktails;

  /**
   * An overridden function to handle the creation of the activity
   * @param savedInstanceState A Bundle containing saved state data
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    cocktails = new ArrayList<Cocktail>();
    initView();
    loadData();
  }

  /**
   * A method to initialize the Table Layout
   */
  private void initView(){
    tableLayout = (TableLayout) findViewById(R.id.tableLayoutCocktail);
  }

  /**
   * A method to run a thread which receives data from the api to be loaded into the dataTable
   */
  private void loadData(){
    new Thread(new Runnable() {
      /**
       * An overridden function to call the api and pass the data as a message to the main thread
       */
      @Override
      public void run() {
        List<Cocktail> Cocktails = new CocktailDBAPI().getDrinkList();
        Message msg = handler.obtainMessage();
        msg.obj = Cocktails;
        handler.sendMessage(msg);

      }
    }).start();


    createColumns();
    fillData(cocktails);
  }

  /**
   * A method which formats the column headers for the DataTable
   */
  private void createColumns(){
    TableRow tableRow = new TableRow(this);
    tableRow.setLayoutParams(new TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
    ));

    //Drink Image column
    TextView textViewPic = new TextView(this);
    textViewPic.setText("Picture");
    textViewPic.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewPic.setPadding(5,5,5,0);
    tableRow.addView(textViewPic);

    //Drink ID column
    TextView textViewId = new TextView(this);
    textViewId.setText("ID");
    textViewId.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewId.setPadding(5,5,5,0);
    tableRow.addView(textViewId);

    //Drink Name column
    TextView textViewName = new TextView(this);
    textViewName.setText("Name");
    textViewName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewName.setPadding(5,5,5,0);
    tableRow.addView(textViewName);

    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT
    ));

    //Add divider
    tableRow = new TableRow(this);
    tableRow.setLayoutParams(new TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT));

    //Pic column
    textViewPic = new TextView(this);
    textViewPic.setText("-----------");
    textViewPic.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewPic.setPadding(5,5,5,0);
    tableRow.addView(textViewPic);

    //ID column
    textViewId = new TextView(this);
    textViewId.setText("-----------");
    textViewId.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewId.setPadding(5,5,5,0);
    tableRow.addView(textViewId);

    //Name column
    textViewName = new TextView(this);
    textViewName.setText("-----------");
    textViewName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewName.setPadding(5,5,5,0);
    tableRow.addView(textViewName);

    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT
    ));

  }

  /**
   * A method to fill the DataTable with the data provided
   * @param cocktails A List<Cocktail> (ArrayList<Cocktail>) of Cocktails to be displayed on the DataTable
   */
  private void fillData(List<Cocktail> cocktails){
    //Format Table Rows and Click listeners
    //Loops through the Cocktails passed the the Main thread and displays them in rows
    for(Cocktail cocktail : cocktails){
      TableRow tableRow = new TableRow(this);
      tableRow.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.MATCH_PARENT,
          TableRow.LayoutParams.WRAP_CONTENT
      ));
      tableRow.setOnClickListener(new View.OnClickListener() {
        /**
         * An overridden method which handles the users click event and initiates the Detail View Activity
         * @param view A View which contains data about the current activity
         */
        @Override
        public void onClick(View view) {
          TableRow currentRow = (TableRow) view;
          TextView textViewId = (TextView) currentRow.getChildAt(1);
          String id = textViewId.getText().toString();
          //Creates a bundle to pass the data to the Detail View Activity
          Bundle bundle = new Bundle();
          bundle.putString("id", id);
          Intent intent = new Intent(view.getContext(), DrinkDetailView.class);
          intent.putExtras(bundle);
          //Spawns the new Activity
          startActivity(intent);

        }
      });

      //Add Photo column
      ImageView imageViewPicture = new ImageView(this);
      imageViewPicture.setImageBitmap(cocktail.getPicture());
      tableRow.addView(imageViewPicture);

      //Add ID column
      TextView textViewId = new TextView(this);
      textViewId.setText(cocktail.getId());
      textViewId.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
      textViewId.setPadding(5,5,5,0);
      tableRow.addView(textViewId);

      //Add Name column
      TextView textViewName = new TextView(this);
      textViewName.setText(cocktail.getTitle());
      textViewName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
      textViewName.setPadding(5,5,5,0);
      tableRow.addView(textViewName);

      tableLayout.addView(tableRow, new TableLayout.LayoutParams(
          TableLayout.LayoutParams.MATCH_PARENT,
          TableLayout.LayoutParams.WRAP_CONTENT
      ));
    }
  }

  /**
   * A Handler to pass messages between the main and sub threads
   */
  Handler handler = new Handler(){
    /**
     * An overridden method from the handler class which launches the data population upon receiving the list of cocktails from the sub thread
     * @param  msg A Message containing the List of Cocktails received from the API
     */
    @Override
    public void handleMessage(@NonNull Message msg) {
      if(msg.obj != null){
        fillData((ArrayList<Cocktail>)msg.obj);
      }
      super.handleMessage(msg);
    }
  };
}