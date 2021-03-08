import scala.util.parsing.json._
import scalaj.http._
import ujson._
import scala.io._

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
import Filter.AllergenFilter
import Filter.RestaurantFilter
import Filter.MenuFilter

class UI extends MainFrame {
  title = "LunchList Application by Jeheon Kim"
  private def restrictHeight(s: Component){
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }
  
  val searchIntro = new Label ("Type the date (DD/MM/YYYY): "){
    font = new Font("Dialog", BOLD, 13)
  }
  
  // Search Date Field -> Listen to when "editDone"
  val searchField = new TextField {
    columns = 6
    text = DateTimeFormatter.ofPattern("dd/MM/YYYY").format(java.time.LocalDate.now)
    // Align text in the field in the center 
    horizontalAlignment = Alignment.Center
  }
  
  // Searching Date Button -> Listen to when "clicked"
  val searchButton = new Button ("Search")
  
  val searchLine = new BoxPanel(Orientation.Horizontal) { 
    contents += searchIntro
    contents += searchField
    contents += Swing.HStrut(5)
    contents += searchButton
    border = Swing.EmptyBorder(0, 0, 0, 370)
  }
  
  // Reading the favorite restaurants from the file and transform into an Array
  var favArray = Source.fromFile("Favorite_Restaurant_List.txt").getLines.mkString.split(" ").toBuffer
  
  // println(favArray.contains("there")) // Recognize the string (restaurant name) without any white space 
  
  
  
  // Class variable initialization
  val restaurant = new RestaurantFilter(favArray)
  val allergen = new AllergenFilter()
  val menu = new MenuFilter()
  val date = new DateCheck()
  //val json = new JSON_Parsing()
  
  
  
  val info1 = new Label("""Choose one of the restaurants and press "Set / Unset as favorite" button to add to your favorite restaurant list"""){
    foreground = new Color (14, 75, 128) 
    font = new Font("Dialog", BOLD, 13)
  }
  val info2 = new Label("""The restaurant successfuly added to your favorite restaurant list will be indicated by the red color text below"""){
    foreground = new Color (14, 75, 128) 
    font = new Font("Dialog", BOLD, 13)
  }
  
  val allergenInfo1 = new Label("""Check one or more allergens to be flitered and press "Search" button to see the result"""){
    foreground = new Color (14, 75, 128) 
    font = new Font("Dialog", BOLD, 13)
  }
  
  val allergenInfo2 = new Label("""Menus matching your selection of allergens will be indicated by the red color text"""){
    foreground = new Color (14, 75, 128) 
    font = new Font("Dialog", BOLD, 13)
  }
  
  val menuInfo1 = new Label("""Type the menu name (whole or partial) and press "Add / Remove as favorite" """){
    foreground = new Color (14, 75, 128) 
    font = new Font("Dialog", BOLD, 13)
  }
  
  val menuInfo2 = new Label("""Successfuly added menu names will appear in the panel right above"""){
    foreground = new Color (14, 75, 128) 
    font = new Font("Dialog", BOLD, 13)
  }
  
  
  val infoPanel1 = new BorderPanel {
      add(info1, BorderPanel.Position.West)
      border = Swing.EmptyBorder(5, 5, 3, 5)
  }
  
  val infoPanel2 = new BorderPanel {
      add(info2, BorderPanel.Position.West)
      border = Swing.EmptyBorder(0, 5, 5, 5)
  }
  
  val allergenInfoPanel1 = new BorderPanel {
      add(allergenInfo1, BorderPanel.Position.West)
      border = Swing.EmptyBorder(5, 5, 3, 5)
  }
  
  val allergenInfoPanel2 = new BorderPanel {
      add(allergenInfo2, BorderPanel.Position.West)
      border = Swing.EmptyBorder(0, 5, 5, 5)
  }
  
  val menuInfoPanel1 = new BorderPanel {
      add(menuInfo1, BorderPanel.Position.West)
      border = Swing.EmptyBorder(5, 5, 3, 5)
  }
  
  val menuInfoPanel2 = new BorderPanel {
      add(menuInfo2, BorderPanel.Position.West)
      border = Swing.EmptyBorder(0, 5, 5, 5)
  }
  
  // contents for the menuList
  var taffaName = new TextArea {
    rows = 1
    lineWrap = true 
    wordWrap = true
    editable = false
    background = new Color(189, 189, 189)
  }
  
  var errorUI = new BoxPanel(Orientation.Vertical){
    border = Swing.EmptyBorder(0,0,0,0)
  }
  
  var taffaMenuUI = new BoxPanel(Orientation.Vertical) {
    border = Swing.EmptyBorder(0,0,0,0)
  }
  
  var ravioliMenuUI = new BoxPanel(Orientation.Vertical) {
    border = Swing.EmptyBorder(0,0,0,0)
  }
  
  var chemicumMenuUI = new BoxPanel(Orientation.Vertical) {
    border = Swing.EmptyBorder(0,0,0,0)
  }
  
  
  // allMenuMap for the pop up notification
  
  //var allMenuArray: ArrayBuffer[String] = ArrayBuffer.empty[String] 
  var allMenuMap:Map[String, String] = Map()
  
  // Show up all menus of the current date when program started
  // The number of menus to be opened depends on the list in the favArray
  if (favArray.contains("Taffa")){
    searchTaffaMenu()
  }
  
  if (favArray.contains("Ravioli")){
    searchRavioliMenu()
  }
  
  if (favArray.contains("Chemicum")){
    searchChemicumMenu()
  }
  
  if (!favArray.contains("Taffa") && !favArray.contains("Ravioli") && !favArray.contains("Chemicum")){
    searchTaffaMenu()
    searchRavioliMenu()
    searchChemicumMenu()
  }
  
  menu.resultUpdate()
  
  // Here comes the pop up notification based on the updated "allMenuArray"
  val dialogLabel = new Label 
  
  for (dish1 <- menu.favMenuArray){
    for (dish2 <- allMenuMap){
      var dish2_Name = dish2._1
      var dish2_Restaurant = dish2._2
      if (dish2_Name.toLowerCase().trim().contains(dish1.toLowerCase().trim()) && dish2_Name.toLowerCase().trim() == dish1.toLowerCase().trim()){
        Dialog.showMessage(dialogLabel, s"""Your favorite dish "$dish2_Name" is served today!""", s" Notification from the $dish2_Restaurant")
      } else if (dish2._1.toLowerCase().trim().contains(dish1.toLowerCase().trim()) && dish2._1.toLowerCase().trim() != dish1.toLowerCase().trim()){
        Dialog.showMessage(dialogLabel, s"""Hello. Possibly, we are serving one of your favorite menus today!
                                         |Today's menu "$dish2_Name" 
                                         |contains the string "$dish1" which you set as your favorite.""".stripMargin, s" Notification from the $dish2_Restaurant")
      } else {
        None
      }
    }
  }
  
  // The code below allows to check the default font of the TextArea
  // println(javax.swing.UIManager.getDefaults().getFont("Label.font"))
  
  
  val string1 = new Label("This program is designed to provide menu lists from restaurants: Taffa, Ravioli, and Chemicum"){
    font = new Font("Dialog", BOLD, 13)
  }
  val string2 = new Label("  1. Täffä in Aalto University Campus in Otaniemi"){
    font = new Font("Dialog", BOLD, 13)
  }
  val string3 = new Label("  2. Ravioli in Meilahti (Aromi Food Service, HUS)"){
    font = new Font("Dialog", BOLD, 13)
  }
  val string4 = new Label("  3. Chemicum in Helsinki University Campus in Kumpula") {
    font = new Font("Dialog", BOLD, 13)
  }
  
  // Exit Button and Save & Exit Button
  val saveExitButton = new Button("Save and Exit") 
  val exitButton = new Button("Exit Without Saving") 
  
  val exitPanel = new BoxPanel(Orientation.Horizontal){
    contents += saveExitButton
    contents += Swing.HStrut(10)
    contents += exitButton
    contents += Swing.HStrut(10)
  }
  
 
  // Main contents to be added
  contents = new BoxPanel(Orientation.Vertical){
    contents += new BorderPanel {
      add(string1, BorderPanel.Position.West)
      border = Swing.EmptyBorder(5, 0, 0, 5)
    }
    /*
     *contents += new BorderPanel {
      add(string2, BorderPanel.Position.West)
      border = Swing.EmptyBorder(5, 5, 0, 5)
    }
    contents += new BorderPanel {
      add(string3, BorderPanel.Position.West)
      border = Swing.EmptyBorder(3, 5, 0, 5)
    }
    contents += new BorderPanel {
      add(string4, BorderPanel.Position.West)
      border = Swing.EmptyBorder(3, 5, 0, 5)
    }*/
    border = Swing.EmptyBorder(10,10,10,10)
    
   
    
    // For the guide of the demonstration, uncomment below contents that are commented out
    // However, if the testing screen is less than 27 inch, I wouldn't recommend as it is not scrollable
    contents += Swing.VStrut(12)
    contents += searchLine
    contents += Swing.VStrut(10)
    //contents += allergenInfoPanel1
    //contents += allergenInfoPanel2
    contents += allergen.allergenUI
    contents += Swing.VStrut(10)
    //contents += infoPanel1 
    //contents += infoPanel2
    contents += restaurant.menuUI
    contents += Swing.VStrut(10)
    contents += errorUI
    contents += taffaMenuUI
    contents += ravioliMenuUI
    contents += chemicumMenuUI
    contents += menu.resultBox
    contents += Swing.VStrut(15)
    //contents += menuInfoPanel1
    //contents += menuInfoPanel2
    //contents += Swing.VStrut(5)
    contents += menu.addLine
    
    contents += Swing.VStrut(15)
    contents += exitPanel
  }
  
  
  listenTo(searchField)
  listenTo(searchButton)
  listenTo(restaurant.allButton)
  listenTo(restaurant.taffaButton)
  listenTo(restaurant.ravioliButton)
  listenTo(restaurant.chemicumButton)
  listenTo(restaurant.setFavoriteButton)
  
  // Allergen checkbox buttons
  listenTo(allergen.S_Allergen_Checkbox)
  listenTo(allergen.E_Allergen_Checkbox)
  listenTo(allergen.G_Allergen_Checkbox)
  listenTo(allergen.M_Allergen_Checkbox)
  listenTo(allergen.L_Allergen_Checkbox)
  listenTo(allergen.LL_Allergen_Checkbox)
  
  listenTo(menu.addButton)
  
  listenTo(saveExitButton)
  listenTo(exitButton)
  
  reactions += {
    
    // When the searched date is ENTERED (By the keyboard) 
    
    case ButtonClicked(`searchButton`) => {
      println("Search Button Activated")
      // Set radiobuttons to default (allButton selected) when newly searched
      restaurant.allButton.selected = true
      errorUI.contents.clear()
      taffaMenuUI.contents.clear()
      ravioliMenuUI.contents.clear()
      chemicumMenuUI.contents.clear()
      searchTaffaMenu()
      searchRavioliMenu()
      searchChemicumMenu()
      pack()
    }
    
    /* Due to the GUI error, enter for searchField is temporarily commented out 
    case EditDone(`searchField`) => {
      println("SearchField Activated")
      errorUI.contents.clear()
      taffaMenuUI.contents.clear()
      ravioliMenuUI.contents.clear()
      chemicumMenuUI.contents.clear() 
      searchTaffaMenu()
      searchRavioliMenu()
      searchChemicumMenu()
      pack()  
    }
    * 
    */
    
    case ButtonClicked(restaurant.allButton) => {
      taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      println("allButton Activated")
      errorUI.contents.clear()
      taffaMenuUI.contents.clear()
      ravioliMenuUI.contents.clear()
      chemicumMenuUI.contents.clear()
      searchTaffaMenu()
      searchRavioliMenu()
      searchChemicumMenu()
      restaurant.allButton.repaint()
      restaurant.allButton.revalidate()
      pack()  
    }
    
    case ButtonClicked(restaurant.taffaButton) => {
      taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)

      println("taffaButton Activated")
      errorUI.contents.clear()
      taffaMenuUI.contents.clear()
      ravioliMenuUI.contents.clear()
      chemicumMenuUI.contents.clear()
      searchTaffaMenu()
      restaurant.taffaButton.repaint()
      restaurant.taffaButton.revalidate()
      pack()  
    }
    
    case ButtonClicked(restaurant.ravioliButton) => {
      taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      println("ravioliButton Activated")
      errorUI.contents.clear()
      taffaMenuUI.contents.clear()
      ravioliMenuUI.contents.clear()
      chemicumMenuUI.contents.clear()
      searchRavioliMenu()
      restaurant.ravioliButton.repaint()
      restaurant.ravioliButton.revalidate()
      pack()  
    }
    
    case ButtonClicked(restaurant.chemicumButton) => {
      taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)
      println("ravioliButton Activated")
      errorUI.contents.clear()
      taffaMenuUI.contents.clear()
      ravioliMenuUI.contents.clear()
      chemicumMenuUI.contents.clear()
      searchChemicumMenu()
      restaurant.chemicumButton.repaint()
      restaurant.chemicumButton.revalidate()
      pack()  
    }
    
    case ButtonClicked(restaurant.setFavoriteButton) => {
      if (restaurant.taffaButton.selected == true) {
        if (favArray.contains("Taffa")){
          favArray -= "Taffa"
          restaurant.taffaButton.foreground = new Color (0, 0, 0)
        } else {
          favArray += "Taffa"
          restaurant.taffaButton.foreground = new Color (176, 62, 62)
        }
      } else if (restaurant.ravioliButton.selected == true) {
        if (favArray.contains("Ravioli")){
          favArray -= "Ravioli"
          restaurant.ravioliButton.foreground = new Color (0, 0, 0)
        } else {
          favArray += "Ravioli"
          restaurant.ravioliButton.foreground = new Color (176, 62, 62)
        }
      } else if (restaurant.chemicumButton.selected == true) {
        if (favArray.contains("Chemicum")){
          favArray -= "Chemicum"
          restaurant.chemicumButton.foreground = new Color (0, 0, 0)
        } else {
          favArray += "Chemicum"
          restaurant.chemicumButton.foreground = new Color (176, 62, 62)
        }
      } else {
        None
      }
      
      restaurant.menuUI.repaint()
      restaurant.menuUI.revalidate()
      pack()
    }
    
    case ButtonClicked(menu.addButton) => {
      // Make the list case insensitive and white-space free
      val favMenuArray2 = menu.favMenuArray.map(_.trim().toLowerCase())
      if (favMenuArray2.contains(menu.addField.text.trim.toLowerCase())){
        for (dish <- menu.favMenuArray) {
          if (dish.trim().toLowerCase() == menu.addField.text.trim.toLowerCase()){
            menu.favMenuArray -= dish
          } else {
            None
          }
        }
      } else {
        menu.favMenuArray += menu.addField.text.trim()
      }
      menu.addField.text = ""
      menu.resultBox.contents.clear()
      menu.resultUpdate()
      menu.resultBox.repaint()
      menu.resultBox.revalidate()
      pack()
    }
    
    
    case ButtonClicked(`exitButton`) => {
      restaurant.writeFile("")
      menu.writeFile("")
      sys.exit(0)
    }
    
    case ButtonClicked(`saveExitButton`) => {
      val favList = favArray.mkString(" ")
      restaurant.writeFile(favList)
      val favMenuList = menu.favMenuArray.mkString("/")
      menu.writeFile(favMenuList)
      sys.exit(0)
    }
    
    // Allergen filter buttons (checked: add to array, unchecked: remove from array) 
    case ButtonClicked(allergen.S_Allergen_Checkbox) => {
      if (allergen.S_Allergen_Checkbox.selected) {
        allergen.allergenArray += "S"
      } else {
        allergen.allergenArray -= "S"
      }
      println(allergen.allergenArray.mkString(""))
    }
    
    case ButtonClicked(allergen.E_Allergen_Checkbox) => {
      if (allergen.E_Allergen_Checkbox.selected) {
        allergen.allergenArray += "E"
      } else {
        allergen.allergenArray -= "E"
      }
      println(allergen.allergenArray.mkString(""))
    }
    
    case ButtonClicked(allergen.M_Allergen_Checkbox) => {
      if (allergen.M_Allergen_Checkbox.selected) {
        allergen.allergenArray += "M"
      } else {
        allergen.allergenArray -= "M"
      }
      println(allergen.allergenArray.mkString(""))
    }
    
    case ButtonClicked(allergen.G_Allergen_Checkbox) => {
      if (allergen.G_Allergen_Checkbox.selected) {
        allergen.allergenArray += "G"
      } else {
        allergen.allergenArray -= "G"
      }
      println(allergen.allergenArray.mkString(""))
    }
    
    case ButtonClicked(allergen.L_Allergen_Checkbox) => {
      if (allergen.L_Allergen_Checkbox.selected) {
        allergen.allergenArray += "L"
      } else {
        allergen.allergenArray -= "L"
      }
      println(allergen.allergenArray.mkString(""))
    }
    
    case ButtonClicked(allergen.LL_Allergen_Checkbox) => {
      if (allergen.LL_Allergen_Checkbox.selected) {
        allergen.allergenArray += "LL"
      } else {
        allergen.allergenArray -= "LL"
      }
      println(allergen.allergenArray.mkString(""))
    }
      
  }
  
  
  def validateDate(inputDate: String) = {
    var dateArr = inputDate.split("/")
    try {
      val dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy")
      dateFormat.setLenient(false)
      dateFormat.parse(inputDate)
      // Check if the the day, month, and year are expressed in correct digit numbers
      if(dateArr(0).length() > 2 || dateArr(1).length() > 2 || dateArr(2).length() > 4){
        throw new Exception() {
          errorUI.contents += Swing.VStrut(10)
          errorUI.contents += new TextArea{
          errorUI.border = Swing.LineBorder(java.awt.Color.BLACK)
          rows = 1
          editable = false
          background = new Color(238, 238, 238)
          text = """     Sorry, but the input date is incorrect. 
                    |     Please check again the format (DD/MM/YYYY). """.stripMargin
          foreground = new Color (176, 62, 62)
          font = new Font("Dialog", BOLD, 12)
          }
          taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
          ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
          chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)
          errorUI.contents += Swing.VStrut(10)
          errorUI.revalidate()
          errorUI.repaint()
          pack()
        }
      }
      true
    } catch {
      case e: java.text.ParseException => {
        errorUI.contents += Swing.VStrut(10)
        errorUI.border = Swing.LineBorder(java.awt.Color.BLACK)
        errorUI.contents += new TextArea{
          rows = 2
          editable = false
          background = new Color(238, 238, 238)
          text = """     Sorry, but the input date is incorrect. 
                    |     Please check again the format (DD/MM/YYYY). """.stripMargin
          foreground = new Color (176, 62, 62)
          font = new Font("Dialog", BOLD, 12)
        }
        taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
        ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
        chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)
        errorUI.contents += Swing.VStrut(10)
        errorUI.revalidate()
        errorUI.repaint()
        pack()
      }
    }
  }
  
  def searchTaffaMenu() {
    var inputDate = searchField.text.toString()
    validateDate(inputDate)
    pack()
    var searchedDay = inputDate.substring(0,2)
    var searchedMonth = inputDate.substring(3,5)
    var searchedYear = inputDate.substring(6,10)
    
    // Find the day of the week for the result section
    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val dayFinder = LocalDate.parse(inputDate, dateFormat).getDayOfWeek.toString()
    // Leave the first capital letter and changes the rest into the lowercase letters
    val dayOfWeek = dayFinder.substring(0, 1) + dayFinder.substring(1).toLowerCase()
    
    /* 
     * 3: Täffä, Otaniemi
     * 47: Ravioli Meilahti
     */ 
    
    val taffaNum = "3"
    
    // JSON response about the Taffa's menu from the Kanttiinit
   
    try {
      // for test: https://kitchen.kanttiinit.fi/restaurants/32/menu?day=2020-04-20
      var json_response = Http("https://kitchen.kanttiinit.fi/restaurants/" + taffaNum + "/menu?day=" + searchedYear + "-" + searchedMonth + "-" + searchedDay).option(HttpOptions.connTimeout(10000)).option(HttpOptions.readTimeout(50000)).asString
      assertEquals(json_response.code, 200)
      assertEquals("{", json_response.body.substring(0, 1))
      var response = ujson.read(json_response.body)
      // Restaurant name and the day of the week
      taffaMenuUI.border = Swing.LineBorder(java.awt.Color.BLACK)
      taffaMenuUI.contents += Swing.VStrut(10)
      taffaMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        text = "     " + response("name").str + " - " + response("address").str + "     "
        foreground = new Color (14, 75, 128) 
        font = new Font("Dialog", BOLD, 12)
        
      }
      // Restaurant opening hours information on the specific day
      taffaMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        if (dayOfWeek == "Wednesday"){
          var timeArr = response("openingHours").arr(2).str.split(" - ")
          text = "     " + dayOfWeek +   " menus served from " + timeArr(0) + " to " + timeArr(1) + "     "
        } else if (dayOfWeek == "Saturday" || dayOfWeek == "Sunday"){
          text = "     " + "Sorry, but the restaurant doesn't open on " + dayOfWeek + "     "
        } else {
          var timeArr = response("openingHours").arr(0).str.split(" - ")
          text = "     " + dayOfWeek + " menus served from " + timeArr(0) + " to " + timeArr(1) + "     "
        }
        foreground = new Color (41, 105, 33)  
        font = new Font("Dialog", BOLD, 12)
      }
      
      // Space between the restaurant information and the menus
      taffaMenuUI.contents += Swing.VStrut(10)
      
      // List of the menus. Add one by one by for loop
      if (response("menus").arr.isEmpty == false) {
        for (menu <- response("menus").arr(0)("courses").arr){
        // add the name of menu into allMenuArray
        // allMenuArray += menu("title").str.trim 
        allMenuMap += (menu("title").str.trim -> "Taffa")
        var allergenArr = menu("properties").arr.map(_.str)
        var allergenStr = "(" + allergenArr.mkString(", ") + ")"
        taffaMenuUI.contents += new TextArea{
          rows = 1
          editable = false
          background = new Color(238, 238, 238)
          text = "     " + menu("title").str + " " + allergenStr + "     "
          
          // If selected allergens contained, change the font to BOLD
          var boolCheck = false
          if (allergen.allergenArray.isEmpty == true){
            None
            } else {
              breakable{
                for (allergen <- allergen.allergenArray) {
                  if (allergenArr.contains(allergen)){
                    boolCheck = true
                    } else {
                      boolCheck = false
                      // which means the selection of multiple allergens must be satisfied at the same time
                      break
                      }
                  }
                }
              if (boolCheck == true) {
                font = new Font("Dialog", BOLD, 12)
                foreground = new Color (176, 62, 62)
                }
              }
          }
        }
        } else {
          taffaMenuUI.contents += new TextArea{
            rows = 1
            editable = false
            background = new Color(238, 238, 238)
            text = "     " + "No menu available" + "     "
            font = new Font("Dialog", BOLD, 12)
            foreground = new Color (176, 62, 62)
            }
          }
      
      } catch {
        case x: java.net.UnknownHostException => {
            taffaMenuUI.contents += new Label("Sorry. No internet connection")
            }
        }
      taffaMenuUI.contents += Swing.VStrut(10)
      }
  
  def searchRavioliMenu() {
    var inputDate = searchField.text.toString()
    validateDate(inputDate)
    pack()
    var searchedDay = inputDate.substring(0,2)
    var searchedMonth = inputDate.substring(3,5)
    var searchedYear = inputDate.substring(6,10)
    
    // Find the day of the week for the result section
    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val dayFinder = LocalDate.parse(inputDate, dateFormat).getDayOfWeek.toString()
    // Leave the first capital letter and changes the rest into the lowercase letters
    val dayOfWeek = dayFinder.substring(0, 1) + dayFinder.substring(1).toLowerCase()
    
    /* 
     * 3: Täffä, Otaniemi
     * 47: Ravioli Meilahti
     */ 
    
    val ravioliNum = "47"
    
    // JSON response about the Taffa's menu from the Kanttiinit
   
    try {
      var json_response = Http("https://kitchen.kanttiinit.fi/restaurants/" + ravioliNum + "/menu?day=" + searchedYear + "-" + searchedMonth + "-" + searchedDay).option(HttpOptions.connTimeout(10000)).option(HttpOptions.readTimeout(50000)).asString
      assertEquals(json_response.code, 200)
      assertEquals("{", json_response.body.substring(0, 1))
      var response = ujson.read(json_response.body)
      // Restaurant name and the day of the week
      ravioliMenuUI.border = Swing.LineBorder(java.awt.Color.BLACK)
      ravioliMenuUI.contents += Swing.VStrut(10)
      ravioliMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        text = "     " + response("name").str + " - " + response("address").str + "     "
        foreground = new Color (14, 75, 128) 
        font = new Font("Dialog", BOLD, 12)

      }
      // Restaurant opening hours information on the specific day
      ravioliMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        if (dayOfWeek == "Saturday" || dayOfWeek == "Sunday"){
          var timeArr = response("openingHours").arr(6).str.split(" - ")
          text = "     " + dayOfWeek + " menus served from " + timeArr(0) + " to " + timeArr(1) + "     "
          } else {
            var timeArr = response("openingHours").arr(0).str.split(" - ")
            text = "     " + dayOfWeek + " menus served from " + timeArr(0) + " to " + timeArr(1) + "     "
            }
        foreground = new Color (41, 105, 33)  
        font = new Font("Dialog", BOLD, 12)

        }
      
      // Space between the restaurant information and the menus
      ravioliMenuUI.contents += Swing.VStrut(10)
      
      // List of the menus. Add one by one by for loop
      if (response("menus").arr.isEmpty == false) {
        for (menu <- response("menus").arr(0)("courses").arr){
        // add the name of menu into allMenuArray
        // allMenuArray += menu("title").str.trim 
        allMenuMap += (menu("title").str.trim -> "Ravioli")
        var allergenArr = menu("properties").arr.map(_.str)
        var allergenStr = "(" + allergenArr.mkString(", ") + ")"
        ravioliMenuUI.contents += new TextArea{
          rows = 1
          editable = false
          background = new Color(238, 238, 238)
          text = "     " + menu("title").str + " " + allergenStr + "     "
          // If selected allergens contained, change the font to BOLD
          var boolCheck = false
          if (allergen.allergenArray.isEmpty == true){
            None
            } else {
              breakable{
                for (allergen <- allergen.allergenArray) {
                  if (allergenArr.contains(allergen)){
                    boolCheck = true
                    } else {
                      boolCheck = false
                      // which means the selection of multiple allergens must be satisfied at the same time
                      break
                      }
                  }
                }
              if (boolCheck == true) {
                font = new Font("Dialog", BOLD, 12)
                foreground = new Color (176, 62, 62)
                }
              }
          }
        }
        } else {
          ravioliMenuUI.contents += new TextArea{
            rows = 1
            editable = false
            background = new Color(238, 238, 238)
            text = "     " + "No menu available" + "     "
            font = new Font("Dialog", BOLD, 12)
            foreground = new Color (176, 62, 62)
            }
          }
      
      } catch {
        case x: java.net.UnknownHostException => {
          ravioliMenuUI.contents += new Label("Sorry. No internet connection")
          }
        }
      ravioliMenuUI.contents += Swing.VStrut(10)
    }
  
  def searchChemicumMenu() {
    var inputDate = searchField.text.toString()
    validateDate(inputDate)
    pack()
    var searchedDay = inputDate.substring(0,2)
    var searchedMonth = inputDate.substring(3,5)
    var searchedYear = inputDate.substring(6,10)
    
    // Find the day of the week for the result section
    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val dayFinder = LocalDate.parse(inputDate, dateFormat).getDayOfWeek.toString()
    // Leave the first capital letter and changes the rest into the lowercase letters
    val dayOfWeek = dayFinder.substring(0, 1) + dayFinder.substring(1).toLowerCase()
    
    /* 
     * 3: Täffä, Otaniemi
     * 32: Chemicum Kumpula
     * 47: Ravioli Meilahti
     */ 
    
    val chemicumNum = "32"
    
    // JSON response about the Taffa's menu from the Kanttiinit
   
    try {
      var json_response = Http("https://kitchen.kanttiinit.fi/restaurants/" + chemicumNum + "/menu?day=" + searchedYear + "-" + searchedMonth + "-" + searchedDay).option(HttpOptions.connTimeout(10000)).option(HttpOptions.readTimeout(50000)).asString
      assertEquals(json_response.code, 200)
      assertEquals("{", json_response.body.substring(0, 1))
      var response = ujson.read(json_response.body)
      // Restaurant name and the day of the week
      chemicumMenuUI.border = Swing.LineBorder(java.awt.Color.BLACK)
      chemicumMenuUI.contents += Swing.VStrut(10)
      chemicumMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        text = "     " +response("name").str + " - " + response("address").str + "     "
        foreground = new Color (14, 75, 128) 
        font = new Font("Dialog", BOLD, 12)

      }
      // Restaurant opening hours information on the specific day
      chemicumMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        if (dayOfWeek == "Saturday" || dayOfWeek == "Sunday"){
          text = "     " +"Sorry, but the restaurant doesn't open on " + dayOfWeek + "     "
        } else if (dayOfWeek == "Friday"){
          var timeArr = response("openingHours").arr(4).str.split(" - ")
          text = "     " +dayOfWeek + " menus served from " + timeArr(0) + " to " + timeArr(1) + "     "
        } else {
          var timeArr = response("openingHours").arr(0).str.split(" - ")
          text = "     " +dayOfWeek + " menus served from " + timeArr(0) + " to " + timeArr(1) + "     "
        }
        foreground = new Color (41, 105, 33)  
        font = new Font("Dialog", BOLD, 12)
      }
      
      // Space between the restaurant information and the menus
      chemicumMenuUI.contents += Swing.VStrut(10)
      
      // List of the menus. Add one by one by for loop
      if (response("menus").arr.isEmpty == false) {
        for (menu <- response("menus").arr(0)("courses").arr){
          // add the name of menu into allMenuArray
          // allMenuArray += menu("title").str.trim 
          allMenuMap += (menu("title").str.trim -> "Chemicum")
          var allergenArr = menu("properties").arr.map(_.str)
          var allergenStr = "(" + allergenArr.mkString(", ") + ")"
          chemicumMenuUI.contents += new TextArea{
            rows = 1
            editable = false
            background = new Color(238, 238, 238)
            text = "     " +menu("title").str + " " + allergenStr + "     "
            // If selected allergens contained, change the font to BOLD
            var boolCheck = false
            if (allergen.allergenArray.isEmpty == true){
              None
              } else {
                breakable{
                  for (allergen <- allergen.allergenArray) {
                    if (allergenArr.contains(allergen)){
                      boolCheck = true
                      } else {
                        boolCheck = false
                        // which means the selection of multiple allergens must be satisfied at the same time
                        break
                        }
                    }
                  }
                if (boolCheck == true) {
                  font = new Font("Dialog", BOLD, 12)
                  foreground = new Color (176, 62, 62)
                  }
                }
            }
          }
        } else {
          chemicumMenuUI.contents += new TextArea{
            rows = 1
            editable = false
            background = new Color(238, 238, 238)
            text = "     " + "No menu available" + "     "
            font = new Font("Dialog", BOLD, 12)
            foreground = new Color (176, 62, 62)
            }
          }
      
      } catch {
        case x: java.net.UnknownHostException => {
          chemicumMenuUI.contents += new Label("Sorry. No internet connection")
        }
        }
      chemicumMenuUI.contents += Swing.VStrut(10)
      }
  }