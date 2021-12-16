import sys.process._

/**
 *   @author Gustaf Franzén
 *   A simple API for for creating terminal UI components
 *   @param out String => Unit defaults to println ie stdout
 *   used for setting the desired output
 **/

class TerminalWindow(out : String => Unit = (s : String) => print(s)) :
	import TerminalWindow._
	import TerminalWindow.CodeType
/**
*	prepare terminal settings to enable reading of scroll status and linewrap status
*/
	enableLineWrap()
	disableUnderline()
	disableBold()

	var _scrollIsEnabled    = true
	var _lineWrapIsEnabled  = true
	var _underlineIsEnabled = false
	var _boldIsEnabled      = false

/**
 * method for writing escape codes to the @param out function
 * @param command:TerminalWindow.CodeType the code to output
 * @param args the parameters passed to that command
 **/

	private def EXEC_CMD(command : CodeType, args : Int*) : Unit =
		if args.length > 0 then
			out(command.esc + args.mkString(";") + command.code)
		else out(command.esc + command.code)

/**
*  cursor controll methods
*  @see escape codes under [[TerminalWindow.Cursor]]
*/
	def cursorUp(lines : Int = 1)    : Unit = EXEC_CMD(Cursor.up,    lines)
	def cursorDown(lines : Int = 1)  : Unit = EXEC_CMD(Cursor.down,  lines)
	def cursorRight(steps : Int = 1) : Unit = EXEC_CMD(Cursor.right, steps)
	def cursorLeft(steps : Int = 1)  : Unit = EXEC_CMD(Cursor.left,  steps)
	def cursorMove(x : Int, y : Int) : Unit = EXEC_CMD(Cursor.set, y, x)
	def cursorSave()                 : Unit = EXEC_CMD(Cursor.save)
	def cursorUnsave()               : Unit = EXEC_CMD(Cursor.unsave)
	def cursorToOrgin()              : Unit = EXEC_CMD(Cursor.toOrgin)
	def cursorHide()                 : Unit = EXEC_CMD(Cursor.hide)
	def cursorShow()                 : Unit = EXEC_CMD(Cursor.show)
	def cursorStore()                : Unit = EXEC_CMD(Cursor.store)
	def cursorRestore()              : Unit = EXEC_CMD(Cursor.restore)




/**
 * @todo
 *
 **/
//	def cursorPos() : (Int, Int) =
//		val r = getReport(() => EXEC_CMD(Cursor.getPos), 'R')
//		(r(1), r(0))

/**
*  text clearing controll methods
*  @see escape codes under [[TerminalWindow.Clear]]
*/
	def clearLineTail() : Unit = EXEC_CMD(Clear.clearTail)
	def clearLineHead() : Unit = EXEC_CMD(Clear.clearHead)
	def clearLine()     : Unit = EXEC_CMD(Clear.clearLine)
	def clearDown()     : Unit = EXEC_CMD(Clear.clearDown)
	def clearUp()       : Unit = EXEC_CMD(Clear.clearUp)
	def clear()         : Unit = EXEC_CMD(Clear.clear)

/**
*  general terminal controll methods
*  @see escape codes under [[TerminalWindow.General]]
*/
	def resetSettings()    : Unit = EXEC_CMD(General.reset)
	def enableLineWrap()   : Unit = EXEC_CMD(General.enableLineWrap)
	def dissableLineWrap() : Unit = EXEC_CMD(General.dissableLineWrap)
	def largeFont()        : Unit = EXEC_CMD(General.largeFont)
	def jumboFont()        : Unit = EXEC_CMD(General.jumboFont)
	def enableUnderline()  : Unit = {_underlineIsEnabled = true;  EXEC_CMD(General.underlineOn)}
	def disableUnderline() : Unit = {_underlineIsEnabled = false; EXEC_CMD(General.underlineOff)}
	def enableBold()       : Unit = {_boldIsEnabled = true;  EXEC_CMD(General.boldOn)}
	def disableBold()      : Unit = {_boldIsEnabled = false; EXEC_CMD(General.boldOff)}

/**
*  window controll methods
*  @see escape codes under [[TerminalWindow.Window]]
*/
	def resizeWindow(width : Int, height : Int) : Unit =
		EXEC_CMD(Window.resize, Window.resizePrefix, width, height)

	def moveWindow(x : Int, y : Int) : Unit =
		EXEC_CMD(Window.move, Window.movePrefix, x, y)

/**
 * @todo
 *
 **/
	//def windowSize() : (Int, Int)=
	//	val r = getReport(() => EXEC_CMD(Window.getSize), 3, 't')
	//	(r(2), r(1))

/**
*  scroll controll methods
*  @see escape codes under [[TerminalWindow.Scroll]]
*/
	def enableScroll()  : Unit = { _scrollIsEnabled = true;  EXEC_CMD(Scroll.enable)}
	def disableScroll() : Unit = { _scrollIsEnabled = false; EXEC_CMD(Scroll.disable)}
	def scrollDown(lines : Int) : Unit = for i <- 0 until lines do EXEC_CMD(Scroll.down)
	def scrollUp(lines : Int)   : Unit = for i <- 0 until lines do EXEC_CMD(Scroll.up)
/**
*   @param l1 line from whitch scrolling is enabled
*   @param l2 line to whitch scrolling is enabled
*   Enable scrolling from row {l1} to row {l2}
*/
	def enableBetween(l1 : Int, l2 : Int) : Unit =
		_scrollIsEnabled = true
		EXEC_CMD(Scroll.setScroll, l1, l2)
/**
*  color controll methods
*  @see escape codes under [[TerminalWindow.Color]]
*/
	def backgroundColor(color : TerminalWindow.Color) : Unit = EXEC_CMD(Colors.background(color))
	def foregroundColor(color : TerminalWindow.Color) : Unit = EXEC_CMD(Colors.foreground(color))

/**
 *
 * @todo
 * Executes a request and parses the response report.
 * Does not work
 * @param exec_cmd   () => Unit exec_cmd to execute
 * @param args       Int How many arguments are expected
 * @param terminator Char Terminator character or delimitor of the report
 * @return           Sequence of parsed integers
 **/

//	def getReport(csi: () => Unit, terminator: Char): Array[Int] =
//		var buff = new Array[Byte](8)
//		try
//			csi()
//			System.in.read(buff,3,8)
//		catch case e : Exception =>()
//		println(buff.toString)
//		var xs = (for b <- buff yield b.toChar).mkString.split(';')
//		println(xs)
//		for s <- xs yield (for c <- s yield if c.isDigit then c else ' ').toString.split(' ').filter(_.nonEmpty)(0).toInt

object TerminalWindow:

	enum Color:
		case Black, Grey, Red, BrightRed, Green, BrightGreen, Yellow, BrightYellow, Blue,
		     BrightBlue, Magenta, BrightMagenta, Cyan, BrightCyan, White, BrightWhite

	trait CodeType(val esc : String, val code : String)
	case class ESC(_code : String) extends CodeType("\u001b",  _code)
	case class CSI(_code : String) extends CodeType("\u001b[", _code)

	object Cursor:
		val up      = CSI("A")      // UP;
		val down    = CSI("B")      // DOWN;
		val right   = CSI("C")      // FORWARDS;
		val left    = CSI("D")      // BACKWARDS;
		val set     = CSI("H")      // HOME;
		val save    = CSI("s")      // SAVE;
		val unsave  = CSI("u")      // UNSAVE;
		val toOrgin = CSI("H")      // UPPER LEFT CORNER;
		val getPos  = CSI("6n")     // READ CURSOR POS
		val hide    = CSI("25l")    // HIDE;
		val show    = CSI("25h")    // SHOW;
		val store   = ESC("7")      // STORE POS;
		val restore = ESC("8")      // RESTORE POS FROM STORE;

	object Scroll:
		val enable    = CSI("r")         // ENABLE SCROLL;
		val disable   = CSI("0;0r")      // DISABLE SCROLL;
		val setScroll = CSI("r")         // SET ENABLED SCROLL SPACE;
		val down      = ESC("D")         // SCROLL DOWN (1 line);
		val up        = ESC("M")         // SCROLL UP   (1 line);

	object Clear:
		val clearTail = CSI("K")      // LINE AFTER CURSOR;
		val clearHead = CSI("1K")     // LINE BEFORE CURSOR;
		val clearLine = CSI("2K")     // ENTIRE LINE;
		val clearDown = CSI("J")      // SCREEN BELOW CURSO;
		val clearUp   = CSI("1J")     // SCREEN ABOVE CURSOR;
		val clear     = CSI("2J")     // ENIRE SCREEN;

	object General:
		val reset            = ESC("c")      // RESET TERMINAL SETTINGS;
		val enableLineWrap   = CSI("7h")     // ENABLE LINE WRAPPING;
		val dissableLineWrap = CSI("7l")     // DISSABLE LINE WRAPPING;
		val underlineOn      = CSI("4m")     // UNDERLINE;
		val underlineOff     = CSI("24m")    // UNDERLINE;
		val boldOn           = CSI("1m")
		val boldOff          = CSI("22m")
		val largeFont        = CSI("3m")     // LAGRER FONT;
		val jumboFont        = CSI("6m")     // LARGEST FONT;

	object Window:
		val resize  = CSI("t")
		val move    = CSI("t")
		val getSize = CSI("14t")
		val resizePrefix = 4
		val movePrefix   = 3

	object Colors:
		val resetColors = CSI("0m")
		def background(color : Color) : CodeType = color match
			case Color.Black         => CSI("40m")
			case Color.Grey          => CSI("40;1m")
			case Color.Red           => CSI("41m")
			case Color.BrightRed     => CSI("41;1m")
			case Color.Green         => CSI("42m")
			case Color.BrightGreen   => CSI("42;1m")
			case Color.Yellow        => CSI("43m")
			case Color.BrightYellow  => CSI("43;1m")
			case Color.Blue          => CSI("44m")
			case Color.BrightBlue    => CSI("44;1m")
			case Color.Magenta       => CSI("45m")
			case Color.BrightMagenta => CSI("45;1m")
			case Color.Cyan          => CSI("46m")
			case Color.BrightCyan    => CSI("46;1m")
			case Color.White         => CSI("47m")
			case Color.BrightWhite   => CSI("47;1m")
		def foreground(color : Color) : CodeType = color match
			case Color.Black         => CSI("30m")
			case Color.Grey          => CSI("30;1m")
			case Color.Red           => CSI("31m")
			case Color.BrightRed     => CSI("31;1m")
			case Color.Green         => CSI("32m")
			case Color.BrightGreen   => CSI("32;1m")
			case Color.Yellow        => CSI("33m")
			case Color.BrightYellow  => CSI("33;1m")
			case Color.Blue          => CSI("34m")
			case Color.BrightBlue    => CSI("34;1m")
			case Color.Magenta       => CSI("35m")
			case Color.BrightMagenta => CSI("35;1m")
			case Color.Cyan          => CSI("36m")
			case Color.BrightCyan    => CSI("36;1m")
			case Color.White         => CSI("37m")
			case Color.BrightWhite   => CSI("37;1m")
