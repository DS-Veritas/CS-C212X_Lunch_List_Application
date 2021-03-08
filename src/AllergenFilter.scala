package Filter

import scala.collection.mutable._

import scala.swing._
import scala.swing.event._

import java.awt.Font.BOLD

class AllergenFilter {
  
  
  val allergenArray: ArrayBuffer[String] = ArrayBuffer.empty[String]
  
  val info = new Label("Alleregens: "){
    font = new Font("Dialog", BOLD, 13) 
  }
  val S_Allergen_Checkbox = new CheckBox("S (Soy-free)")
  val E_Allergen_Checkbox = new CheckBox("E (Egg-free)")
  val M_Allergen_Checkbox = new CheckBox("M (Milk-free)")
  val G_Allergen_Checkbox = new CheckBox("G (Gluten-free)")
  val L_Allergen_Checkbox = new CheckBox("L (Lactose-free)")
  val LL_Allergen_Checkbox = new CheckBox("LL (Low-Lactose)")
  
  val allergenUI = new BoxPanel(Orientation.Horizontal){
    contents += info
    contents += S_Allergen_Checkbox
    contents += Swing.HStrut(5)
    contents += E_Allergen_Checkbox
    contents += Swing.HStrut(5)
    contents += M_Allergen_Checkbox
    contents += Swing.HStrut(5)
    contents += G_Allergen_Checkbox
    contents += Swing.HStrut(5)
    contents += L_Allergen_Checkbox
    contents += Swing.HStrut(5)
    contents += LL_Allergen_Checkbox
  }
  
}