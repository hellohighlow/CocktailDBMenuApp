package com.example.cocktailmenu.Cocktail;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

/**
 * A class built to represent a cocktail
 */
public class Cocktail {
  private String id;
  private String title;
  private String pictureURL;
  private Bitmap picture;
  private String category;
  private boolean isAlcoholic;
  private String glassType;
  private String instructions;
  private List<String> ingredients;
  private List<String> quantities;

  public String getId(){
    return id;
  }
  public String getTitle(){
    return title;
  }
  public String getPictureURL(){
    return pictureURL;
  }
  public Bitmap getPicture(){ return picture; }
  public String getCategory(){ return category; }
  public boolean getIsAlcoholic(){ return isAlcoholic; }
  public String getGlassType(){ return glassType; }
  public String getInstructions(){ return instructions; }
  public List<String> getIngredients(){ return ingredients; }
  public List<String> getQuantities(){ return quantities; }

  public void setId(String id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPictureURL(String pictureURL) {
    this.pictureURL = pictureURL;
  }

  public void setPicture(Bitmap picture){
    this.picture = picture;
  }

  public void setCategory(String category){
    this.category = category;
  }

  public void setIsAlcoholic(boolean isAlcoholic){
    this.isAlcoholic = isAlcoholic;
  }

  public void setGlassType(String glassType) {
    this.glassType = glassType;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public void setIngredients(List<String> ingredients) {
    this.ingredients.clear();
    this.ingredients.addAll(ingredients);
  }

  public void setQuantities(List<String> quantities){
    this.quantities.clear();
    this.quantities.addAll(quantities);
  }

  public Cocktail(){
    this.ingredients = new ArrayList<String>();
    this.quantities = new ArrayList<String>();
  }
  public Cocktail(String id, String title, String pictureURL, Bitmap picture){
    this.id = id;
    this.title = title;
    this.pictureURL = pictureURL;
    this.picture = picture;
    this.ingredients = new ArrayList<String>();
    this.quantities = new ArrayList<String>();
  }
  @Override
  public String toString(){
    return "ID: " + id + " Title: " + title + " PictureURL: " + pictureURL;
  }
}
