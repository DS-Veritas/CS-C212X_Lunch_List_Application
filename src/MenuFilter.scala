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

class MenuFilter {
  
   val addField = new TextField { 
     columns = 25 
     }
   
   val addButton = new Button("Add / Remove as favorite")
   
   
   val addLine = new BoxPanel(Orientation.Horizontal) {
    contents += addField
    contents += Swing.HStrut(20)
    contents += addButton
    border = Swing.EmptyBorder(0, 100, 0, 100)
   }
   
   var resultBox = new BoxPanel(Orientation.Vertical) {
     border = Swing.EmptyBorder(0, 0, 0, 0)
   }
   
  // favorite menu initialization
  var favMenuArray = Source.fromFile("Favorite_Menu_List.txt").getLines.mkString.split("/").toBuffer
  favMenuArray -= ""
   
  def resultUpdate () = {
    if (favMenuArray.isEmpty == true){
      resultBox.border = Swing.LineBorder(java.awt.Color.BLACK)
      resultBox.contents += new TextArea { 
        text = "     There is no menu in your favorite list. Please add one or more menus to the list first"
        background = new Color(238, 238, 238)
        foreground = new Color (176, 62, 62)
        font = new Font("Dialog", BOLD, 12)
        rows = 1
        editable = false
        border = Swing.EmptyBorder(10, 0, 10, 0)
        }
      } else {
        resultBox.border = Swing.LineBorder(java.awt.Color.BLACK)
        resultBox.contents += new TextArea { 
          text = "     Your favoirte menus in the list are:"
          background = new Color(238, 238, 238)
          foreground = new Color (176, 62, 62)
          font = new Font("Dialog", BOLD, 12)
          rows = 1
          editable = false
          border = Swing.EmptyBorder(10, 0, 10, 0)
          }
        resultBox.contents += Swing.VStrut(1)
        for (dish <- favMenuArray) {
          resultBox.contents += new TextArea {
            background = new Color(238, 238, 238)
            //foreground = new Color (14, 75, 128)
            font = new Font("Dialog", BOLD, 12)
            rows = 1
            editable = false
            text = "     " + dish.toString()
            }
          }
        resultBox.contents += Swing.VStrut(10)
        }
    }
   
   def writeFile(line: String): Unit = {
    val file = new File("Favorite_Menu_List.txt")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(line) 
    bw.close()
  }
   
  
  
}