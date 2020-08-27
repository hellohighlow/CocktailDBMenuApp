package com.example.cocktailmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.cocktailmenu.Cocktail.Cocktail;
import com.example.cocktailmenu.Cocktail.CocktailDBAPI;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity which controls the detail view of the app
 */
public class DrinkDetailView extends AppCompatActivity {

  String id;
  TableLayout tableLayout;
  Cocktail cocktail;

  /**
   * Overridden method from AppCompatActivity, runs when activity is started
   * @param savedInstanceState A Bundle containing information passed through state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_drink_detail_view);
    Bundle bundle = getIntent().getExtras();
    //Gets ID from previous activity
    id = bundle.getString("id");
    cocktail = new Cocktail();
    initView();
    loadData();
  }

  /**
   * A method to initialize the TableLayout object
   */
  public void initView(){
    tableLayout = (TableLayout) findViewById(R.id.tableLayoutDetail);
  }

  /**
   * A method which runs a thread to get the data from the API, and then passes a message to the main thread to display the data
   */
  public void loadData(){
    new Thread(new Runnable() {
      /**
       * An overridden method which runs the thread when .start() is called
       */
      @Override
      public void run() {
        Cocktail cocktail = new CocktailDBAPI().getDrinkDetails(id);
        Message msg = new Message();
        msg.obj = cocktail;
        //Passes a message to the app front end containing the cocktail details from the api
        handler.sendMessage(msg);
      }
    }).start();
  }

  /**
   * A method to display the cocktail details which are passed to the app front end
   * @param cocktail A Cocktail containing all information received from the API
   */
  public void fillData(Cocktail cocktail){
    //Add Image to top of page
    TableRow tableRow = new TableRow(this);
    tableRow.setLayoutParams(new TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
    ));

    ImageView imageViewPicture = new ImageView(this);
    imageViewPicture.setImageBitmap(Bitmap.createScaledBitmap(cocktail.getPicture(), (int) Math.floor(cocktail.getPicture().getWidth() * 2), (int)Math.floor(cocktail.getPicture().getHeight() * 2), true));
    imageViewPicture.setPadding(5,5,5,0);
    tableRow.addView(imageViewPicture);

    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT
    ));

    //Add Title below picture
    tableRow = new TableRow(this);
    tableRow.setLayoutParams(new TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
    ));

    TextView textViewTitle = new TextView(this);
    textViewTitle.setText(cocktail.getTitle());
    textViewTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewTitle.setPadding(5,5,5,0);
    tableRow.addView(textViewTitle);

    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT
    ));

    //Add isAlcoholic below Title
    tableRow = new TableRow(this);
    tableRow.setLayoutParams(new TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
    ));

    TextView textViewAlcoholic = new TextView(this);
    textViewAlcoholic.setText((cocktail.getIsAlcoholic()) ? "Acoholic" : "Non-Alcoholic");
    textViewAlcoholic.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewAlcoholic.setPadding(5,5,5,0);
    tableRow.addView(textViewAlcoholic);

    //Add Category next to isAlcoholic
    TextView textViewCategory = new TextView(this);
    textViewCategory.setText(cocktail.getCategory());
    textViewCategory.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewCategory.setPadding(5,5,5,0);
    tableRow.addView(textViewCategory);

    //Add glassType next to Category
    TextView textViewGlass = new TextView(this);
    textViewGlass.setText(cocktail.getGlassType());
    textViewGlass.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewGlass.setPadding(5,5,5,0);
    tableRow.addView(textViewGlass);

    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT
    ));

    //Add Instructions below Alcoholic / Category / Glass Type row
    tableRow = new TableRow(this);
    tableRow.setLayoutParams(new TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
    ));

    TextView textViewInstructions = new TextView(this);
    textViewInstructions.setText(cocktail.getInstructions());
    textViewInstructions.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewInstructions.setPadding(5,5,5,0);
    tableRow.addView(textViewInstructions);

    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT
    ));

    //Add Ingredients and Quantities
    ArrayList<String> ingredients = (ArrayList<String>)cocktail.getIngredients();
    ArrayList<String> quantities = (ArrayList<String>)cocktail.getQuantities();

    for(int x = 0; x < ingredients.size(); x++) {
      if(ingredients.get(x) == null){
        break;
      }
      //Ingredients / Quantities x
      tableRow = new TableRow(this);
      tableRow.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.MATCH_PARENT,
          TableRow.LayoutParams.WRAP_CONTENT
      ));

      TextView textViewIngredients = new TextView(this);
      textViewIngredients.setText(quantities.get(x) + ingredients.get(x));
      textViewIngredients.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
      textViewIngredients.setPadding(5,5,5,0);
      tableRow.addView(textViewIngredients);

      tableLayout.addView(tableRow, new TableLayout.LayoutParams(
          TableLayout.LayoutParams.MATCH_PARENT,
          TableLayout.LayoutParams.WRAP_CONTENT
      ));
    }
  }

  /**
   * A Handler which provides the channel to pass the Cocktail data to the app Main thread
   */
  Handler handler = new Handler(){
    /**
     * An overridden function from the handler class, it receives messages from other threads
     * @param msg A Message containing information from a non-main thread
     */
    @Override
    public void handleMessage(@NonNull Message msg) {
      if(msg.obj != null){
        Cocktail cocktail = (Cocktail) msg.obj;
        fillData(cocktail);
      }
      super.handleMessage(msg);
    }
  };
}