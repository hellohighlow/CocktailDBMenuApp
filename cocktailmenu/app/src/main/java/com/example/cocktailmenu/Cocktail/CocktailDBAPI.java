package com.example.cocktailmenu.Cocktail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class to handle API requests and interactions
 */
public class CocktailDBAPI {
  /**
   * A method to return the list of alcoholic drinks from the Cocktail DB JSON API
   * @return A List (ArrayList) which contains all the Cocktails which have been parsed
   */
  public List<Cocktail> getDrinkList(){
    try{
      ArrayList<Cocktail> cocktailList = new ArrayList<Cocktail>();
      //Handles the connection
      URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic");
      HttpURLConnection cocktailDB = (HttpURLConnection)url.openConnection();
      cocktailDB.setRequestMethod("GET");
      cocktailDB.connect();
      String menu = "";

      //This tests if the connection was successful
      int responseCode = cocktailDB.getResponseCode();
      if(responseCode != 200) {
        throw new RuntimeException("HTTP RESPONSE CODE " + responseCode);
      }else{
        Scanner scan = new Scanner(url.openStream());
        //Scans the API response and appends it all to a string
        while(scan.hasNext()){
          menu += scan.nextLine();
        }
        JSONObject jsonMenu = new JSONObject(menu);
        JSONArray menuArray = jsonMenu.getJSONArray("drinks");
        //A loop to loop through the JSONArray which is divided into seperate drinks
        for(int i = 0; i < menuArray.length(); i++){
          String[] cocktailString = menuArray.get(i).toString().split(",");
          Cocktail cocktail = new Cocktail();
          //A loop to loop through the String array which is the individual drink attributes
          for(int j = 0; j < cocktailString.length; j++){
            if(cocktailString[j].contains("\"strDrink\"")){
              String[] temp = cocktailString[j].split(":", 2);
              cocktail.setTitle(temp[1].substring(1,temp[1].length()-1));
            }
            if(cocktailString[j].contains("\"strDrinkThumb\"")){
              String[] temp = cocktailString[j].split(":", 2);
              String picUrl = temp[1].substring(1,temp[1].length()-1).replace("\\", "");
              cocktail.setPictureURL(picUrl);
              cocktail.setPicture(getPicture(cocktail.getPictureURL()));
            }
            if(cocktailString[j].contains("\"idDrink\"")){
              String[] temp = cocktailString[j].split(":", 2);
              cocktail.setId(temp[1].substring(1,temp[1].length()-2));
            }
          }
          cocktailList.add(cocktail);
        }
      }
      return cocktailList;
    }catch (MalformedURLException Exception){
      System.out.println("MalformedURLException in getDrinkList: " + Exception);
    }catch(IOException Exception){
      System.out.println("IOException in getDrinkList: " + Exception);
    }catch(JSONException Exception){
      System.out.println("JSONException in getDrinkList: " + Exception);
    }
    return new ArrayList<Cocktail>();
  }

  /**
   * A method to get the images from a url and returns it
   * @param imageUrl A string which points to the image to be downloaded
   * @return A Bitmap image from the internet
   * @throws IOException If the image is not found there may be an IOException. Handled by caller.
   */
  public Bitmap getPicture(String imageUrl) throws IOException {
    //Connects to the API to get the image
    URL url = new URL(imageUrl);
    InputStream is = url.openStream();
    Bitmap image = BitmapFactory.decodeStream(is);
    //Scales the image for use
    image = Bitmap.createScaledBitmap(image, (int)Math.floor((double)(image.getWidth() / 4)), (int)Math.floor((double)(image.getHeight() / 4)), true);
    return image;
  }

  /**
   * A Method to return the API response for a specific drink. The API returns more data than for the full list
   * @param id A String that is the id of the selected drink
   * @return A Cocktail object containing all information returned by the API for a given drink
   */
  public Cocktail getDrinkDetails(String id){
    Cocktail drink = new Cocktail();
    try{
      //Connects to the API
      URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i="+id);
      HttpURLConnection cocktailDB = (HttpURLConnection)url.openConnection();
      cocktailDB.setRequestMethod("GET");
      cocktailDB.connect();
      String details = "";

      //Tests to ensure the connection is good
      int responseCode = cocktailDB.getResponseCode();
      if(responseCode != 200) {
        throw new RuntimeException("HTTP RESPONSE CODE " + responseCode);
      } else {
        //Scans the Stream from the url
        Scanner scan = new Scanner(url.openStream());
        while(scan.hasNext()){
          //Appends the API responses to one string
          details += scan.nextLine();
        }
        JSONObject jsonDetails = new JSONObject(details);
        JSONArray detailArray = jsonDetails.getJSONArray("drinks");
        //A loop to loop through the JSONArray containing the drink details. Should only run once.
        for(int x = 0; x < detailArray.length(); x++){
          //Splits the drink details so that they can be parsed for individual detail extraction
          String[] detailString = detailArray.get(x).toString().split(",");
          Cocktail cocktail = new Cocktail();
          ArrayList<String> ingredients = new ArrayList<String>();
          ArrayList<String> quantities = new ArrayList<String>();
          //Loops through the list of details formated as "tag":"Data"
          for(int y = 0; y < detailString.length; y++){
            if(detailString[y].contains("\"idDrink\"")){
              String[] temp = detailString[y].split(":", 2);
              cocktail.setId(temp[1].substring(1, temp[1].length()-1));
            }
            if(detailString[y].contains("\"strDrink\"")){
              String[] temp = detailString[y].split(":", 2);
              cocktail.setTitle(temp[1].substring(1, temp[1].length()-1));
            }
            if(detailString[y].contains("\"strCategory\"")){
              String[] temp = detailString[y].split(":", 2);
              cocktail.setCategory(temp[1].substring(1, temp[1].length()-1));
            }
            if(detailString[y].contains("\"strAlcoholic\"")){
              String[] temp = detailString[y].split(":", 2);
              cocktail.setIsAlcoholic((temp[1].substring(1, temp[1].length()-1).equals("Alcoholic")));
            }
            if(detailString[y].contains("\"strGlass\"")){
              String[] temp = detailString[y].split(":", 2);
              cocktail.setGlassType(temp[1].substring(1, temp[1].length()-1));
            }
            if(detailString[y].contains("\"strInstructions\"")){
              String[] temp = detailString[y].split(":", 2);
              cocktail.setInstructions(temp[1].substring(1, temp[1].length()-1));
            }
            if(detailString[y].contains("\"strDrinkThumb\"")){
              String[] temp = detailString[y].split(":", 2);
              String picUrl = temp[1].substring(1,temp[1].length()-1).replace("\\", "");
              cocktail.setPictureURL(picUrl);
              cocktail.setPicture(getPicture(cocktail.getPictureURL()));
            }
            if(detailString[y].contains("\"strIngredient")){
              String[] temp = detailString[y].split(":", 2);
              if(!temp[1].equals("null")) {
                System.out.println(temp[1]);
                ingredients.add(temp[1].substring(1, temp[1].length() - 1));
              }else {
                ingredients.add(null);
              }
            }
            if(detailString[y].contains("\"strMeasure")) {
              String[] temp = detailString[y].split(":", 2);
              if (temp[1] != null) {
                quantities.add(temp[1].substring(1, temp[1].length() - 1).replace("\\", ""));
              } else {
                quantities.add(null);
              }
            }
          }
          cocktail.setIngredients(ingredients);
          cocktail.setQuantities(quantities);
          return cocktail;
        }
      }
    }catch (MalformedURLException Exception){
      System.out.println("MalformedURLException in getDrinkDetails: " + Exception);
    }catch(IOException Exception){
      System.out.println("IOException in getDrinkDetails: " + Exception);
    }catch(JSONException Exception){
      System.out.println("JSONException in getDrinkDetails: " + Exception);
    }
    return drink;
  }
}


