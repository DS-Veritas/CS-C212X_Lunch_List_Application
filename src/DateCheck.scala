package Date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.time.format.DateTimeFormatter
import java.time.LocalDate

import scala.swing._
import scala.swing.event._
import java.awt.Font.BOLD

// Not used due to the error: "Exception in thread "main" java.lang.StackOverflowError"

class DateCheck() {
  
  /*
  var main = new UI
  
  def currentDateGenerator(date: String): String = {
    var currentDate = DateTimeFormatter.ofPattern("dd/MM/YYYY").format(java.time.LocalDate.now)
    return currentDate
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
          main.errorUI.contents += Swing.VStrut(10)
          main.errorUI.contents += new TextArea{
          main.errorUI.border = Swing.LineBorder(java.awt.Color.BLACK)
          rows = 1
          editable = false
          background = new Color(238, 238, 238)
          text = """     Sorry, but the input date is incorrect. 
                    |     Please check again the format (DD/MM/YYYY). """.stripMargin
          foreground = new Color (176, 62, 62)
          font = new Font("Dialog", BOLD, 12)
          }
          main.taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
          main.ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
          main.chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)
          main.errorUI.contents += Swing.VStrut(10)
          main.errorUI.revalidate()
          main.errorUI.repaint()
          main.pack()
        }
      }
      true
    } catch {
      case e: java.text.ParseException => {
        main.errorUI.contents += Swing.VStrut(10)
        main.errorUI.border = Swing.LineBorder(java.awt.Color.BLACK)
        main.errorUI.contents += new TextArea{
          rows = 2
          editable = false
          background = new Color(238, 238, 238)
          text = """     Sorry, but the input date is incorrect. 
                    |     Please check again the format (DD/MM/YYYY). """.stripMargin
          foreground = new Color (176, 62, 62)
          font = new Font("Dialog", BOLD, 12)
        }
        main.taffaMenuUI.border = Swing.EmptyBorder(0,0,0,0)
        main.ravioliMenuUI.border = Swing.EmptyBorder(0,0,0,0)
        main.chemicumMenuUI.border = Swing.EmptyBorder(0,0,0,0)
        main.errorUI.contents += Swing.VStrut(10)
        main.errorUI.revalidate()
        main.errorUI.repaint()
        main.pack()
      }
    }
  }
  * 
  */
}