package pl.edu.pw.elka.stud.tkogut.passim.gui
import scala.swing._
import event.ButtonClicked

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 06.05.12
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */

object BrokeringGuiApp extends SimpleSwingApplication  {
  def top = new MainFrame {
    title = "First Swing App"

    var counter = 0

    val btn = new Button {
      text = "Click me"
    }

    val lbl = new Label {
      text = "Number of clicks: " + counter

      listenTo(btn)

      reactions += {
        case ButtonClicked(b)  =>
        counter+=1
        revalidate()
        text = "Number of clicks: " + counter
      }
    }


   contents = new BoxPanel(Orientation.Vertical) {
     contents+=btn
     contents+=lbl
     border = Swing.EmptyBorder(30,30,10,30)
   }




  }
}
