
import java.io.BufferedOutputStream
import java.io.OutputStream
//import TerminalWindow

object Example :

	def main(args: Array[String]): Unit =
		val term = new TerminalWindow()
		term.cursorStore()
		animateSnow(term)
		term.cursorRestore()
		term.scrollUp(1000)
		term.cursorToOrgin()
		term.clear()
		animateRocket(term)

	def animateSnow(term : TerminalWindow) : Unit =
		term.clear()
		term.cursorHide()
		val rand = scala.util.Random(99)
		for i <- 0 until 1000 do
			term.scrollDown(1)
			var x = rand.nextInt(230)
			var y = rand.nextInt(70)
			var color = TerminalWindow.Color.fromOrdinal(rand.nextInt(16))
			term.foregroundColor(color)
			term.cursorMove(x,y)
			print("██")
			var timeout: Long = System.currentTimeMillis + 10
			while System.currentTimeMillis < timeout do ()
		term.clear()


	def printRocket(term : TerminalWindow, height : Int) : Unit =
		val rocket =
		Vector("      ████",
		       "    ██    ██",
		       "  ██  ▓▓▓▓  ██",
		       "  ██  ▓▓▓▓  ██",
		       "██            ██",
		       "██    ▓▓▓▓    ██",
		       "██    ▓▓▓▓    ██",
		       "██            ██",
		       "  ██  ▓▓▓▓  ██",
		       "  ██  ▓▓▓▓  ██",
		       "██▒▒██    ██▒▒██",
		       "██▒▒██    ██▒▒██",
		       "██▒▒████████▒▒██",
		       "██▒▒██    ██▒▒██",
		       "  ██        ██")

		for i <- 0 until rocket.length do
			term.cursorMove(50,height + i)
			print(rocket(i))

	def animateRocket(term : TerminalWindow) =
		term.foregroundColor(TerminalWindow.Color.BrightCyan)
		term.clear()
		term.cursorToOrgin()
		term.cursorHide()
		term.disableScroll()
		for i <- 0 to 300 do
			printRocket(term, 200-i)
			var timeout: Long = System.currentTimeMillis + 30
			while System.currentTimeMillis < timeout do ()
			term.clear()
