package Filter

import scala.util.parsing.json._
import scalaj.http._
import ujson._
import scala.io._
import java.io._

import scala.util.control.Breaks._

import scala.swing._
import scala.swing.event._
import java.awt.Font.BOLD

import java.text.SimpleDateFormat
import java.util.Calendar
import java.time.format.DateTimeFormatter
import java.time.LocalDate

import org.junit.Assert.assertEquals

import scala.collection.mutable._

import Date.DateCheck


class RestaurantFilter(favArray: Buffer[String]) {
  
  // radio button for filtering the restaurant menu
  val allButton = new RadioButton("All restaurants") 
  val taffaButton = new RadioButton("Taffa (Otaniemi)"){
    // For initializing, the color of the button labels will be determined
    // depending on the favorite restaurant name included in the file
    if (favArray.contains("Taffa")){
      foreground = new Color (176, 62, 62)
      
    } else {
      foreground = new Color (0, 0, 0)
    }
  }
  
  val ravioliButton = new RadioButton("Ravioli (Meilahti)"){
    if (favArray.contains("Ravioli")){
      foreground = new Color (176, 62, 62)
    } else {
      foreground = new Color (0, 0, 0)
    }
  }
  
  val chemicumButton = new RadioButton("Chemicum (Kumpula)"){
    if (favArray.contains("Chemicum")){
      foreground = new Color (176, 62, 62)
    } else {
      foreground = new Color (0, 0, 0)
    }
  }
  
  val buttonGroup = new ButtonGroup() {
    buttons += allButton
    if (favArray.contains("Taffa") || favArray.contains("Ravioli") || favArray.contains("Chemicum")){
      allButton.selected = false
    } else {
      allButton.selected = true
    }
    buttons += taffaButton
    buttons += ravioliButton
    buttons += chemicumButton
  }
  
  val setFavoriteButton = new Button("Set / Unset as favorite") 
  
  val menuUI = new BoxPanel(Orientation.Horizontal){
    contents += allButton
    contents += Swing.HStrut(5)
    contents += taffaButton
    contents += Swing.HStrut(5)
    contents += ravioliButton
    contents += Swing.HStrut(5)
    contents += chemicumButton
    contents += Swing.HStrut(5)
    contents += setFavoriteButton
  }
  
  // Testing changing text color before the writing / reading text file
  // Condition must be more specified to have consistency
  def setFavorite() { 
    if (taffaButton.selected == true){
      taffaButton.foreground = new Color (176, 62, 62)
    } else if (taffaButton.selected == false){
      taffaButton.foreground = new Color (0, 0, 0)
    } else if (ravioliButton.selected == true){
      ravioliButton.foreground = new Color (176, 62, 62)
    } else if (ravioliButton.selected == false){
      ravioliButton.foreground = new Color (0, 0, 0)
    } else if (chemicumButton.selected == true){
      chemicumButton.foreground = new Color (176, 62, 62)
    } else if (chemicumButton.selected == false){
      chemicumButton.foreground = new Color (0, 0, 0)
    } else {
      None
    }
  }
  
  // Write a text file (Confirmed that it works), GUI_Test line 61
  def writeFile(line: String): Unit = {
    val file = new File("Favorite_Restaurant_List.txt")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(line) 
    bw.close()
  }
  
  
  
}