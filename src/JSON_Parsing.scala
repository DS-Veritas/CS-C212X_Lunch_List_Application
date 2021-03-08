package Tech

import scala.util.parsing.json._
import scalaj.http._
import ujson._

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
import Filter._

// Not used due to the error: "Exception in thread "main" java.lang.StackOverflowError"


class JSON_Parsing() {
  
  /*
  val date = new DateCheck()
  val main = new UI
  val restaurant = new RestaurantFilter(main.favArray)
  val allergen = new AllergenFilter()
  val menu = new MenuFilter
  
  
  def searchTaffaMenu() {
    var inputDate = main.searchField.text.toString()
    date.validateDate(inputDate)
    main.pack()
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
      main.taffaMenuUI.border = Swing.LineBorder(java.awt.Color.BLACK)
      main.taffaMenuUI.contents += Swing.VStrut(10)
      main.taffaMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        text = "     " + response("name").str + " - " + response("address").str + "     "
        foreground = new Color (14, 75, 128) 
        font = new Font("Dialog", BOLD, 12)
        
      }
      // Restaurant opening hours information on the specific day
      main.taffaMenuUI.contents += new TextArea{
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
      main.taffaMenuUI.contents += Swing.VStrut(10)
      
      // List of the menus. Add one by one by for loop
      if (response("menus").arr.isEmpty == false) {
        for (menu <- response("menus").arr(0)("courses").arr){
        // add the name of menu into allMenuArray
        // allMenuArray += menu("title").str.trim 
        main.allMenuMap += (menu("title").str.trim -> "Taffa")
        var allergenArr = menu("properties").arr.map(_.str)
        var allergenStr = "(" + allergenArr.mkString(", ") + ")"
        main.taffaMenuUI.contents += new TextArea{
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
          main.taffaMenuUI.contents += new TextArea{
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
             main.taffaMenuUI.contents += new Label("Sorry. No internet connection")
            }
        }
       main.taffaMenuUI.contents += Swing.VStrut(10)
      }
  
  def searchRavioliMenu() {
    var inputDate =  main.searchField.text.toString()
    date.validateDate(inputDate)
     main.pack()
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
      main.ravioliMenuUI.border = Swing.LineBorder(java.awt.Color.BLACK)
      main.ravioliMenuUI.contents += Swing.VStrut(10)
      main.ravioliMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        text = "     " + response("name").str + " - " + response("address").str + "     "
        foreground = new Color (14, 75, 128) 
        font = new Font("Dialog", BOLD, 12)

      }
      // Restaurant opening hours information on the specific day
      main.ravioliMenuUI.contents += new TextArea{
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
      main.ravioliMenuUI.contents += Swing.VStrut(10)
      
      // List of the menus. Add one by one by for loop
      if (response("menus").arr.isEmpty == false) {
        for (menu <- response("menus").arr(0)("courses").arr){
        // add the name of menu into allMenuArray
        // allMenuArray += menu("title").str.trim 
        main.allMenuMap += (menu("title").str.trim -> "Ravioli")
        var allergenArr = menu("properties").arr.map(_.str)
        var allergenStr = "(" + allergenArr.mkString(", ") + ")"
        main.ravioliMenuUI.contents += new TextArea{
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
          main.ravioliMenuUI.contents += new TextArea{
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
          main.ravioliMenuUI.contents += new Label("Sorry. No internet connection")
          }
        }
      main.ravioliMenuUI.contents += Swing.VStrut(10)
    }
  
  def searchChemicumMenu() {
    var inputDate = main.searchField.text.toString()
    date.validateDate(inputDate)
    main.pack()
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
      main.chemicumMenuUI.border = Swing.LineBorder(java.awt.Color.BLACK)
      main.chemicumMenuUI.contents += Swing.VStrut(10)
      main.chemicumMenuUI.contents += new TextArea{
        rows = 1
        editable = false
        background = new Color(238, 238, 238)
        text = "     " +response("name").str + " - " + response("address").str + "     "
        foreground = new Color (14, 75, 128) 
        font = new Font("Dialog", BOLD, 12)

      }
      // Restaurant opening hours information on the specific day
      main.chemicumMenuUI.contents += new TextArea{
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
      main.chemicumMenuUI.contents += Swing.VStrut(10)
      
      // List of the menus. Add one by one by for loop
      if (response("menus").arr.isEmpty == false) {
        for (menu <- response("menus").arr(0)("courses").arr){
          // add the name of menu into allMenuArray
          // allMenuArray += menu("title").str.trim 
          main.allMenuMap += (menu("title").str.trim -> "Chemicum")
          var allergenArr = menu("properties").arr.map(_.str)
          var allergenStr = "(" + allergenArr.mkString(", ") + ")"
          main.chemicumMenuUI.contents += new TextArea{
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
          main.chemicumMenuUI.contents += new TextArea{
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
          main.chemicumMenuUI.contents += new Label("Sorry. No internet connection")
        }
        }
      main.chemicumMenuUI.contents += Swing.VStrut(10)
    }
    * 
    */
  }