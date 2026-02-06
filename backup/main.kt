package com.example.joywtg2

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview

import java.io.File
import java.lang.Math
import java.text.SimpleDateFormat
import java.nio.file.Paths
import java.util.*


// documentation to find at:
const val helpurl = "https://www.github.com/metazip/Joy-REPL"

// ----- to stop processing
public var runvm: Boolean = false

// ----- constants
const val ctquote: String   = "'"
const val ctquote2: String  = "\""
const val ctdef: String     = "=="
const val cttrue: String    = "true"  // ???
const val ctfalse: String   = "false" // ???
const val ctact: String     = "!"
const val ctspecial: String = "()[]{}" + ctquote + ctquote2

// ----- error messages
const val ecalcstop  = "CALC-STOP"   // ???
const val ereturndo  = "RETURN-DO"   // ???
const val ebreakloop = "BREAK-LOOP"  // ???

const val ecomonlyparenend   = ") without ( before"
const val ecomonlybracketend = "] without [ in front"
const val ecomonlycurlyend   = "} without { before"
const val ecomcommtunexpend  = "unexpected ending, ) missing"
const val ecomlistunexpend   = "unexpected ending, ] missing"
const val ecomstringunexpend = "unexpected end of String scan"
const val ecom2notallowed    = "# not allowed in lists"

const val einitarrayoverflow = "the pcounter exceeds the array size"
const val eidentnull = "Eval  >>>  ident is not defined"
const val efuncundef = "Function is not defined"
const val edeferr    =  "==  >>>  wrong use of define"
const val estacknull    = "  >>>  stack is null"
const val efloatexp     = "  >>>  float expected"
const val econsexp      = "  >>>  cons expected"
const val elistexp      = "  >>>  list expected"
const val elistorstrexp = "  >>>  list or string expected"
const val elistornumexp = "  >>>  list or float expected"
const val edivbyzero    = "  >>>  division by zero"
const val eidentexp     = "  >>>  ident expected"
const val eboolexp      = "  >>>  bool expected"
const val estringexp    = "  >>>  string expected"
const val estringnull   = "  >>>  string is null"
const val elistofstrexp = "  >>>  list of strings expected"
const val elistofnumexp = "  >>>  list of floats expected"
const val eoutofrange   = "  >>>  access out of range"
const val enoncompare   = "  >>>  types not comparable"
const val enonvalue     = "  >>>  no value for key"

// ----- function names
const val ctundefined = "undefined"
// const val ctquote = "'"
const val ctid       = "id"
const val ctstack    = "stack"
const val ctunstack  = "unstack"
const val ctdup      = "dup"
const val ctpop      = "pop"
const val ctswap     = "swap"
const val ctover     = "over"
const val ctrotate   = "rotate"
const val ctrollup   = "rollup"
const val ctrolldown = "rolldown"
const val ctfirst = "first"
const val ctrest  = "rest"
const val ctcons  = "cons"
const val ctuncons = "uncons"
const val ctswons  = "swons"
const val ctunswons = "unswons"
const val ctreverse = "reverse"
const val ctadd  = "+"
const val ctsub  = "-"
const val ctmul  = "*"
const val ctmul2 = "×"
const val ctdiv  = "/"
const val ctdiv2 = "÷"
const val ctmod  = "mod"
const val ctpow  = "pow"
const val ctpred = "pred"
const val ctsucc = "succ"
const val ctsign = "sign"
const val ctabs  = "abs"
const val ctneg  = "neg"
const val ctfloor = "floor"
const val ctceil  = "ceil"
const val cttrunc = "trunc"
const val ctint   = "int"
const val ctfrac  = "frac"
const val ctround = "round"
const val ctroundto = "roundto"
const val ctexp   = "exp"
const val ctlog   = "log"
const val ctlog10 = "log10"
const val ctlog2  = "log2"
const val ctsq   = "sq"
const val ctsqrt = "sqrt"
const val ctcbrt = "cbrt"
const val ctpi   = "pi"
const val ctsin  = "sin"
const val ctcos  = "cos"
const val cttan  = "tan"
const val ctasin = "asin"
const val ctacos = "acos"
const val ctatan = "atan"
const val ctatan2 = "atan2"
const val ctsinh = "sinh"
const val ctcosh = "cosh"
const val cttanh = "tanh"
const val ctrad  = "rad"
const val ctdeg  = "deg"
const val ctnot  = "not"
const val ctand  = "and"
const val ctor   = "or"
const val ctxor  = "xor"
const val cteq   = "="
const val ctne   = "<>"
const val ctneq  = "!="
const val ctlt   = "<"
const val ctgt   = ">"
const val ctle   = "<="
const val ctge   = ">="
const val ctmin  = "min"
const val ctmax  = "max"
const val cthas  = "has"
const val ctin   = "in"
const val ctsmall = "small"
const val ctnull  = "null"
const val ctlist  = "list"
const val ctleaf  = "leaf"
const val ctbool  = "bool"
const val ctconsp = "consp"
const val ctident = "ident"
const val ctfloat = "float"
const val ctstring = "string"
const val ctundef = "undef"
const val cttype  = "type"
const val ctname  = "name"
const val ctbody  = "body"
const val ctinfo  = "info"
const val ctuser  = "user"
const val ctbound = "bound"
const val ctintern = "intern"
const val ctindex = "index"
const val ctat    = "at"
const val ctof    = "of"
const val ctmake  = "make"
const val cttake  = "take"
const val ctdrop  = "drop"
const val ctconcat  = "concat"
const val ctswoncat = "swoncat"
const val ctfind   = "find"
const val ctcount  = "count"
const val ctcollect = "collect"
const val ctiota   = "iota"
const val ctfromto = "fromto"
const val ctsum    = "sum"
const val ctprod   = "prod"
const val ctzip    = "zip"
const val ctunlist = "unlist"
const val ctnewdict = "newdict"
const val ctget   = "get"
const val ctput   = "put"
const val ctmidstr   = "midstr"
const val ctleftstr  = "leftstr"
const val ctrightstr = "rightstr"
const val ctindexof  = "indexof"
const val ctupper = "upper"
const val ctlower = "lower"
const val ctcapitalize = "capitalize"
const val cttrim  = "trim"
const val cttriml = "triml"
const val cttrimr = "trimr"
const val cttrimpre = "trimpre"
const val ctchr   = "chr"
const val ctord   = "ord"
const val ctreplace  = "replace"
const val ctreplace1 = "replace1"
const val ctsplit = "split"
const val ctjoin  = "join"
const val cti     = "i"
const val ctdip   = "dip"
const val ctdip2  = "dip2"
const val ctnullary = "nullary"
const val ctdo     = "do"
const val ctreturn = "return"
const val ctinfra  = "infra"
const val ctunary  = "unary"
const val ctunary2 = "unary2"
const val ctunary3 = "unary3"
const val ctunary4 = "unary4"
const val ctif     = "if"
const val ctbranch = "branch"
const val ctifte   = "ifte"
const val ctchoice = "choice"
const val ctcase  = "case"
const val ctcond  = "cond"
const val cttimes = "times"
const val ctwhile = "while"
const val ctloop  = "loop"
const val ctbreak = "break"
const val ctstep  = "step"
const val ctmap   = "map"
const val ctfold  = "fold"
const val ctfilter = "filter"
const val ctsplit2 = "split2"
const val ctconstr1 = "constr1"
const val ctcleave = "cleave"
const val ctprimrec = "primrec"
const val cttailrec = "tailrec"
const val ctgenrec  = "genrec"
const val ctlinrec  = "linrec"
const val ctbinrec  = "binrec"
const val cttry    = "try"
const val ctabort  = "abort"
const val cterror  = "error"
const val ctsize   = "size"
const val ctunpack = "unpack"
const val ctpack   = "pack"
const val ctparse  = "parse"  // position?
const val cttostr  = "tostr"
const val cttoval = "toval"
const val cttrytoval = "trytoval"
const val ctstrtod = "strtod"
const val cthextod = "hextod"
const val cttohex  = "tohex"
const val cttimeformat = "timeformat"
const val ctidentlist = "identlist"
const val ctidentdump = "identdump"
const val cthelpinfo = "helpinfo"
const val ctdeflines  = "deflines"
const val ctgc = "gc"
const val cttest = "test"   //  for test!!!


// ----- Joy is a stack-based language
var stack: Any = Nil()
var quotenext: Boolean = false
var trail: Any = Nil()

class Nil {
    constructor() {}
}

class Cons {
    var addr: Any
    var decr: Any
    constructor(hd: Any, tl: Any) {  addr = hd ; decr = tl  }
}

class Ident {
    val pname: String
    var body: Any
    var info: String
    constructor(pn: String, bd: Any) {  pname = pn ; body = bd ; info = ""  }
}

// ----- output

fun toSequence(x: Any): String {
    var i: Any = x
    var s: String = ""
    var sp: String = ""
    while (i is Cons) {
        s = s + sp + toValue(i.addr)
        sp = " "
        i = i.decr
    }
    return s  }

fun toDoubleString(x: Any): String {
    val str = x.toString()
    val len = str.length
    if (len>=2) { if (str.substring(len-2,len) == ".0") return str.substring(0,len-2) }
    return str  }

fun toValue(x: Any): String {
    return when (x) {
        is Cons   -> "["+toSequence(x)+"]"
        is Nil    -> "[ ]"
        is Ident  -> x.pname
        is Double -> toDoubleString(x)
        is Int    -> "("+x.toString()+")"
        is Long   -> "(Long="+x.toString()+")"
        is String -> "\""+x+"\""
        true      -> cttrue
        false     -> ctfalse
        else      -> "(Printerror)"
    }  }

// ----- Joy virtual machine

class JoyVM {

    var maxproc: Int = 500
    var proc: Array<() -> Unit> = Array(maxproc, { initElement(it) })

    var pcounter: Int = 0
    var identlist: Any = Nil()

    var xmark   = Ident("_mark",Nil())  // besser nicht in identlist

    var idundefined = newidentfunc(ctundefined,::fundefined)
    var iddef   = newidentfunc(ctdef,::fdef)
    var idact   = newidentfunc(ctact,::fact)
    var idquote = newidentfunc(ctquote,::fquote)
    var idid    = newidentfunc(ctid,::fid)
    var idstack = newidentfunc(ctstack,::fstack)
    var idunstack = newidentfunc(ctunstack,::funstack)
    var iddup  = newidentfunc(ctdup,::fdup)
    var idpop  = newidentfunc(ctpop,::fpop)
    var idswap = newidentfunc(ctswap,::fswap)
    var idover = newidentfunc(ctover,::fover)
    var idrotate = newidentfunc(ctrotate,::frotate)
    var idrollup = newidentfunc(ctrollup,::frollup)
    var idrolldown = newidentfunc(ctrolldown,::frolldown)
    var idfirst = newidentfunc(ctfirst,::ffirst)
    var idrest = newidentfunc(ctrest,::frest)
    var idcons = newidentfunc(ctcons,::fcons)
    var iduncons = newidentfunc(ctuncons,::funcons)
    var idswons = newidentfunc(ctswons,::fswons)
    var idunswons = newidentfunc(ctunswons,::funswons)
    var idreverse = newidentfunc(ctreverse,::freverse)
    var idadd = newidentfunc(ctadd,::fadd)
    var idsub = newidentfunc(ctsub,::fsub)
    var idmul = newidentfunc(ctmul,::fmul)
    var idmul2 = newidentfunc(ctmul2,::fmul2)
    var iddiv = newidentfunc(ctdiv,::fdiv)
    var iddiv2 = newidentfunc(ctdiv2,::fdiv2)
    var idmod = newidentfunc(ctmod,::fmod)
    var idpow = newidentfunc(ctpow,::fpow)
    var idpred = newidentfunc(ctpred,::fpred)
    var idsucc = newidentfunc(ctsucc,::fsucc)
    var idsign = newidentfunc(ctsign,::fsign)
    var idabs = newidentfunc(ctabs,::fabs)
    var idneg = newidentfunc(ctneg,::fneg)
    var idfloor = newidentfunc(ctfloor,::ffloor)
    var idceil = newidentfunc(ctceil,::fceil)
    var idtrunc = newidentfunc(cttrunc,::ftrunc)
    var idint = newidentfunc(ctint,::fint)
    var idfrac = newidentfunc(ctfrac,::ffrac)
    var idround = newidentfunc(ctround,::fround)
    var idroundto = newidentfunc(ctroundto,::froundto)
    var idexp = newidentfunc(ctexp,::fexp)
    var idlog = newidentfunc(ctlog,::flog)
    var idlog10 = newidentfunc(ctlog10,::flog10)
    var idlog2 = newidentfunc(ctlog2,::flog2)
    var idsq = newidentfunc(ctsq,::fsq)
    var idsqrt = newidentfunc(ctsqrt,::fsqrt)
    var idcbrt = newidentfunc(ctcbrt,::fcbrt)
    var idpi = newidentfunc(ctpi,::fpi)
    var idsin = newidentfunc(ctsin,::fsin)
    var idcos = newidentfunc(ctcos,::fcos)
    var idtan = newidentfunc(cttan,::ftan)
    var idasin = newidentfunc(ctasin,::fasin)
    var idacos = newidentfunc(ctacos,::facos)
    var idatan = newidentfunc(ctatan,::fatan)
    var idatan2 = newidentfunc(ctatan2,::fatan2)
    var idsinh = newidentfunc(ctsinh,::fsinh)
    var idcosh = newidentfunc(ctcosh,::fcosh)
    var idtanh = newidentfunc(cttanh,::ftanh)
    var idrad = newidentfunc(ctrad,::frad)
    var iddeg = newidentfunc(ctdeg,::fdeg)
    var idnot = newidentfunc(ctnot,::fnot)
    var idand = newidentfunc(ctand,::fand)
    var idor = newidentfunc(ctor,::f_or)
    var idxor = newidentfunc(ctxor,::fxor)
    var ideq = newidentfunc(cteq,::feq)
    var idne = newidentfunc(ctne,::fne)
    var idneq = newidentfunc(ctneq,::fneq)
    var idlt = newidentfunc(ctlt,::flt)
    var idgt = newidentfunc(ctgt,::fgt)
    var idle = newidentfunc(ctle,::fle)
    var idge = newidentfunc(ctge,::fge)
    var idmin = newidentfunc(ctmin,::fmin)
    var idmax = newidentfunc(ctmax,::fmax)
    var idhas = newidentfunc(cthas,::fhas)
    var idin = newidentfunc(ctin,::fin)
    var idsmall = newidentfunc(ctsmall,::fsmall)
    var idnull  = newidentfunc(ctnull,::fnull)
    var idlist  = newidentfunc(ctlist,::flist)
    var idleaf = newidentfunc(ctleaf,::fleaf)
    var idbool = newidentfunc(ctbool,::fbool)
    var idconsp = newidentfunc(ctconsp,::fconsp)
    var idident = newidentfunc(ctident,::fident)
    var idfloat = newidentfunc(ctfloat,::ffloat)
    var idstring = newidentfunc(ctstring,::fstring)
    var idundef = newidentfunc(ctundef,::fundef)
    var idtype = newidentfunc(cttype,::ftype)
    var idname = newidentfunc(ctname,::fname)
    var idbody  = newidentfunc(ctbody,::fbody)
    var idinfo = newidentfunc(ctinfo,::finfo)
    var iduser = newidentfunc(ctuser,::fuser)
    var idbound = newidentfunc(ctbound,::fbound)
    var idintern = newidentfunc(ctintern,::fintern)
    var idindex = newidentfunc(ctindex,::findex)
    var idat = newidentfunc(ctat,::fat)
    var idof = newidentfunc(ctof,::fof)
    var idmake = newidentfunc(ctmake,::fmake)
    var idtake = newidentfunc(cttake,::ftake)
    var iddrop = newidentfunc(ctdrop,::fdrop)
    var idconcat = newidentfunc(ctconcat,::fconcat)
    var idswoncat = newidentfunc(ctswoncat,::fswoncat)
    var idfind = newidentfunc(ctfind,::ffind)
    var idcount = newidentfunc(ctcount,::fcount)
    var idcollect = newidentfunc(ctcollect,::fcollect)
    var idiota = newidentfunc(ctiota,::fiota)
    var idfromto = newidentfunc(ctfromto,::ffromto)
    var idsum = newidentfunc(ctsum,::fsum)
    var idprod = newidentfunc(ctprod,::fprod)
    var idzip = newidentfunc(ctzip,::fzip)
    var idunlist = newidentfunc(ctunlist,::funlist)
    var idnewdict = newidentfunc(ctnewdict,::fnewdict)
    var idget = newidentfunc(ctget,::fget)
    var idput = newidentfunc(ctput,::fput)
    var idmidstr = newidentfunc(ctmidstr,::fmidstr)
    var idleftstr = newidentfunc(ctleftstr,::fleftstr)
    var idrightstr = newidentfunc(ctrightstr,::frightstr)
    var idindexof = newidentfunc(ctindexof,::findexof)
    var idupper = newidentfunc(ctupper,::fupper)
    var idlower = newidentfunc(ctlower,::flower)
    var idcapitalize = newidentfunc(ctcapitalize,::fcapitalize)
    var idtrim = newidentfunc(cttrim,::ftrim)
    var idtriml = newidentfunc(cttriml,::ftriml)
    var idtrimr = newidentfunc(cttrimr,::ftrimr)
    var idtrimpre = newidentfunc(cttrimpre,::ftrimpre)
    var idchr = newidentfunc(ctchr,::fchr)
    var idord = newidentfunc(ctord,::ford)      // position? ,name?
    var idreplace = newidentfunc(ctreplace,::freplace)
    var idreplace1 = newidentfunc(ctreplace1,::freplace1)
    var idsplit = newidentfunc(ctsplit,::fsplit)
    var idjoin = newidentfunc(ctjoin,::fjoin)
    var idi   = newidentfunc(cti,::fi)
    var iddip = newidentfunc(ctdip,::fdip)
    var iddip2 = newidentfunc(ctdip2,::fdip2)
    var idnullary = newidentfunc(ctnullary,::fnullary)
    var iddo = newidentfunc(ctdo,::fdo)
    var idreturn = newidentfunc(ctreturn,::freturn)
    var idinfra  = newidentfunc(ctinfra,::finfra)
    var idunary  = newidentfunc(ctunary, ::funary)
    var idunary2 = newidentfunc(ctunary2,::funary2)
    var idunary3 = newidentfunc(ctunary3,::funary3)
    var idunary4 = newidentfunc(ctunary4,::funary4)
    var idif = newidentfunc(ctif,::fif)
    var idbranch = newidentfunc(ctbranch,::fbranch)
    var idifte = newidentfunc(ctifte,::fifte)
    var idchoice = newidentfunc(ctchoice,::fchoice)
    var idcase = newidentfunc(ctcase,::fcase)
    var idcond = newidentfunc(ctcond,::fcond)
    var idtimes = newidentfunc(cttimes,::ftimes)
    var idwhile = newidentfunc(ctwhile,::fwhile)
    var idloop = newidentfunc(ctloop,::floop)
    var idbreak = newidentfunc(ctbreak,::fbreak)
    var idstep = newidentfunc(ctstep,::fstep)
    var idmap = newidentfunc(ctmap,::fmap)
    var idfold = newidentfunc(ctfold,::ffold)
    var idfilter = newidentfunc(ctfilter,::ffilter)
    var idsplit2 = newidentfunc(ctsplit2,::fsplit2)
    var idconstr1 = newidentfunc(ctconstr1,::fconstr1)
    var idcleave = newidentfunc(ctcleave,::fcleave)
    var idprimrec = newidentfunc(ctprimrec,::fprimrec)
    var idtailrec = newidentfunc(cttailrec,::ftailrec)
    var idgenrec = newidentfunc(ctgenrec,::fgenrec)
    var idlinrec = newidentfunc(ctlinrec,::flinrec)
    var idbinrec = newidentfunc(ctbinrec,::fbinrec)
    var idtry = newidentfunc(cttry,::ftry)  // test!
    var idabort = newidentfunc(ctabort,::fabort)
    var iderror = newidentfunc(cterror,::ferror)  // position?
    var idsize = newidentfunc(ctsize,::fsize)
    var idunpack = newidentfunc(ctunpack,::funpack)
    var idpack = newidentfunc(ctpack,::fpack)
    var idparse = newidentfunc(ctparse,::fparse)
    var idtostr = newidentfunc(cttostr,::ftostr)
    var idtoval = newidentfunc(cttoval,::ftoval)
    var idtrytoval = newidentfunc(cttrytoval,::ftrytoval)
    var idstrtod = newidentfunc(ctstrtod,::fstrtod)
    //var idhextod = newidentfunc(cthextod,::fhextod)
    //var idtohex = newidentfunc(cttohex,::ftohex)
    var idtimeformat = newidentfunc(cttimeformat,::ftimeformat)
    var ididentlist = newidentfunc(ctidentlist,::fidentlist)
    var ididentdump = newidentfunc(ctidentdump,::fidentdump)
    var idhelpinfo = newidentfunc(cthelpinfo,::fhelpinfo)
    var idgc = newidentfunc(ctgc,::fgc)
    var idtest = newidentfunc(cttest,::ftest)
    var idpen    = identlistPut("pen",Nil())
    var idcolor  = identlistPut("color",Nil())
    //var idsize   = identlistPut("size",Nil())
    var idbrush  = identlistPut("brush",Nil())
    var idcircle = identlistPut("circle",Nil())
    var idrect   = identlistPut("rect",Nil())

    constructor() {  init()  }

    fun init() {
        // identlist = Cons(123,Cons(456.toLong(),xnil))
    }

    fun initElement(x: Int): () -> Unit { return ::fundefined }

    fun newidentfunc(pn: String, fn: ()->Unit ): Ident {
        pcounter = pcounter + 1
        if (pcounter >= maxproc) throw Exception(einitarrayoverflow)
        proc[pcounter] = fn
        val id: Ident = Ident(pn,pcounter)
        identlist = Cons(id,identlist)
        return id  }

    fun identlistPut(pn: String, v: Any): Ident {
        val id: Ident = Ident(pn,v)
        identlist = Cons(id,identlist)
        return id  }

    fun isLong(str: String) = (str.toLongOrNull() != null)
    fun isDouble(str: String) = (str.toDoubleOrNull() != null)

    // true und false extra compilieren !!!
    var ix: Int = 0
    var txt: String = ""
    var item: String = ""
    var cstack: Any = Nil()

// ----- scanner

    fun scanItem(): String {
        var ch: String = " "
        var quit : Boolean
        while ((ix<txt.length) and (ch<=" ")) {
            ch = txt.substring(ix,ix+1)
            ix = ix.inc() }
        if (ch<=" ") return ""
        if (ctspecial.indexOf(ch)>=0) return ch
        ix = ix.dec()
        val k: Int = ix    // val k: Int ,für größere Zahlen ?
        do { ix = ix.inc()
            if (ix>=txt.length) { quit = true }
            else { ch = txt.substring(ix,ix+1)
                quit = ((ctspecial.indexOf(ch)>=0) or (ch<=" ")) }
        } while (!quit)
        return txt.substring(k,ix) }

// ----- compiler

    fun comComment() {
        item = scanItem()
        while (item!=")") {
            if (item=="") throw Exception(ecomcommtunexpend)
            item = scanItem()
        }  }

    fun comComment2() {  ix = txt.length;  item = ""  }

    fun cbackcons(i: Cons): Cons {
        var stack: Cons = i
        var p: Any
        var ref: Any = Nil()
        while (stack.addr!=xmark) {
            p = stack
            stack = stack.decr as Cons
            p.decr = ref
            ref = p  }
        stack.addr = ref
        return stack  }

    fun comList() {
        cstack = Cons(xmark,cstack)
        item = scanItem()
        while (item!="]") {
            when (item) {
                ""       -> throw Exception(ecomlistunexpend)
                "("       -> comComment()
                ")"       -> throw Exception(ecomonlyparenend)
                "["       -> comList()
                // "]"       -> throw Exception(ecomonlybracketend)
                "{"       -> comCurly()
                "}"       -> throw Exception(ecomonlycurlyend)
                ctquote   -> cstack = Cons(idquote,cstack)
                ctquote2  -> comString()
                ctdef     -> cstack = Cons(iddef,cstack) // ??? oder exception oder _s einfügen?
                cttrue    -> cstack = Cons(true,cstack)
                ctfalse   -> cstack = Cons(false,cstack)
                "#"       -> throw Exception(ecom2notallowed)
                else      -> atom(item)  }
            item = scanItem()  }
        cstack = cbackcons(cstack as Cons)  }

    fun comCurly() {
        cstack = Cons("{",cstack)
    }

    fun comString() {
        val k = ix
        do {  ix = ix.inc()  // backslash einbauen!
            if (ix>txt.length) throw Exception(ecomstringunexpend)
        } while (txt.substring(ix-1,ix)!=ctquote2)
        cstack = Cons(txt.substring(k,ix-1),cstack)
    }

    fun findIdent(str: String): Any {
        var seq: Any = identlist
        var found: Any = Nil()
        while ((seq is Cons) and (found is Nil)) {
            if (((seq as Cons).addr as Ident).pname == str)
                found = (seq as Cons).addr
            else seq = seq.decr }
        return found  }

    fun toIdent(str: String): Any {
        val id: Any = findIdent(str)
        // println("find = ${toValue(id)}") // auskommentieren
        if (id is Nil) return identlistPut(str,Nil())
        else return id  }

    fun atom(str: String) {
        if (isDouble(str)) cstack = Cons(str.toDouble(),cstack)
        else cstack = Cons(toIdent(str),cstack)  }

    fun parse(str: String): Any {
        // var ix: Int = 0 // größere Zahlen?
        // var it: String
        // var cstack: Any = Nil()
        txt = str
        ix = 0
        cstack = Nil()
        item = scanItem()
        while (item != "") {
            when (item) {
                //  ""      ---> while
                "("       -> comComment()
                ")"       -> throw Exception(ecomonlyparenend)
                "["       -> comList()
                "]"       -> throw Exception(ecomonlybracketend)
                "{"       -> comCurly()
                "}"       -> throw Exception(ecomonlycurlyend)
                ctquote   -> cstack = Cons(idquote,cstack)
                ctquote2  -> comString()
                ctdef     -> cstack = Cons(iddef,cstack) // ??? oder exception oder _s einfügen?
                cttrue    -> cstack = Cons(true,cstack)
                ctfalse   -> cstack = Cons(false,cstack)
                "#"       -> comComment2()
                else      -> atom(item)  }
            item = scanItem()  }
        txt = ""
        item = ""
        val res: Any = cstack
        cstack = Nil()
        return nreverse(res)
    }  // außerhalb try verwenden?

    fun nreverse(i: Any): Any {
        var n: Any = i
        var p: Any
        var reseq: Any = Nil()
        while (n is Cons) {
            p = n
            n = n.decr
            p.decr = reseq
            reseq = p }
        return reseq  }

    fun creverse(x: Any): Any {
        var r: Any = Nil()
        var i: Any = x
        while (i is Cons) {
            r = Cons(i.addr,r)
            i = i.decr
        }
        return r  }

    fun calc(txt: String) {
        val clist: Any = parse(txt)
        if (clist is Cons) {
            if ((clist.addr is Ident) and (clist.decr is Cons)) {
                val id: Ident = clist.addr as Ident
                val dlist: Cons = clist.decr as Cons
                if (dlist.addr == iddef) {
                    id.info = txt
                    id.body = dlist.decr
                } else run(clist)
            } else run(clist)
        }  } // function-use in try einbetten

// ----- Joy interpreter

    var efun: Any = Nil()
    var eid:  Any = Nil()

    fun eval() {
        if (!runvm) throw Exception(ecalcstop)
        when (efun) {
            is Cons -> {
                var i: Any = efun
                while (i is Cons) {
                    if (!runvm) throw Exception(ecalcstop)
                    efun = i.addr
                    if (quotenext) {  quotenext = false
                        stack = Cons(efun,stack)  }
                    else if (efun !is Ident) stack = Cons(efun,stack)
                    else {
                        eid = efun
                        efun = (efun as Ident).body
                        if (efun is Int) proc[efun as Int]()
                        else if (efun is Cons) eval()
                        else throw Exception(eidentnull+" - "+toValue(eid))
                    }
                    i = i.decr
                }  }
            is Ident -> {
                eid = efun
                efun = (efun as Ident).body
                if (efun is Int) proc[efun as Int]()
                else if (efun is Cons) eval()
                else throw Exception(eidentnull+" - "+toValue(eid))
            }
            is Nil   -> {  }
            else     -> stack = Cons(efun,stack)
        }
        efun = Nil()  }

    fun run(x: Any) {
        runvm = true
        efun = x
        eval()
        runvm = false
        // efun = Nil()
    }

// ----- build-in functions

    fun fundefined() {  throw Exception(efuncundef)  }

    fun fdef()   {  throw Exception(edeferr)  }

    fun fact()   {  stack = Cons(idact,stack)  }     //  ???

    fun fquote() {  quotenext = true  }

    fun fid()    {  }

    fun fstack() {  stack = Cons(stack,stack)  }

    fun funstack() {
        if (stack !is Cons) throw Exception(ctunstack + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctunstack + elistexp)
        stack = x
    }

    fun fdup() {
        if (stack !is Cons) throw Exception(ctdup + estacknull)
        val x = (stack as Cons).addr
        stack = Cons(x,stack)
    }

    fun fpop() {
        if (stack !is Cons) throw Exception(ctpop + estacknull)
        stack = (stack as Cons).decr
    }

    fun fswap() {
        if (stack !is Cons) throw Exception(ctswap + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctswap + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(x,Cons(y,stack))
    }

    fun fover() {
        if (stack !is Cons) throw Exception(ctover + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctover + estacknull)
        val y = (stack as Cons).addr
        //stack = (stack as Cons).decr
        //stack = Cons(y,Cons(x,Cons(y,stack)))
        stack = Cons(y,Cons(x,stack))
    }

    fun frotate() {
        if (stack !is Cons) throw Exception(ctrotate + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctrotate + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctrotate + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(x,Cons(y,Cons(z,stack)))
    }

    fun frollup() {
        if (stack !is Cons) throw Exception(ctrollup + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctrollup + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctrollup + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(y,Cons(x,Cons(z,stack)))
    }

    fun frolldown() {
        if (stack !is Cons) throw Exception(ctrolldown + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctrolldown + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctrolldown + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(x,Cons(z,Cons(y,stack)))
    }

    // d words...

    fun ffirst() {
        if (stack !is Cons) throw Exception(ctfirst + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x is Cons) stack = Cons(x.addr,stack)
        else if (x is Nil) stack = Cons(x,stack)
        else throw Exception(ctfirst + elistexp)
    }

    fun frest() {
        if (stack !is Cons) throw Exception(ctrest + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x is Cons) stack = Cons(x.decr,stack)
        else if (x is Nil) stack = Cons(x,stack)
        else throw Exception(ctrest + elistexp)
    }

    fun fcons() {
        if (stack !is Cons) throw Exception(ctcons + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctcons + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((y is Cons) or (y is Nil))) throw Exception(ctcons + elistexp)
        stack = Cons(Cons(x,y),stack)
    }

    fun funcons() {
        if (stack !is Cons) throw Exception(ctuncons + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Cons) throw Exception(ctuncons + econsexp)
        stack = Cons(x.decr,Cons(x.addr,stack))
    }

    fun fswons() {
        if (stack !is Cons) throw Exception(ctswons + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctswons + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctswons + elistexp)
        stack = Cons(Cons(y,x),stack)
    }

    fun funswons() {
        if (stack !is Cons) throw Exception(ctunswons + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Cons) throw Exception(ctunswons + econsexp)
        stack = Cons(x.addr,Cons(x.decr,stack))
    }

    fun freverse() {
        if (stack !is Cons) throw Exception(ctreverse + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if ((x is Cons) or (x is Nil)) stack = Cons(creverse(x),stack)
        else if (x is String) stack = Cons(x.reversed(),stack)
        else throw Exception(ctreverse + elistorstrexp)
    }

    fun fadd() {
        if (stack !is Cons) throw Exception(ctadd + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctadd + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctadd + efloatexp)
        if (y !is Double) throw Exception(ctadd + efloatexp)
        stack = Cons((x + y),stack)
    }

    fun fsub() {
        if (stack !is Cons) throw Exception(ctsub + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctsub + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctsub + efloatexp)
        if (y !is Double) throw Exception(ctsub + efloatexp)
        stack = Cons((x - y),stack)
    }

    fun fmul() {
        if (stack !is Cons) throw Exception(ctmul + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmul + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctmul + efloatexp)
        if (y !is Double) throw Exception(ctmul + efloatexp)
        stack = Cons((x * y),stack)
    }

    fun fmul2() {
        if (stack !is Cons) throw Exception(ctmul2 + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmul2 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctmul2 + efloatexp)
        if (y !is Double) throw Exception(ctmul2 + efloatexp)
        stack = Cons((x * y),stack)
    }

    fun fdiv() {
        if (stack !is Cons) throw Exception(ctdiv + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctdiv + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctdiv + efloatexp)
        if (y !is Double) throw Exception(ctdiv + efloatexp)
        if (y == 0.0) throw Exception(ctdiv + edivbyzero)
        stack = Cons((x / y),stack)
    }

    fun fdiv2() {
        if (stack !is Cons) throw Exception(ctdiv2 + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctdiv2 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctdiv2 + efloatexp)
        if (y !is Double) throw Exception(ctdiv2 + efloatexp)
        if (y == 0.0) throw Exception(ctdiv2 + edivbyzero)
        stack = Cons((x / y),stack)
    }

    fun fmod() {
        if (stack !is Cons) throw Exception(ctmod + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmod + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctmod + efloatexp)
        if (y !is Double) throw Exception(ctmod + efloatexp)
        val z: Double = x.mod(y)
        stack = Cons(z,stack)
    }

    fun freci() {}

    fun fpow() {
        if (stack !is Cons) throw Exception(ctpow + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctpow + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctpow + efloatexp)
        if (y !is Double) throw Exception(ctpow + efloatexp)
        stack = Cons(Math.pow(x,y),stack)
    }

    fun froot() {}

    fun fpred() {
        if (stack !is Cons) throw Exception(ctpred + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctpred + efloatexp)
        stack = Cons((x - 1.0),stack)
    }

    fun fsucc() {
        if (stack !is Cons) throw Exception(ctsucc + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctsucc + efloatexp)
        stack = Cons((x + 1.0),stack)
    }

    fun fsign() {
        if (stack !is Cons) throw Exception(ctsign + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctsign + efloatexp)
        stack = Cons(Math.signum(x),stack)
    }

    fun fabs() {
        if (stack !is Cons) throw Exception(ctabs + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctabs + efloatexp)
        stack = Cons(Math.abs(x),stack)
    }

    fun fneg() {
        if (stack !is Cons) throw Exception(ctneg + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctneg + efloatexp)
        stack = Cons(-x,stack)
    }

    fun ffloor() {
        if (stack !is Cons) throw Exception(ctfloor + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctfloor + efloatexp)
        stack = Cons(Math.floor(x),stack)
    }

    fun fceil() {
        if (stack !is Cons) throw Exception(ctceil + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctceil + efloatexp)
        stack = Cons(Math.ceil(x),stack)
    }

    fun ftrunc() {
        if (stack !is Cons) throw Exception(cttrunc + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(cttrunc + efloatexp)
        stack = Cons(x.toLong().toDouble(),stack)
    }

    fun fint() {
        if (stack !is Cons) throw Exception(ctint + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctint + efloatexp)
        stack = Cons(x.toLong().toDouble(),stack)
    }

    fun ffrac() {
        if (stack !is Cons) throw Exception(ctfrac + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctfrac + efloatexp)
        stack = Cons(x - (x.toLong().toDouble()),stack)
    }

    fun fround() {
        if (stack !is Cons) throw Exception(ctround + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctround + efloatexp)
        stack = Cons(Math.round(x).toDouble(),stack)
    }

    fun Double.roundTo(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return Math.round(this * multiplier) / multiplier
    }

    fun froundto() {
        if (stack !is Cons) throw Exception(ctroundto + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctroundto + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctroundto + efloatexp)
        if (y !is Double) throw Exception(ctroundto + efloatexp)
        stack = Cons(x.roundTo(Math.round(y).toInt()),stack)
    }

    fun fexp() {
        if (stack !is Cons) throw Exception(ctexp + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctexp + efloatexp)
        stack = Cons(Math.exp(x),stack)
        // Infinity?
    }

    fun flog() {
        if (stack !is Cons) throw Exception(ctlog + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctlog + efloatexp)
        stack = Cons(Math.log(x),stack)
        // -Infinity?
    }

    fun flog10() {
        if (stack !is Cons) throw Exception(ctlog10 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctlog10 + efloatexp)
        stack = Cons(Math.log10(x),stack)
        // -Infinity?
    }

    fun flog2() {
        if (stack !is Cons) throw Exception(ctlog2 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctlog2 + efloatexp)
        stack = Cons(Math.log10(x)/Math.log10(2.0),stack)
        // -Infinity?
    }

    fun fsq() {
        if (stack !is Cons) throw Exception(ctsq + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctsq + efloatexp)
        stack = Cons((x * x),stack)
    }

    fun fsqrt() {
        if (stack !is Cons) throw Exception(ctsqrt + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctsqrt + efloatexp)
        stack = Cons(Math.sqrt(x),stack)
        // NaN?
    }

    fun fcbrt() {
        if (stack !is Cons) throw Exception(ctcbrt + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctcbrt + efloatexp)
        stack = Cons(Math.cbrt(x),stack)
    }

    fun fpi() {  stack = Cons(Math.PI,stack)  }

    fun fsin() {
        if (stack !is Cons) throw Exception(ctsin + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctsin + efloatexp)
        stack = Cons(Math.sin(x),stack)
    }

    fun fcos() {
        if (stack !is Cons) throw Exception(ctcos + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctcos + efloatexp)
        stack = Cons(Math.cos(x),stack)
    }

    fun ftan() {
        if (stack !is Cons) throw Exception(cttan + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(cttan + efloatexp)
        stack = Cons(Math.tan(x),stack)
        // overflow?
    }

    fun fasin() {
        if (stack !is Cons) throw Exception(ctasin + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctasin + efloatexp)
        stack = Cons(Math.asin(x),stack)
        // NaN?
    }

    fun facos() {
        if (stack !is Cons) throw Exception(ctacos + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctacos + efloatexp)
        stack = Cons(Math.acos(x),stack)
        // NaN?
    }

    fun fatan() {
        if (stack !is Cons) throw Exception(ctatan + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctatan + efloatexp)
        stack = Cons(Math.atan(x),stack)
    }

    fun fatan2() {
        if (stack !is Cons) throw Exception(ctatan2 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctatan2 + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (y !is Double) throw Exception(ctatan2 + efloatexp)
        if (x !is Double) throw Exception(ctatan2 + efloatexp)
        stack = Cons(Math.atan2(y,x),stack)
    }

    fun fsinh() {
        if (stack !is Cons) throw Exception(ctsinh + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctsinh + efloatexp)
        stack = Cons(Math.sinh(x),stack)
        // overflow?
    }

    fun fcosh() {
        if (stack !is Cons) throw Exception(ctcosh + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctcosh + efloatexp)
        stack = Cons(Math.cosh(x),stack)
        // overflow?
    }

    fun ftanh() {
        if (stack !is Cons) throw Exception(cttanh + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(cttanh + efloatexp)
        stack = Cons(Math.tanh(x),stack)
        // overflow?
    }

    fun frad() {
        if (stack !is Cons) throw Exception(ctrad + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctrad + efloatexp)
        stack = Cons(Math.toRadians(x),stack)
    }

    fun fdeg() {
        if (stack !is Cons) throw Exception(ctdeg + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctdeg + efloatexp)
        stack = Cons(Math.toDegrees(x),stack)
    }

    fun fnot() {
        if (stack !is Cons) throw Exception(ctnot + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Boolean) throw Exception(ctnot + eboolexp)
        stack = Cons(!x,stack)
    }

    fun fand() {
        if (stack !is Cons) throw Exception(ctand + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctand + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Boolean) throw Exception(ctand + eboolexp)
        if (y !is Boolean) throw Exception(ctand + eboolexp)
        stack = Cons((x and y),stack)
    }

    fun f_or() {
        if (stack !is Cons) throw Exception(ctor + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctor + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Boolean) throw Exception(ctor + eboolexp)
        if (y !is Boolean) throw Exception(ctor + eboolexp)
        stack = Cons((x or y),stack)
    }

    fun fxor() {
        if (stack !is Cons) throw Exception(ctxor + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctxor + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Boolean) throw Exception(ctxor + eboolexp)
        if (y !is Boolean) throw Exception(ctxor + eboolexp)
        stack = Cons((x xor y),stack)
    }

    fun isEq(a: Any, b: Any): Boolean {
        return when (a) {
            is Double  -> (a == b)
            is String  -> (a == b)
            is Ident   -> (a == b)
            is Cons    -> {  if (b !is Cons) false
            else if (!isEq(a.addr,b.addr)) false
            else isEq(a.decr,b.decr)
            }
            is Nil     -> (b is Nil)
            is Boolean -> (a == b)
            else       -> false
        }  }

    fun isLess(a: Any, b: Any, eid: Ident): Boolean {
        return when (a) {
            is Double  -> {  if (b is Double) (a < b)
            else throw Exception(toValue(eid) + enoncompare)  }
            is String  -> {  if (b is String) (a < b)
            else throw Exception(toValue(eid) + enoncompare)  }
            is Ident   -> {  if (b is Ident) (a.pname < b.pname)
            else throw Exception(toValue(eid) + enoncompare)  }
            // is Cons    -> {}
            // is Nil     -> {}
            is Boolean -> {  if (b is Boolean) (a < b)
            else throw Exception(toValue(eid) + enoncompare)  }
            else       -> {  throw Exception(toValue(eid) + enoncompare)  }
        }  }

    fun feq() {
        if (stack !is Cons) throw Exception(cteq + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(cteq + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(isEq(x,y),stack)
    }

    fun fne() {
        if (stack !is Cons) throw Exception(ctne + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctne + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(!isEq(x,y),stack)
    }

    fun fneq() {
        if (stack !is Cons) throw Exception(ctneq + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctneq + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(!isEq(x,y),stack)
    }

    fun flt() {
        if (stack !is Cons) throw Exception(ctlt + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctlt + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val z = isLess(x,y,idlt)
        stack = Cons(z,stack)
    }

    fun fgt() {
        if (stack !is Cons) throw Exception(ctgt + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctgt + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val z = isLess(y,x,idgt)
        stack = Cons(z,stack)
    }

    fun fle() {
        if (stack !is Cons) throw Exception(ctle + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctle + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val z = !isLess(y,x,idle)
        stack = Cons(z,stack)
    }

    fun fge() {
        if (stack !is Cons) throw Exception(ctge + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctge + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val z = !isLess(x,y,idge)
        stack = Cons(z,stack)
    }

    fun fmin() {
        if (stack !is Cons) throw Exception(ctmin + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmin + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (isLess(x,y,idmin)) stack = Cons(x,stack)
        else stack = Cons(y,stack)
    }

    fun fmax() {
        if (stack !is Cons) throw Exception(ctmax + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmax + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (isLess(x,y,idmax)) stack = Cons(y,stack)
        else stack = Cons(x,stack)
    }

    fun inList(x: Any, z: Any): Boolean {
        var i: Any = z
        while (i is Cons) {  if (isEq(x,i.addr)) return true
        else i = i.decr  }
        return false  }

    fun fhas() {
        if (stack !is Cons) throw Exception(cthas + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(cthas + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(cthas + elistexp)
        stack = Cons(inList(y,x),stack)
    }

    fun fin() {
        if (stack !is Cons) throw Exception(ctin + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctin + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((y is Cons) or (y is Nil))) throw Exception(ctin + elistexp)
        stack = Cons(inList(x,y),stack)
    }

    fun fsmall() {
        if (stack !is Cons) throw Exception(ctsmall + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val b: Boolean = when (x) {
            is Nil  -> true
            is Cons -> (x.decr !is Cons)
            0.0     -> true
            1.0     -> true
            else    -> false
        }
        stack = Cons(b,stack)
    }

    fun fnull() {
        if (stack !is Cons) throw Exception(ctnull + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(((x is Nil) or (x == 0.0)),stack)
    }

    fun flist() {
        if (stack !is Cons) throw Exception(ctlist + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(((x is Cons) or (x is Nil)),stack)
    }

    fun fleaf() {
        if (stack !is Cons) throw Exception(ctleaf + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(!((x is Cons) or (x is Nil)),stack)
    }

    fun fbool() {    // name?
        if (stack !is Cons) throw Exception(ctbool + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons((x is Boolean),stack)
    }

    fun fconsp() {
        if (stack !is Cons) throw Exception(ctconsp + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons((x is Cons),stack)
    }

    fun fident() {
        if (stack !is Cons) throw Exception(ctident + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons((x is Ident),stack)
    }

    fun ffloat() {
        if (stack !is Cons) throw Exception(ctfloat + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons((x is Double),stack)
    }

    fun fstring() {
        if (stack !is Cons) throw Exception(ctstring + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons((x is String),stack)
    }

    fun fundef() {
        if (stack !is Cons) throw Exception(ctundef + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons((x == idundef),stack)
    }

    fun getType(x: Any): Any {
        return when (x) {
            is Cons    -> idcons
            is Ident   -> idident
            is Double  -> idfloat
            is String  -> idstring
            is Boolean -> idbool
            is Nil     -> idnull
            is Int     -> "Int"     // ?
            is Long    -> "Long"    // ?
            else       -> idundef
        }  }

    fun ftype() {
        if (stack !is Cons) throw Exception(cttype + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(getType(x),stack)
    }

    fun fname() {
        if (stack !is Cons) throw Exception(ctname + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x is Ident) stack = Cons(x.pname,stack)
        else throw Exception(ctname + eidentexp)
    }

    fun fbody() {
        if (stack !is Cons) throw Exception(ctbody + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Ident) throw Exception(ctbody + eidentexp)
        val y = x.body
        when (y) {
            is Int  -> stack = Cons(y.toDouble(),stack)
            is Cons -> stack = Cons(y,stack)
            else    -> stack = Cons(idundef,stack)
        }  }

    fun finfo() {
        if (stack !is Cons) throw Exception(ctinfo + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x is Ident) stack = Cons(x.info,stack)
        else throw Exception(ctinfo + eidentexp)
    }

    fun fuser() {
        if (stack !is Cons) throw Exception(ctuser + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Ident) throw Exception(ctuser + eidentexp)
        stack = Cons((x.body is Cons),stack)
    }

    fun fbound() {
        if (stack !is Cons) throw Exception(ctbound + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Ident) throw Exception(ctbound + eidentexp)
        stack = Cons((x.body !is Nil),stack)
    }

    fun fintern() {
        if (stack !is Cons) throw Exception(ctintern + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctintern + estringexp)
        stack = Cons(toIdent(x),stack)
    }

    fun findex() {
        if (stack !is Cons) throw Exception(ctindex + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctindex + efloatexp)
        var n: Long = Math.round(x)
        var i: Any = stack
        if (n < 0) throw Exception(ctindex + eoutofrange)
        if (n == 0.toLong()) stack = Cons(x,stack)
        else {
            while ((n > 1) and (i is Cons)) {  i = (i as Cons).decr ; n = n - 1  }
            if (i !is Cons) throw Exception(ctindex + eoutofrange)
            if (n > 1) throw Exception(ctindex + eoutofrange)
            stack = Cons(i.addr,stack)
        }  }

    fun selectAt(a: Any, x: Double, eid: Ident): Any {
        var i: Any = a
        var n: Long = Math.round(x)
        if (n <= 0.toLong()) throw Exception(toValue(eid) + eoutofrange)
        while ((n > 1) and (i is Cons)) {  i = (i as Cons).decr ; n = n - 1  }
        if (i !is Cons) throw Exception(toValue(eid) + eoutofrange)
        if (n > 1) throw Exception(toValue(eid) + eoutofrange)
        return i.addr
    }

    fun fat() {
        if (stack !is Cons) throw Exception(ctat + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctat + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctat + elistexp)
        if (y !is Double) throw Exception(ctat + efloatexp)
        val z: Any = selectAt(x,y,idat)
        stack = Cons(z,stack)
    }

    fun fof() {
        if (stack !is Cons) throw Exception(ctof + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctof + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctof + efloatexp)
        if (!((y is Cons) or (y is Nil))) throw Exception(ctof + elistexp)
        val z: Any = selectAt(y,x,idof)
        stack = Cons(z,stack)
    }

    fun fset() {}

    fun fmake() {
        if (stack !is Cons) throw Exception(ctmake + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmake + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (y !is Double) throw Exception(ctmake + efloatexp)
        var n: Long = Math.round(y)
        var res: Any = Nil()
        while (n > 0) {  res = Cons(x,res) ; n = n - 1  }
        stack = Cons(res,stack)
    }

    fun ftake() {
        if (stack !is Cons) throw Exception(cttake + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(cttake + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(cttake + elistexp)
        if (y !is Double) throw Exception(cttake + efloatexp)
        var n: Long = Math.round(y)
        var i: Any = x
        var res: Any = Nil()
        while ((n > 0) and (i is Cons)) {  res = Cons((i as Cons).addr,res)
            n = n - 1
            i = i.decr
        }
        stack = Cons(nreverse(res),stack)
    }

    fun fdrop() {
        if (stack !is Cons) throw Exception(ctdrop + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctdrop + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctdrop + elistexp)
        if (y !is Double) throw Exception(ctdrop + efloatexp)
        var n: Long = Math.round(y)
        var i: Any = x
        while ((n > 0) and (i is Cons)) {  n = n - 1 ; i = (i as Cons).decr  }
        stack = Cons(i,stack)
    }

    fun fconcat() {
        if (stack !is Cons) throw Exception(ctconcat + estacknull)
        var y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctconcat + estacknull)
        var x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x is String) {
            if (y !is String) throw Exception(ctconcat + estringexp)
            stack = Cons((x + y),stack)
        } else if ((x is Cons) or (x is Nil)) {
            if (!((y is Cons) or (y is Nil))) throw Exception(ctconcat + elistexp)
            var z: Any = Nil()
            while (x is Cons) {  z = Cons(x.addr,z) ; x = x.decr  }
            var p: Any      // y = Restliste
            while (z is Cons) {
                p = z
                z = z.decr
                p.decr = y
                y = p
            }
            stack = Cons(y,stack)
        } else throw Exception(ctconcat + elistorstrexp)
    }

    fun fswoncat() {
        if (stack !is Cons) throw Exception(ctswoncat + estacknull)
        var x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctswoncat + estacknull)
        var y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (y is String) {
            if (x !is String) throw Exception(ctswoncat + estringexp)
            stack = Cons((x + y),stack)
        } else if ((y is Cons) or (y is Nil)) {
            if (!((x is Cons) or (x is Nil))) throw Exception(ctswoncat + elistexp)
            var z: Any = Nil()
            while (x is Cons) {  z = Cons(x.addr,z) ; x = x.decr  }
            var p: Any      // y = Restliste
            while (z is Cons) {
                p = z
                z = z.decr
                p.decr = y
                y = p
            }
            stack = Cons(y,stack)
        } else throw Exception(ctswoncat + elistorstrexp)
    }

    fun ffind() {
        if (stack !is Cons) throw Exception(ctfind + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctfind + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctfind + elistexp)
        var i: Any = x
        var n: Double = 0.0
        while (i is Cons) {
            n = n + 1.0
            if (isEq(i.addr,y)) break
            else i = i.decr
        }
        if (i is Cons) stack = Cons(n,stack)
        else stack = Cons(0.0,stack)
    }

    fun fcount() {
        if (stack !is Cons) throw Exception(ctcount + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctcount + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctcount + elistexp)
        var i: Any = x
        var n: Double = 0.0
        while (i is Cons) {
            if (isEq(i.addr,y)) n = n + 1.0
            i = i.decr
        }
        stack = Cons(n,stack)
    }

    fun fcollect() {
        if (stack !is Cons) throw Exception(ctcollect + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctcollect + efloatexp)
        var n: Long = Math.round(x)
        var z: Any
        var res: Any = Nil()
        while (n > 0) {
            if (stack !is Cons) throw Exception(ctcollect + estacknull)
            z = (stack as Cons).addr
            stack = (stack as Cons).decr
            res = Cons(z,res)
            n = n - 1
        }
        stack = Cons(res,stack)
    }

    fun fiota() {
        if (stack !is Cons) throw Exception(ctiota + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctiota + efloatexp)
        var n: Long = Math.round(x)
        var res: Any = Nil()
        while (n > 0) {  res = Cons(n.toDouble(),res) ; n = n - 1  }
        stack = Cons(res,stack)
    }

    fun ffromto() {
        if (stack !is Cons) throw Exception(ctfromto + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctfromto + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctfromto + efloatexp)
        if (y !is Double) throw Exception(ctfromto + efloatexp)
        val i: Long = Math.round(x)
        var k: Long = Math.round(y)
        var res: Any = Nil()
        if (i <= k)
            while (i <= k) {  res = Cons(k.toDouble(),res) ; k = k - 1  }
        else
            while (i >= k) {  res = Cons(k.toDouble(),res) ; k = k + 1  }
        stack = Cons(res,stack)
    }

    fun fsum() {
        if (stack !is Cons) throw Exception(ctsum + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctsum + elistexp)
        var i: Any = x
        var sum: Double = 0.0
        while (i is Cons) {
            if (i.addr !is Double) throw Exception(ctsum + elistofnumexp)
            sum = sum + (i.addr as Double)
            i = i.decr
        }
        stack = Cons(sum,stack)
    }

    fun fprod() {
        if (stack !is Cons) throw Exception(ctprod + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctprod + elistexp)
        var i: Any = x
        var prod: Double = 1.0
        while (i is Cons) {
            if (i.addr !is Double) throw Exception(ctprod + elistofnumexp)
            prod = prod * (i.addr as Double)
            i = i.decr
        }
        stack = Cons(prod,stack)
    }

    fun fzip() {
        if (stack !is Cons) throw Exception(ctzip + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctzip + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctzip + elistexp)
        if (!((y is Cons) or (y is Nil))) throw Exception(ctzip + elistexp)
        var i: Any = x
        var k: Any = y
        var res: Any = Nil()
        while ((i is Cons) and (k is Cons)) {
            res = Cons(Cons((i as Cons).addr,Cons((k as Cons).addr,Nil())),res)
            i = i.decr
            k = k.decr
        }
        stack = Cons(nreverse(res),stack)
    }

    //  [x y z ... k] unlist  -->  x y z ... k
    fun funlist() {
        if (stack !is Cons) throw Exception(ctunlist + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctunlist + elistexp)
        var i: Any = x
        while (i is Cons) {
            stack = Cons(i.addr,stack)
            i = i.decr
        }  }

    //  x1 x2 ... xn [i1 i2 ... in] newdict  -->  [dict]
    fun fnewdict() {
        if (stack !is Cons) throw Exception(ctnewdict + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((y is Cons) or (y is Nil))) throw Exception(ctnewdict + elistexp)
        var i: Any = creverse(y)
        var x: Any
        var d: Any = Nil()
        while (i is Cons) {
            if (stack !is Cons) throw Exception(ctnewdict + estacknull)
            x = (stack as Cons).addr
            stack = (stack as Cons).decr
            d = Cons(i.addr,Cons(x,d))
            i = i.decr
        }
        stack = Cons(d,stack)
    }

    fun fget() {
        if (stack !is Cons) throw Exception(ctget + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctget + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctget + elistexp)
        //if (y !is Ident) throw Exception(ctget + eidentexp)
        var i: Any = x
        var a: Any
        var z: Any = idundef
        while (i is Cons) {
            a = i.addr
            i = i.decr
            if (i !is Cons) throw Exception(ctget + enonvalue)
            if (a == y) {  z = i.addr ; break  }
            i = i.decr
        }
        stack = Cons(z,stack)
    }

    fun fput() {
        if (stack !is Cons) throw Exception(ctput + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctput + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctput + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctput + elistexp)
        //if (y !is Ident) throw Exception(ctput + eidentexp)
        var i: Any = x
        var a: Any
        var st: Any = Nil()
        while (i is Cons) {
            a = i.addr
            if (a == y) break
            st = Cons(a,st)
            i = i.decr
            if (i !is Cons) throw Exception(ctput + enonvalue)
            st = Cons(i.addr,st)
            i = i.decr
        }
        if (i is Cons) {
            i = i.decr
            if (i !is Cons) throw Exception(ctput + enonvalue)
            i = i.decr
        }
        i = Cons(y,Cons(z,i))  // i = Restliste
        while (st is Cons) {
            a = st
            st = st.decr
            a.decr = i
            i = a
        }
        stack = Cons(i,stack)
    }

    fun fremove() {}  // dicts

    fun fkeys() {}

    fun fvalues() {}

    // concat is also for strings

    //  midstr ?    weil substr(a bis b)
    fun fmidstr() {
        if (stack !is Cons) throw Exception(ctmidstr + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmidstr + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmidstr + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctmidstr + estringexp)
        if (y !is Double) throw Exception(ctmidstr + efloatexp)
        if (z !is Double) throw Exception(ctmidstr + efloatexp)
        val s: String = x
        var i: Int = Math.round(y).toInt()
        var k: Int = Math.round(z).toInt()
        if (i < 1) i = 1
        if (i > s.length) i = s.length + 1
        if (k < 0) k = 0
        if ((i+k) > s.length) k = s.length + 1 - i
        stack = Cons(s.substring(i-1,i+k-1),stack)
    }

    fun fleftstr() {
        if (stack !is Cons) throw Exception(ctleftstr + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctleftstr + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctleftstr + estringexp)
        if (y !is Double) throw Exception(ctleftstr + efloatexp)
        val s: String = x
        var n: Int = Math.round(y).toInt()
        if (n < 0) n = 0
        if (n > s.length) n = s.length
        stack = Cons(s.take(n),stack)
    }

    fun frightstr() {
        if (stack !is Cons) throw Exception(ctrightstr + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctrightstr + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctrightstr + estringexp)
        if (y !is Double) throw Exception(ctrightstr + efloatexp)
        val s: String = x
        var n: Int = Math.round(y).toInt()
        if (n < 0) n = 0
        if (n > s.length) n = s.length
        stack = Cons(s.takeLast(n),stack)
    }

    fun findexof() {
        if (stack !is Cons) throw Exception(ctindexof + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctindexof + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctindexof + estringexp)
        if (y !is String) throw Exception(ctindexof + estringexp)
        stack = Cons((x.indexOf(y)+1).toDouble(),stack)
    }

    fun fupper() {
        if (stack !is Cons) throw Exception(ctupper + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctupper + estringexp)
        val z: String = x.uppercase()
        stack = Cons(z,stack)
    }

    fun flower() {
        if (stack !is Cons) throw Exception(ctlower + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctlower + estringexp)
        val z: String = x.lowercase()
        stack = Cons(z,stack)
    }

    fun fcapitalize() {
        if (stack !is Cons) throw Exception(ctcapitalize + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctcapitalize + estringexp)
        val z: String = x.replaceFirstChar{if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()}
        stack = Cons(z,stack)
    }

    fun ftrim() {
        if (stack !is Cons) throw Exception(cttrim + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cttrim + estringexp)
        val z: String = x.trim()
        stack = Cons(z,stack)
    }

    fun ftriml() {
        if (stack !is Cons) throw Exception(cttriml + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cttriml + estringexp)
        val z: String = x.trimStart()
        stack = Cons(z,stack)
    }

    fun ftrimr() {
        if (stack !is Cons) throw Exception(cttrimr + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cttrimr + estringexp)
        val z: String = x.trimEnd()
        stack = Cons(z,stack)
    }

    fun ftrimpre() {
        if (stack !is Cons) throw Exception(cttrimpre + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(cttrimpre + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cttrimpre + estringexp)
        if (y !is String) throw Exception(cttrimpre + estringexp)
        val z: String = if (y.length > 0) x.trimStart(y[0])
        else x.trimStart()
        stack = Cons(z,stack)
    }

    fun fchr() {
        if (stack !is Cons) throw Exception(ctchr + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(ctchr + efloatexp)
        val z: String = Math.round(x).toInt().toChar().toString()
        stack = Cons(z,stack)
    }

    fun ford() {
        if (stack !is Cons) throw Exception(ctord + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctord + estringexp)
        if (x.length == 0) throw Exception(ctord + estringnull)
        stack = Cons(x[0].code.toDouble(),stack)
    }

    //  string old new replace
    fun freplace() {
        if (stack !is Cons) throw Exception(ctreplace + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctreplace + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctreplace + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctreplace + estringexp)
        if (y !is String) throw Exception(ctreplace + estringexp)
        if (z !is String) throw Exception(ctreplace + estringexp)
        stack = Cons(x.replace(y,z),stack)
    }

    fun freplace1() {
        if (stack !is Cons) throw Exception(ctreplace1 + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctreplace1 + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctreplace1 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctreplace1 + estringexp)
        if (y !is String) throw Exception(ctreplace1 + estringexp)
        if (z !is String) throw Exception(ctreplace1 + estringexp)
        stack = Cons(x.replaceFirst(y,z),stack)
    }

    fun splitTo(str: String, dm: String): Any {
        var i: Int
        var s: String = str
        var list: Any = Nil()
        while (s.length > 0) {
            i = s.indexOf(dm)
            if (i == -1) {  list = Cons(s,list); s = ""  }
            else {  list = Cons(s.substring(0,i),list)
                s = s.substring(i+(dm.length),s.length)
            } }
        return nreverse(list)  }

    fun fsplit() {
        if (stack !is Cons) throw Exception(ctsplit + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctsplit + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctsplit + estringexp)
        if (y !is String) throw Exception(ctsplit + estringexp)
        stack = Cons(splitTo(x,y),stack)
    }

    fun fjoin() {
        if (stack !is Cons) throw Exception(ctjoin + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctjoin + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctjoin + elistexp)
        if (y !is String) throw Exception(ctjoin + estringexp)
        var i: Any = x
        var s: String = ""
        var dm: String = ""
        while (i is Cons) {
            if (i.addr is String) s = s + (dm + i.addr)
            else s = s + (dm + toValue(i.addr))
            dm = y
            i = i.decr
        }
        stack = Cons(s,stack)
    }

    fun fi() {
        if (stack !is Cons) throw Exception(cti + estacknull)
        efun  = (stack as Cons).addr
        stack = (stack as Cons).decr
        eval()  }

    fun fdip() {
        if (stack !is Cons) throw Exception(ctdip + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctdip + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        efun = x
        eval()
        stack = Cons(y,stack)
    }

    fun fdip2() {
        if (stack !is Cons) throw Exception(ctdip2 + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctdip2 + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctdip2 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        efun = z ; eval()
        stack = Cons(y,Cons(x,stack))
    }



    //  <stack> [ ... x ] nullary  --  <stack> x
    fun fnullary() {
        if (stack !is Cons) throw Exception(ctnullary + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        efun = x
        eval()
        if (stack !is Cons) throw Exception(ctnullary + estacknull)
        val res = (stack as Cons).addr
        //stack = (stack as Cons).decr
        stack = Cons(res,st)
    }

    //  <stack> [ ... x return ... x ] do  -- <stack> x
    fun fdo() {
        if (stack !is Cons) throw Exception(ctdo + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        try {  efun = x ; eval()  }
        catch(e: Exception) {
            val s: String = e.message.toString()
            if (s != ereturndo) throw Exception(s)   // mit fbreak correkt hier???
        }
        if (stack !is Cons) throw Exception(ctdo + estacknull)
        val res = (stack as Cons).addr
        //stack = (stack as Cons).decr
        stack = Cons(res,st)
    }

    fun freturn() {  throw Exception(ereturndo)  }


    //  Using list L1 as stack, executes P and returns a new list L2.
    //  The first element of L1 is used as the top of stack,
    //  and after execution of P the top of stack becomes the first element of L2.
    //
    //  (Note that when treating lists as stacks, the first element in the list
    //  will be the top item of the stack.)

    //  L1 [P]  -->  L2
    fun finfra() {
        if (stack !is Cons) throw Exception(ctinfra + estacknull)
        val p = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctinfra + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctinfra + elistexp)
        val st: Any = stack
        stack = x
        efun = p  ;  eval()
        val z: Any = stack
        stack = Cons(z,st)
    }


    //  X [P] -> R
    fun funary() {
        if (stack !is Cons) throw Exception(ctunary + estacknull)
        val p = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        stack = Cons(x,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary + estacknull)
        val r = (stack as Cons).addr
        //stack = (stack as Cons).decr
        stack = Cons(r,st)
    }

    //  X1 X2 [P] -> R1 R2
    fun funary2() {
        if (stack !is Cons) throw Exception(ctunary2 + estacknull)
        val p = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary2 + estacknull)
        val x2 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary2 + estacknull)
        val x1 = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        stack = Cons(x1,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary2 + estacknull)
        val r1 = (stack as Cons).addr
        stack = Cons(x2,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary2 + estacknull)
        val r2 = (stack as Cons).addr
        stack = Cons(r2,Cons(r1,st))
    }


    //  X1 X2 X3 [P] -> R1 R2 R3
    fun funary3() {
        if (stack !is Cons) throw Exception(ctunary3 + estacknull)
        val p = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary3 + estacknull)
        val x3 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary3 + estacknull)
        val x2 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary3 + estacknull)
        val x1 = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        stack = Cons(x1,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary3 + estacknull)
        val r1 = (stack as Cons).addr
        stack = Cons(x2,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary3 + estacknull)
        val r2 = (stack as Cons).addr
        stack = Cons(x3,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary3 + estacknull)
        val r3 = (stack as Cons).addr
        stack = Cons(r3,Cons(r2,Cons(r1,st)))
    }

    //  X1 X2 X3 X4 [P] -> R1 R2 R3 R4
    fun funary4() {
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val p = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val x4 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val x3 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val x2 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val x1 = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        stack = Cons(x1,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val r1 = (stack as Cons).addr
        stack = Cons(x2,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val r2 = (stack as Cons).addr
        stack = Cons(x3,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val r3 = (stack as Cons).addr
        stack = Cons(x4,st)
        efun = p  ;  eval()
        if (stack !is Cons) throw Exception(ctunary4 + estacknull)
        val r4 = (stack as Cons).addr
        stack = Cons(r4,Cons(r3,Cons(r2,Cons(r1,st))))
    }

    fun fif() {
        if (stack !is Cons) throw Exception(ctif + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctif + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctif + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        when (x) {
            true  -> efun = y
            false -> efun = z
            else  -> throw Exception(ctif + eboolexp)
        }
        eval()
    }

    fun fbranch() {
        if (stack !is Cons) throw Exception(ctbranch + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctbranch + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctbranch + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        when (z) {
            true  -> efun = y
            false -> efun = x
            else  -> throw Exception(ctbranch + eboolexp)
        }
        eval()  }

    fun fifte() {
        if (stack !is Cons) throw Exception(ctifte + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctifte + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctifte + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        //if (!((x is Cons) or (x is Nil))) throw Exception(ctifte + elistexp)  // ?
        //if (!((y is Cons) or (y is Nil))) throw Exception(ctifte + elistexp)  // ?
        //if (!((z is Cons) or (z is Nil))) throw Exception(ctifte + elistexp)  // ?
        val st: Any = stack
        efun = x
        eval()
        if (stack !is Cons) throw Exception(ctifte + estacknull)
        val b = (stack as Cons).addr
        stack = (stack as Cons).decr
        when (b) {
            true  -> efun = y
            false -> efun = z
            else  -> throw Exception(ctifte + eboolexp)
        }
        stack = st
        eval()
    }

    fun fchoice() {
        if (stack !is Cons) throw Exception(ctchoice + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctchoice + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctchoice + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        when (x) {
            true  -> stack = Cons(y,stack)
            false -> stack = Cons(z,stack)
            else  -> throw Exception(ctchoice + eboolexp)
        }  }

    fun fcase() {
        if (stack !is Cons) throw Exception(ctcase + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctcase + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((y is Cons) or (y is Nil))) throw Exception(ctcase + elistexp)
        var i: Any = y
        var z: Any
        while (i is Cons) {
            z = i.addr
            if (z !is Cons) throw Exception(ctcase + econsexp)
            if (isEq(z.addr,x)) {  efun = z.decr ; eval() ; break  }
            else i = i.decr
        }  }

    fun fcond() {
        if (stack !is Cons) throw Exception(ctcond + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctcond + elistexp)
        val st: Any = stack
        var i: Any = x
        var z: Any
        var b: Any
        while (i is Cons) {
            z = i.addr
            if (z !is Cons) throw Exception(ctcond + econsexp)
            stack = st ; efun = z.addr ; eval()
            if (stack !is Cons) throw Exception(ctcond + estacknull)
            b = (stack as Cons).addr
            stack = (stack as Cons).decr  // ???
            if (b !is Boolean) throw Exception(ctcond + eboolexp)
            if (b) {  stack = st ; efun = z.decr ; eval() ; break  }
            else i = i.decr
        }  }



    fun ftimes() {     // val st: Any = stack ???
        if (stack !is Cons) throw Exception(cttimes + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(cttimes + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(cttimes + efloatexp)
        //if (!((y is Cons) or (y is Nil))) throw Exception(cttimes + elistexp)  // ?
        var n: Long = Math.round(x)
        while (n > 0) {
            efun = y
            eval()
            n = n - 1
        }  }

    fun fwhile() {     // val st: Any = stack ???
        if (stack !is Cons) throw Exception(ctwhile + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctwhile + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        //if (!((x is Cons) or (x is Nil))) throw Exception(ctwhile + elistexp)  // ?
        //if (!((y is Cons) or (y is Nil))) throw Exception(ctwhile + elistexp)  // ?
        var b: Any = false
        efun = x
        eval()
        if (stack !is Cons) throw Exception(ctwhile + estacknull)
        b = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (b !is Boolean) throw Exception(ctwhile + eboolexp)
        while (b as Boolean) {
            efun = y
            eval()
            efun = x
            eval()
            if (stack !is Cons) throw Exception(ctwhile + estacknull)
            b = (stack as Cons).addr
            stack = (stack as Cons).decr
            if (b !is Boolean) throw Exception(ctwhile + eboolexp)
        }  }

    //  [ ... break ... ] loop                       // correkt
    //  [ ... [ ... x return ... ] loop ... ] do     // correkt
    fun floop() {
        if (stack !is Cons) throw Exception(ctloop + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        try {  while (true) {  efun = x ; eval()  }  }
        catch(e: Exception) {
            val s: String = e.message.toString()
            if (s != ebreakloop) throw Exception(s)
        }  }

    fun fbreak() {  throw Exception(ebreakloop)  }

    fun fstep() {
        if (stack !is Cons) throw Exception(ctstep + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctstep + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctstep + elistexp)
        //if (!((y is Cons) or (y is Nil))) throw Exception(ctstep + elistexp)  // ?
        var i: Any = x
        while (i is Cons) {
            stack = Cons(i.addr,stack)
            efun = y
            eval()
            i = i.decr
        }  }

    fun fmap() {
        if (stack !is Cons) throw Exception(ctmap + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctmap + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctmap + elistexp)
        //if (!((y is Cons) or (y is Nil))) throw Exception(ctmap + elistexp)  // ?
        val st: Any = stack
        var i: Any = x
        var z: Any
        var res: Any = Nil()
        while (i is Cons) {
            stack = Cons(i.addr,st)
            efun = y
            eval()
            if (stack !is Cons) throw Exception(ctmap + estacknull)
            z = (stack as Cons).addr
            //stack = (stack as Cons).decr
            res = Cons(z,res)
            i = i.decr
        }
        stack = Cons(nreverse(res),st)
    }

    // [liste] zero [program] fold
    fun ffold() {
        if (stack !is Cons) throw Exception(ctfold + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctfold + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctfold + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctfold + elistexp)
        //if (!((z is Cons) or (z is Nil))) throw Exception(ctfold + elistexp)  // ?
        val st: Any = stack
        var i: Any = x
        stack = Cons(y,st)
        while (i is Cons) {
            stack = Cons(i.addr,stack)
            efun = z
            eval()
            i = i.decr
        }
        if (stack !is Cons) throw Exception(ctfold + estacknull)
        val res = (stack as Cons).addr
        //stack = (stack as Cons).decr
        stack = Cons(res,st)
    }

    // [liste] [program] filter
    fun ffilter() {
        if (stack !is Cons) throw Exception(ctfilter + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctfilter + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctfilter + elistexp)
        //if (!((y is Cons) or (y is Nil))) throw Exception(ctfilter + elistexp)  // ?
        val st: Any = stack
        var i: Any = x
        var b: Any
        var res: Any = Nil()
        while (i is Cons) {
            stack = Cons(i.addr,st)
            efun = y
            eval()
            if (stack !is Cons) throw Exception(ctfilter + estacknull)
            b = (stack as Cons).addr
            stack = (stack as Cons).decr  // ?
            if (b !is Boolean) throw Exception(ctfilter + eboolexp)
            if (b) {  res = Cons(i.addr,res)  }
            i = i.decr
        }
        stack = Cons(nreverse(res),st)
    }

    //  A [B] -> A1 A2
    fun fsplit2() {
        if (stack !is Cons) throw Exception(ctsplit2 + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctsplit2 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctsplit2 + elistexp)
        val st: Any = stack
        var i: Any = x
        var b: Any
        var a1: Any = Nil()
        var a2: Any = Nil()
        while (i is Cons) {
            stack = Cons(i.addr,st)
            efun = y
            eval()
            if (stack !is Cons) throw Exception(ctsplit2 + estacknull)
            b = (stack as Cons).addr
            stack = (stack as Cons).decr  // ?
            if (b !is Boolean) throw Exception(ctsplit2 + eboolexp)
            if (b) {  a1 = Cons(i.addr,a1)  }
            else   {  a2 = Cons(i.addr,a2)  }
            i = i.decr
        }
        stack = Cons(nreverse(a2),Cons(nreverse(a1),st))
    }

    //  x [[p1] [p2] ... [pn]] constr1  -->  list
    fun fconstr1() {
        if (stack !is Cons) throw Exception(ctconstr1 + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctconstr1 + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((y is Cons) or (y is Nil))) throw Exception(ctconstr1 + elistexp)
        val st: Any = stack
        var i: Any = y
        var z: Any
        var res: Any = Nil()
        while (i is Cons) {
            stack = Cons(x,st)
            efun = i.addr  ;  eval()
            if (stack !is Cons) throw Exception(ctconstr1 + estacknull)
            z = (stack as Cons).addr
            //stack = (stack as Cons).decr  // ???
            res = Cons(z,res)
            i = i.decr
        }
        stack = Cons(nreverse(res),st)
    }

    // X [P1] [P2] -> R1 R2
    fun fcleave() {
        if (stack !is Cons) throw Exception(ctcleave + estacknull)
        val z = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctcleave + estacknull)
        val y = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctcleave + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        //if (!((y is Cons) or (y is Nil))) throw Exception(ctcleave + elistexp)  // ?
        //if (!((z is Cons) or (z is Nil))) throw Exception(ctcleave + elistexp)  // ?
        val st: Any = stack
        stack = Cons(x,st)
        efun = y
        eval()
        if (stack !is Cons) throw Exception(ctcleave + estacknull)
        val r1 = (stack as Cons).addr
        // stack = (stack as Cons).decr
        stack = Cons(x,st)
        efun = z
        eval()
        if (stack !is Cons) throw Exception(ctcleave + estacknull)
        val r2 = (stack as Cons).addr
        // stack = (stack as Cons).decr
        stack = Cons(r2,Cons(r1,st))
    }

    fun primrecnum(n: Double, x: Double, i: Any, c: Any) {
        if (n<=x) {
            stack = Cons(n,stack)
            primrecnum((n+1.0),x,i,c)
            efun = c ; eval()
        } else {  efun = i ; eval()  }
    }

    fun primreclist(k: Any, i: Any, c: Any) {
        if (k is Cons) {
            stack = Cons(k.addr,stack)
            primreclist(k.decr,i,c)
            efun = c ; eval()
        } else {  efun = i ; eval()  }
    }

    fun fprimrec() {
        if (stack !is Cons) throw Exception(ctprimrec + estacknull)
        val c = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctprimrec + estacknull)
        val i = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctprimrec + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        if (x is Double) {
            val z: Double = Math.round(x).toDouble()
            val n: Double = 1.0
            primrecnum(n,z,i,c)
        } else if ((x is Cons) or (x is Nil)) {
            primreclist(x,i,c)
        } else throw Exception(ctprimrec + elistornumexp)
        if (stack !is Cons) throw Exception(ctprimrec + estacknull)
        val r = (stack as Cons).addr
        //stack = (stack as Cons).decr
        stack = Cons(r,st)
    }

    //  [P] [T] [R1] tailrec -> ...
    fun ftailrec() {
        if (stack !is Cons) throw Exception(cttailrec + estacknull)
        val r1 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(cttailrec + estacknull)
        val t = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(cttailrec + estacknull)
        val p = (stack as Cons).addr
        stack = (stack as Cons).decr
        var b: Any
        while (true) {
            efun = p ; eval()
            if (stack !is Cons) throw Exception(cttailrec + estacknull)
            b = (stack as Cons).addr
            stack = (stack as Cons).decr
            if (b !is Boolean) throw Exception(cttailrec + eboolexp)
            if (b) {  efun = t  ; eval() ; break  }
            else   {  efun = r1 ; eval()  }
        }  }

    fun fgenrec() {
        if (stack !is Cons) throw Exception(ctgenrec + estacknull)
        val be2 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctgenrec + estacknull)
        val be1 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctgenrec + estacknull)
        val bth = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctgenrec + estacknull)
        val bif = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((bif is Cons) or (bif is Nil))) throw Exception(ctgenrec + elistexp)
        if (!((bth is Cons) or (bth is Nil))) throw Exception(ctgenrec + elistexp)
        if (!((be1 is Cons) or (be1 is Nil))) throw Exception(ctgenrec + elistexp)
        if (!((be2 is Cons) or (be2 is Nil))) throw Exception(ctgenrec + elistexp)
        val st: Any = stack
        efun = bif ; eval()
        if (stack !is Cons) throw Exception(ctgenrec + estacknull)
        val b = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (b !is Boolean) throw Exception(ctgenrec + eboolexp)
        stack = st
        if (b) {  efun = bth ; eval()  }
        else {
            efun = be1 ; eval()
            stack = Cons(Cons(bif,Cons(bth,Cons(be1,Cons(be2,Cons(idgenrec,Nil()))))),stack)
            efun = be2 ; eval()
        }  }

    fun flinrec() {
        if (stack !is Cons) throw Exception(ctlinrec + estacknull)
        val be2 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctlinrec + estacknull)
        val be1 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctlinrec + estacknull)
        val bth = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctlinrec + estacknull)
        val bif = (stack as Cons).addr
        stack = (stack as Cons).decr
        //if (!((bif is Cons) or (bif is Nil))) throw Exception(ctlinrec + elistexp)
        //if (!((bth is Cons) or (bth is Nil))) throw Exception(ctlinrec + elistexp)
        //if (!((be1 is Cons) or (be1 is Nil))) throw Exception(ctlinrec + elistexp)
        //if (!((be2 is Cons) or (be2 is Nil))) throw Exception(ctlinrec + elistexp)
        val st: Any = stack
        efun = bif ; eval()
        if (stack !is Cons) throw Exception(ctlinrec + estacknull)
        val b = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (b !is Boolean) throw Exception(ctlinrec + eboolexp)
        stack = st
        if (b) {  efun = bth ; eval()  }
        else {
            efun = be1 ; eval()
            stack = Cons(be2,Cons(be1,Cons(bth,Cons(bif,stack))))
            flinrec()
            efun = be2 ; eval()
        }  }

    fun fbinrec() {
        if (stack !is Cons) throw Exception(ctbinrec + estacknull)
        val be2 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctbinrec + estacknull)
        val be1 = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctbinrec + estacknull)
        val bth = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (stack !is Cons) throw Exception(ctbinrec + estacknull)
        val bif = (stack as Cons).addr
        stack = (stack as Cons).decr
        //if (!((bif is Cons) or (bif is Nil))) throw Exception(ctbinrec + elistexp)
        //if (!((bth is Cons) or (bth is Nil))) throw Exception(ctbinrec + elistexp)
        //if (!((be1 is Cons) or (be1 is Nil))) throw Exception(ctbinrec + elistexp)
        //if (!((be2 is Cons) or (be2 is Nil))) throw Exception(ctbinrec + elistexp)
        val st: Any = stack
        efun = bif ; eval()
        if (stack !is Cons) throw Exception(ctbinrec + estacknull)
        val b = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (b !is Boolean) throw Exception(ctbinrec + eboolexp)
        stack = st
        if (b) {  efun = bth ; eval()  }
        else {
            efun = be1 ; eval()
            //val reQt = Cons(bif,Cons(bth,Cons(be1,Cons(be2,Cons(idbinrec,Nil())))))
            val y = (stack as Cons).addr
            stack = (stack as Cons).decr
            //val x = (stack as Cons).addr
            //stack = (stack as Cons).decr
            //
            //stack = Cons(x,stack)
            stack = Cons(be2,Cons(be1,Cons(bth,Cons(bif,stack))))
            fbinrec()
            //efun = reQt ; eval()
            stack = Cons(y,stack)
            stack = Cons(be2,Cons(be1,Cons(bth,Cons(bif,stack))))
            fbinrec()
            //efun = reQt ; eval()
            efun = be2 ; eval()
        }  }

    // [program] try -- stack  []/"..."           (test!)
    fun ftry() {
        if (stack !is Cons) throw Exception(cttry + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        val st: Any = stack
        try {
            efun = x
            eval()
            stack = Cons(Nil(),Cons(stack,st))
        } catch (e: Exception) {
            val s: String = e.message.toString()
            if (s == ecalcstop) throw Exception(ecalcstop)
            else stack = Cons(s,Cons(stack,st))
        }  }

    fun fabort() {
        runvm = false
        throw Exception(ecalcstop)
    }

    fun ferror() {
        if (stack !is Cons) throw Exception(cterror + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cterror + estringexp)
        throw Exception(x)
    }

    fun fsize() {
        if (stack !is Cons) throw Exception(ctsize + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        when (x) {
            is Cons   -> {  var len: Long = 0
                var i: Any = x
                while (i is Cons) {  len = len + 1 ; i = i.decr  }
                stack = Cons(len.toDouble(),stack)
            }
            is String -> stack = Cons(x.length.toDouble(),stack)
            is Nil    -> stack = Cons(0.0,stack)
            else      -> throw Exception(ctsize + elistorstrexp)
        }  }

    fun funpack() {
        if (stack !is Cons) throw Exception(ctunpack + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctunpack + estringexp)
        val s: String = x
        var n: Int = s.length
        var res: Any = Nil()
        while (n > 0) {  res = Cons(s.substring(n-1,n),res) ; n = n - 1  }
        stack = Cons(res,stack)
    }

    fun fpack() {
        if (stack !is Cons) throw Exception(ctpack + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (!((x is Cons) or (x is Nil))) throw Exception(ctpack + elistofstrexp)
        var i: Any = x
        var s: String = ""
        while (i is Cons) {
            if (i.addr !is String) throw Exception(ctpack + elistofstrexp)
            s = s + i.addr
            i = i.decr
        }
        stack = Cons(s,stack)
    }

    fun fparse() {
        if (stack !is Cons) throw Exception(ctparse + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctparse + estringexp)
        val res: Any = parse(x)
        stack = Cons(res,stack)
    }

    fun ftostr() {
        if (stack !is Cons) throw Exception(cttostr + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        stack = Cons(toValue(x),stack)
    }

    fun ftoval() {
        if (stack !is Cons) throw Exception(cttoval + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cttoval + estringexp)
        val z: Any = parse(x)
        if (z is Cons) stack = Cons(z.addr,stack)
        else stack = Cons(idundef,stack)
    }

    fun ftrytoval() {
        if (stack !is Cons) throw Exception(cttrytoval + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cttrytoval + estringexp)
        try {
            val z: Any = parse(x)
            if (z is Cons) stack = Cons(Nil(),Cons(z.addr,stack))
            else stack = Cons(Nil(),Cons(idundef,stack))
        } catch(e: Exception) {
            stack = Cons(e.message.toString(),Cons(idundef,stack))
        }  }

    fun fstrtod() {
        if (stack !is Cons) throw Exception(ctstrtod + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(ctstrtod + estringexp)
        if (isDouble(x)) stack = Cons(x.toDouble(),stack)
        else stack = Cons(idundef,stack)
    }

    /*
    fun fhextod() {
        if (stack !is Cons) throw Exception(cthextod + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is String) throw Exception(cthextod + estringexp)
        try {
            val z: Double = x.hexToLong().toDouble()
            stack = Cons(z,stack)
        } catch(e: Exception) {
            stack = Cons(idundef,stack)
        }  }
    */

    /*
    fun ftohex() {
        if (stack !is Cons) throw Exception(cttohex + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(cttohex + efloatexp)
        var z: String = x.toLong().toHexString().trimStart('0')
        if (z.isEmpty()) z = "0"
        stack = Cons(z,stack)
    }
    */

    fun ftimeformat() {
        if (stack !is Cons) throw Exception(cttimeformat + estacknull)
        val x = (stack as Cons).addr
        stack = (stack as Cons).decr
        if (x !is Double) throw Exception(cttimeformat + efloatexp)
        val n: Long = Math.round(x)
        val sdf = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
        stack = Cons(sdf.format(n),stack)
    }

    fun fidentlist() {  stack = Cons(identlist,stack)  }  // SideEffect -> words

    fun fidentdump() {
        var i: Any = identlist
        var a: Any
        var s: String = ""
        var ln: String = ""
        while (i is Cons) {
            a = i.addr
            if (a is Ident) {
                when (a.body) {
                    is Nil  -> s += ln + a.pname
                    is Int  -> s += ln + a.pname + " == " + toValue(a.body)
                    is Cons -> s += ln + a.pname + " == " + toSequence(a.body)
                    else    -> s += ln + a.pname + " == " + "(???)"
                }  }
            ln = "\n"
            i = i.decr
        }
        stack = Cons(s,stack)     // SideEffect -> dump
    }

    fun fhelpinfo() {  stack = Cons(helpurl,stack)  }  // SideEffect -> help

    /*
    fun calc(txt: String) {
        val clist: Any = parse(txt)
        if (clist is Cons) {
            if ((clist.addr is Ident) and (clist.decr is Cons)) {
                val id: Ident = clist.addr as Ident
                val dlist: Cons = clist.decr as Cons
                if (dlist.addr == iddef) {
                    id.info = txt
                    id.body = dlist.decr
                } else run(clist)
            } else run(clist)
        }  } // function-use in try einbetten

    */
    fun deflines(lines: Any): String {
        var list = lines
        var line: Any
        var clist: Any
        var id: Ident
        var dlist: Cons
        var res: String = ""
        var ln: String = ""
        try {
            while (list is Cons) {
                line = list.addr
                list = list.decr
                if (line is String) {
                    clist = parse(line)
                    if (clist is Cons) {
                        if ((clist.addr is Ident) and (clist.decr is Cons)) {
                            id = clist.addr as Ident
                            dlist = clist.decr as Cons
                            if (dlist.addr == iddef) {
                                id.info = line
                                id.body = dlist.decr
                                res = res + (ln + id.pname+" == "+toSequence(id.body))
                                ln = "\n"
                            } else {}
                        } else {}
                    } else {}
                } else throw Exception(ctdeflines + elistofstrexp)
            }
            return res
        } catch(e: Exception) {  return "ERROR: "+e.message  }
    }

    fun ftest() {
        //val z = Date.from(Instant.now()).toString()
        //stack = Cons(z,stack)
    }

    fun fgc() {  System.gc()  }

    fun prelude(): String {
        return  "\n" +
                "trans == dup first [ ] = [pop [ ]] [dup [first] map swap [rest] map trans cons] if\n" +
                "set == 2 index 0 <= [pop pop pop ' undef] [setrec] if\n" +
                "setrec == 2 index 1 = [3 index [] = [dup []] [dup 4 index rest] if cons] [3 index [] = " +
                "[[] 4 index 4 index 1 - 4 index] [3 index first 4 index rest 4 index 1 - 4 index] " +
                "if setrec cons] if [pop pop pop] dip\n" +
                //"collect == [ ] swap [cons] times\n" +
                "copy == dup [dup 1 + index swap] times pop\n" +
                "ifundef  == [undef] dip2 if\n" +
                "ifstring == [string] dip2 if\n" +
                "iffloat == [float] dip2 if\n" +
                "ifident == [ident] dip2 if\n" +
                "ifbool  == [bool] dip2 if\n" +
                "ifcons  == [consp] dip2 if\n" +
                "iflist  == [list] dip2 if\n" +
                "ifnull  == [null] dip2 if\n" +
                "restr == dup size pred take\n" +    // in Doku umbenennen von init
                "last == dup size at\n" +
                "rolldownd == [rolldown] dip\n" +
                "rollupd == [rollup] dip\n" +
                "rotated == [rotate] dip\n" +
                "swapd == [swap] dip\n" +
                "popd == [pop] dip\n" +
                "dupd == [dup] dip\n" +
                "cat == concat\n" +
                "rem == mod\n" +
                "root == 1 swap / pow\n" +   // 27 3 root ???
                "reci == 1 swap /\n" +
                "fact == iota 1 [*] fold\n" +  // prod ?
                "Y == dup [ ] cons [Y] concat swap i\n" +
                "clear == [ ] unstack\n" +  // newstack ?
                "enconcat == [swap] dip cons concat\n" +
                "qsort == [small] [ ] [uncons [>] split2] [enconcat] binrec\n" +
                "pair == [ ] cons cons\n" +
                "unpair == uncons uncons pop\n" +
                ".s == stack reverse print\n" +
                ". == 1 [ ] '!\n" +
                "print == 2 [ ] '!\n" +
                "load  == 3 [ ] '!\n" +
                "save  == 4 [ ] '!\n" +
                "loadtext == 5 [ ] '!\n" +
                "savetext == 6 [ ] '!\n" +
                "files == 7 [ ] '!\n" +
                "fremove == 8 [ ] '!\n" +
                "fcopyto == swap [loadtext] [savetext] '!\n" +
                //"frename == 9 [ ] '!\n" +
                "timestamp == 10 [ ] '!\n" +
                "date == 11 [ ] '!\n" +
                "# viewurl == 12 [ ] '!\n" +
                "# quit == 13 [ ] '!\n" +
                "# input == 14 [ ] '!\n" +
                "run == 15 [ ] '!\n" +
                "showgraph == 16 [ ] '!\n" +
                "dump  == identdump print\n" +
                "words == identlist print\n" +
                "help  == \"JoyOfPostfix.bat\" run\n" +
                "# google == \"www.google.de\" viewurl\n" +
                "offs == id\n" +
                "2pi == pi pi +\n" +
                "init ( -- turtle) == [stack [] x 0 y 0 angle 0 pen true color 0 size 1 brush 16777215]\n" +
                "draw (turtle -- ) == 'stack get reverse showgraph\n" +
                "moveto (dict x y -- dict) == rotate dup 'stack get 3 index swons 4 index swons" +
                "   'stack swap put swap 'x swap put swap 'y swap put\n" +
                "moverel (dict relx rely -- dict) == 3 index 'y get + swap 3 index 'x get + swap moveto\n" +
                "move (dict rel -- dict) == 2 index 'angle get(dict rel angle)" +
                "                           dup cos 3 index * 4 index 'x get + " +
                "                           2 index sin 4 index * 5 index 'y get +" +
                "                           rotate pop rotate pop (dict   sumrelbogx sumrelbogy) moveto\n" +
                "turnto (dict ang -- dict) == offs doturnto\n" +
                "doturnto == dup 2pi / int 2pi * - 'angle swap put\n" +
                "turn (dict ang -- dict) == offs 2 index 'angle get + doturnto\n" +
                "penup (dict -- dict) == dup 'stack get 'pen swons false swons 'stack swap put 'pen false put\n" +
                "pendown (dict -- dict) == dup 'stack get 'pen swons true swons 'stack swap put 'pen true put\n" +
                "pencolor (dict col -- dict) == swap dup 'stack get 'color swons 3 index swons 'stack swap put swap 'color swap put\n" +
                "pensize (dict size -- dict) == swap dup 'stack get 'size swons 3 index swons 'stack swap put swap 'size swap put\n" +
                "brushcolor (dict col -- dict) == swap dup 'stack get 'brush swons 3 index swons 'stack swap put swap 'brush swap put\n" +
                "circle (dict rad -- dict) == swap dup 'stack get 'circle swons swap rotate swons 'stack swap put\n" +
                "rectangle (dict -- dict) == dup 'stack get 'rect swons [] swons 'stack swap put (mit rect)\n" +
                "colors == [red 255 black 0 blue 16711680 white 16777215 green 32768 aqua 16776960" +
                "   darkgray 8421504 fuchsia 16711935 gray 8421504 lime 65280 lightgray 12632256 maroon 128" +
                "   navy 8388608 olive 32896 purple 8388736 silver 12632256 teal 8421376 yellow 65535 gold 55295 orange 42495]\n" +
                "start == init penup 250 -250 moveto pendown 90 rad turnto\n" +
                "; == dup (to copy the last drawing) dup 'pen get [turtle] [] if draw\n" +
                "turtle == 1 pensize colors 'red get pencolor 120 rad turn 12 move 210 rad turn 20 move -120 rad turn 20 move " +
                "210 rad turn 11 move\n" +
                "cs == start ;\n"
    }

}  // class JoyVM

// ----- constants
const val cterrorcol  = "ERROR:   "
const val ctnullpar   = "(null)"
const val ctjoypath   = "Joy-Files"

// ----- for monad side-effects
const val ctdot       = "."
const val ctprint     = "print"
const val ctload      = "load"
const val ctsave      = "save"
const val ctloadtext  = "loadtext"
const val ctsavetext  = "savetext"
const val ctfremove   = "fremove"
const val ctfrename   = "frename"
const val ctviewurl   = "viewurl"
const val ctrun       = "run"
const val ctshowgraph = "showgraph"

// ----- error messages
const val errinfname  =      "  >>>  error in filename"
const val errnofile   =      "  >>>  file not found"
const val edoacterr   = "doAct  >>>  can't react to this number"

var vm: JoyVM = JoyVM()
var itxt: String = vm.prelude()
var otxt: String = vm.deflines(vm.splitTo(itxt,"\n"))
var jquit: Boolean = false
var line: String = ""

var intfstate = TextFieldState()
var outtfstate = TextFieldState()


fun isTopIdent(st: Any, id: Ident): Boolean {
    if (st is Cons) return (st.addr == id)
    else return false
}

fun lastName(s: String): String {
    var i: Int = s.length
    var ch: Char = ' '
    while (i > 0) {
        ch = s[i-1]
        if ((ch == ':') or (ch == '\\') or (ch == '/')) break
        else i = i - 1
    }
    return s.substring(i,s.length)
}

/*
fun splitTo(str: String, dm: String): Any {
    var i: Int
    var s: String = str
    var list: Any = Nil()
    while (s.length > 0) {
        i = s.indexOf(dm)
        if (i == -1) {  list = Cons(s,list); s = ""  }
        else {  list = Cons(s.substring(0,i),list)
            s = s.substring(i+(dm.length),s.length)
        } }
    return nreverse(list)  }
*/

fun splitLines(str: String): Any {
    var i: Int = str.length
    var k: Int = 0
    var ch: Char = ' '
    var list: Any = Nil()
    while (i > 0) {
        k = i
        while (i > 0) {
            ch = str[i-1]
            if ((ch == 13.toChar()) or (ch == 10.toChar())) break
            else i = i - 1
        }
        list = Cons(str.substring(i,k),list)
        if (i == 0) break
        if (str[i-1] == 10.toChar()) i = i - 1
        if (i == 0) break
        if (str[i-1] == 13.toChar()) i = i - 1
    }
    return list
}

/*
fun `Given a command, When executed with ProcessBuilder, Then it is executed successfully`() {
    val result = ProcessBuilder("java", "-version")
      .redirectOutput(ProcessBuilder.Redirect.INHERIT)
      .redirectError(ProcessBuilder.Redirect.INHERIT)
      .start()
      .waitFor()
    assertThat(result).isEqualTo(0)
}
*/
/*
fun String.runCommand(workingDir: File) {
    ProcessBuilder(*split(" ").toTypedArray())
        .directory(workingDir)
        .redirectOutput(Redirect.INHERIT)
        .redirectError(Redirect.INHERIT)
        .start()
        .waitFor(60, TimeUnit.MINUTES)
}
*/

fun doAct() {
    if (stack !is Cons) throw Exception(ctact + estacknull)
    val z = (stack as Cons).addr
    stack = (stack as Cons).decr
    if (stack !is Cons) throw Exception(ctact + estacknull)
    val y = (stack as Cons).addr
    stack = (stack as Cons).decr
    if (stack !is Cons) throw Exception(ctact + estacknull)
    val x = (stack as Cons).addr
    stack = (stack as Cons).decr
    if (z !is Ident) throw Exception(ctact + eidentexp)
    if (!((y is Cons) or (y is Nil))) throw Exception(ctact + elistexp)
    if ((x is Cons) or (x is Nil)) {
        vm.efun = x
        vm.eval()
    } else {
        if (x !is Double) throw Exception(ctact + elistornumexp)
        val n: Long = Math.round(x)
        when (n) {
            1.toLong()  -> {  // dot
                if (stack !is Cons) throw Exception(ctdot + ctact + estacknull)
                val i = (stack as Cons).addr
                stack = (stack as Cons).decr
                outtfstate.setTextAndPlaceCursorAtEnd(toValue(i))
                //println(toValue(i))
            }
            2.toLong()  -> {  //  print
                if (stack !is Cons) throw Exception(ctprint+ctact + estacknull)
                val i = (stack as Cons).addr
                stack = (stack as Cons).decr
                when (i) {
                    is Cons   -> outtfstate.setTextAndPlaceCursorAtEnd(toSequence(i))
                    is String -> outtfstate.setTextAndPlaceCursorAtEnd(i)
                    is Nil    -> outtfstate.setTextAndPlaceCursorAtEnd(ctnullpar)    // ???
                    else      -> outtfstate.setTextAndPlaceCursorAtEnd(toValue(i))
                }
            }
            3.toLong()  -> {  // load
                if (stack !is Cons) throw Exception(ctload+ctact + estacknull)
                val fnm = (stack as Cons).addr
                stack = (stack as Cons).decr
                val rname = when (fnm) {
                    is Ident  -> lastName(fnm.pname)
                    is String -> lastName(fnm)
                    else      -> ""     }
                if (rname=="") throw Exception(ctload+ctact + errinfname)
                val rpath = Paths.get("").toAbsolutePath().toString() // applicationContext.filesDir
                val rdir = File(rpath,ctjoypath)
                if (!rdir.exists()) rdir.mkdir()
                val rfile = File(rdir, rname)
                if (!rfile.exists()) throw Exception(ctload+ctact + errnofile)
                val rtxt = rfile.readText()

                //vm = JoyVM()  // =>vergisst Daten???
                //itxt = vm.prelude()
                //otxt = vm.deflines(vm.splitTo(itxt,"\n"))
                //et1.setText(rtxt)

                val txt = vm.deflines(splitLines(rtxt))
                intfstate.setTextAndPlaceCursorAtEnd(rtxt)
                //stack = Cons(splitLines(rtxt),stack)
                // runOnUiThread {  et1.setText(rtxt)  }
            }
            4.toLong()  -> {  // save
                if (stack !is Cons) throw Exception(ctsave+ctact + estacknull)
                val fnm = (stack as Cons).addr
                stack = (stack as Cons).decr
                val wname = when (fnm) {
                    is Ident  -> lastName(fnm.pname)
                    is String -> lastName(fnm)
                    else      -> ""     }
                if (wname=="") throw Exception(ctsave+ctact + errinfname)
                val wpath = Paths.get("").toAbsolutePath().toString() // applicationContext.filesDir
                val wdir = File(wpath,ctjoypath)
                if (!wdir.exists()) wdir.mkdir()
                val wfile = File(wdir,wname)
                val wtxt: String = intfstate.text.toString() // et1.text.toString()
                wfile.writeText(wtxt)
            }
            5.toLong()  -> {  // loadtext
                if (stack !is Cons) throw Exception(ctloadtext+ctact + estacknull)
                val fnm = (stack as Cons).addr
                stack = (stack as Cons).decr
                val rname = when (fnm) {
                    is Ident  -> lastName(fnm.pname)
                    is String -> lastName(fnm)
                    else      -> ""     }
                if (rname=="") throw Exception(ctloadtext+ctact + errinfname)
                val rpath = Paths.get("").toAbsolutePath().toString() // applicationContext.filesDir
                val rdir = File(rpath,ctjoypath)
                if (!rdir.exists()) rdir.mkdir()
                val rfile = File(rdir, rname)
                if (!rfile.exists()) throw Exception(ctloadtext+ctact + errnofile)
                val rtxt = rfile.readText()
                stack = Cons(rtxt,stack)
            }
            6.toLong()  -> {  // savetext
                if (stack !is Cons) throw Exception(ctsavetext+ctact + estacknull)
                val str = (stack as Cons).addr
                stack = (stack as Cons).decr
                if (stack !is Cons) throw Exception(ctsavetext+ctact + estacknull)
                val fnm = (stack as Cons).addr
                stack = (stack as Cons).decr
                if (str !is String) throw Exception(ctsavetext+ctact + estringexp)
                val wname = when (fnm) {
                    is Ident  -> lastName(fnm.pname)
                    is String -> lastName(fnm)
                    else      -> ""      }
                if (wname=="") throw Exception(ctsavetext+ctact + errinfname)
                val wpath = Paths.get("").toAbsolutePath().toString() // applicationContext.filesDir
                val wdir = File(wpath,ctjoypath)
                if (!wdir.exists()) wdir.mkdir()
                val wfile = File(wdir,wname)
                wfile.writeText(str)
            }
            7.toLong()  -> {  // files
                val fpath = Paths.get("").toAbsolutePath().toString() //applicationContext.filesDir
                val fdir = File(fpath,ctjoypath)
                if (!fdir.exists()) fdir.mkdir()
                val flist = fdir.listFiles()
                var f: Any = Nil()
                var s: String
                flist?.forEach{
                    s = lastName(it.toString())
                    f = Cons(s,f)  }
                stack = Cons(vm.nreverse(f),stack)
            }
            8.toLong()  -> {  // fremove
                if (stack !is Cons) throw Exception(ctfremove+ctact + estacknull)
                val fnm = (stack as Cons).addr
                stack = (stack as Cons).decr
                val rname = when (fnm) {
                    is Ident  -> lastName(fnm.pname)
                    is String -> lastName(fnm)
                    else      -> ""     }
                if (rname=="") throw Exception(ctfremove+ctact + errinfname)
                val rpath = Paths.get("").toAbsolutePath().toString() // applicationContext.filesDir
                val rdir = File(rpath,ctjoypath)
                if (!rdir.exists()) rdir.mkdir()
                val rfile = File(rdir, rname)
                if (rfile.exists() && rfile.isFile) {
                    rfile.delete()
                    stack = Cons(!rfile.exists(),stack)
                } else stack = Cons(false,stack)
            }
            /*
            9.toLong()  -> {  // frename
                if (stack !is Cons) throw Exception(ctfrename+ctact + estacknull)
                val newfnm = (stack as Cons).addr
                stack = (stack as Cons).decr
                if (stack !is Cons) throw Exception(ctfrename+ctact + estacknull)
                val oldfnm = (stack as Cons).addr
                stack = (stack as Cons).decr
                val oname = when (oldfnm) {
                    is Ident  -> oldfnm.pname.substringAfterLast("/")
                    is String -> oldfnm.substringAfterLast("/")
                    else      -> ""     }
                if (oname=="") throw Exception(ctfrename+ctact + errinfname)
                val nname = when (newfnm) {
                    is Ident  -> newfnm.pname.substringAfterLast("/")
                    is String -> newfnm.substringAfterLast("/")
                    else      -> ""     }
                if (nname=="") throw Exception(ctfrename+ctact + errinfname)
                val rpath = applicationContext.filesDir
                val rdir = File(rpath,ctjoypath)
                if (!rdir.exists()) rdir.mkdir()
                val nfile = File(rdir, nname)
                var success: Boolean = false
                if (!nfile.exists()) {
                    val ofile = File(rdir, oname)
                    if (ofile.exists() && ofile.isFile) {
                        //val opath = ofile.toPath()
                        //val npath = nfile.toPath()
                        //Files.move(opath, npath, StandardCopyOption.REPLACE_EXISTING)
                        //success = (nfile.exists() and !ofile.exists())
                        success = ofile.renameTo(nfile)
                    }  }
                stack = Cons(success,stack)
            }
            */
            10.toLong() -> {  // timestamp
                val t = System.currentTimeMillis().toDouble()
                stack = Cons(t,stack)
            }
            11.toLong() -> {  // date
                val d = Date().toString()
                stack = Cons(d,stack)
            }
            /*
            12.toLong() -> {  // viewurl
                if (stack !is Cons) throw Exception(ctviewurl+ctact + estacknull)
                var url = (stack as Cons).addr
                stack = (stack as Cons).decr
                if (url !is String) throw Exception(ctviewurl+ctact + estringexp)
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
            */
            /*
            13.toLong() -> {  // quit
                jquit = true
            }
            */
            /*
            14.toLong() -> {  // input
                val str = readln().toString()
                stack = Cons(str,stack)
            }
            */
            15.toLong() -> {  // run
                if (stack !is Cons) throw Exception(ctrun+ctact + estacknull)
                val s = (stack as Cons).addr
                stack = (stack as Cons).decr
                if (s !is String) throw Exception(ctrun+ctact + estringexp)
                Runtime.getRuntime().exec(s.split(" ").toTypedArray())
            }
            16.toLong() -> {  // showgraph
                if (stack !is Cons) throw Exception(ctshowgraph + ctact + estacknull)
                val i = (stack as Cons).addr
                stack = (stack as Cons).decr
                if ((i is Cons) or (i is Nil)) {
                    trail = i
                    //Draw
                    // repaint oder reDraw oder Canvas-bezug
                } else throw Exception(ctshowgraph + ctact + elistexp)
                //println(toValue(i))
            }
            else -> {  throw Exception(edoacterr+" - "+n.toString())  }
        }
        //
    }
    while (isTopIdent(stack,vm.idact)) {  doAct()  }
    vm.efun = y
    vm.eval()
}

fun selectline(txt: String,n: Int): String {
    var i: Int = n-1
    var k: Int = n
    val cr: Char = 13.toChar()
    val lf: Char = 10.toChar()
    var quit: Boolean = false
    do {  if (i==-1) quit = true
    else if (txt[i]==lf) quit = true
    else if (txt[i]==cr) quit = true
    else i = i - 1
    } while (!quit)
    quit = false
    do {  if (k>=txt.length) quit = true
    else if (txt[k]==lf) quit = true
    else if (txt[k]==cr) quit = true
    else k = k + 1
    } while (!quit)
    return txt.substring(i+1,k)
}

fun mainxyz(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}

const val edrawnopair        = "  >>>  in trail pair expected"
const val edrawxynofloat     = "  >>>  for x y float expected"
const val edrawpennobool     = "  >>>  for pen bool expected"
const val edrawcolornofloat  = "  >>>  for color float expected"
const val edrawsizenofloat   = "  >>>  for size float expected"
const val edrawcirclenofloat = "  >>>  for circle float expected"

const val edrawprotocolerr   = "  >>>  protocol error"

fun DrawScope.onDraw() {
    var i: Any = trail
    var p: Any = Nil()
    var q: Any = Nil()
    var pendown: Boolean = true
    var pencolor: Color = Color.Black
    var pensize: Float = 1.0F
    var x0: Float = 0.0F
    var y0: Float = 0.0F
    var x1: Float = 0.0F
    var y1: Float = 0.0F
    try {
        while (i is Cons) {
            p = (i as Cons).addr
            i = (i as Cons).decr
            if (i !is Cons) throw Exception(edrawnopair)
            q = (i as Cons).addr
            i = (i as Cons).decr
            when (p) {
                is Double   -> {
                    if (q !is Double) throw Exception(edrawxynofloat)
                    x1 = x0           ;  y1 = y0
                    x0 = p.toFloat()  ;  y0 = q.toFloat()
                    if (pendown) {
                        drawLine( pencolor,
                            Offset(x1,-y1),
                            Offset(x0,-y0),
                            strokeWidth=pensize,
                            cap=StrokeCap.Round )
                    }  }
                vm.idpen    -> {
                    if (q is Boolean) pendown = q
                    else throw Exception(edrawpennobool)
                }
                vm.idcolor  -> {
                    if (q is Double) {
                        val c = Color(Math.round(q))
                        pencolor = Color(red=c.blue,green=c.green,blue=c.red,alpha=1f)
                    } else throw Exception(edrawcolornofloat)
                }
                vm.idsize   -> {
                    if (q is Double) pensize = q.toFloat()
                    else throw Exception(edrawsizenofloat)
                }
                vm.idbrush  -> {}
                vm.idcircle -> {
                    if (q !is Double) throw Exception(edrawcirclenofloat)
                    val r = q.toFloat()
                    if (pendown) {
                        drawCircle(pencolor,r,Offset(x0,-y0))
                    }  }
                vm.idrect   -> {
                    if (pendown) {
                        drawRect( pencolor,
                            Offset(x1,-y1),
                            Size(x0-x1,-(y0-y1)) )
                    }  }
                else        -> throw Exception(edrawprotocolerr)
            }  }
    } catch (e: Exception) {
        runvm = false  // ???
        outtfstate.setTextAndPlaceCursorAtEnd(cterrorcol+e.message)
    }


    /*
    drawOval(Color.Green)
    drawLine(Color.Blue,Offset(20.0.toFloat(),20.0.toFloat()),
        Offset(250.0.toFloat(),250.0.toFloat()),strokeWidth=4.0.toFloat())
    drawRect(Color.Red,Offset(50.0.toFloat(),20.0.toFloat()),
        Size(100.0.toFloat(),40.0.toFloat()))
    */
}


fun mainrepl() {
    //intro()
    while (!jquit) {
        try {
            print("REPL>")
            line = readLine().toString()
            //jquit = (line.trim() == "quit")
            vm.calc(line)
            runvm = true
            while (isTopIdent(stack,vm.idact)) {  doAct()  }
            runvm = false
            //println("stack = "+toSequence(vm.creverse(stack)))
        } catch (e: Exception) {
            runvm = false
            println(cterrorcol+e.message)
        }  }
}

fun doCalc() {
    try {
        val txt: String = intfstate.text.toString()
        // auch selection!!!! beachten
        val n = intfstate.selection.start //(et1.getText())
        val lineN = selectline(txt,n)
        vm.calc(lineN)
        runvm = true
        while (isTopIdent(stack,vm.idact)) {  doAct()  }
        runvm = false
        //val outtxt = "stack = "+toSequence(vm.creverse(stack))
        //outtfstate.setTextAndPlaceCursorAtEnd(outtxt)
    }
    catch (e: Exception) {
        runvm = false
        outtfstate.setTextAndPlaceCursorAtEnd(cterrorcol+e.message)
    }
}

// --------------------------App.kt------------------------------
@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface //primaryContainer
                )
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start //.CenterHorizontally,
        ) {
            Row() {
                Button( onClick = { doCalc() }
                ) { Text("CALC") }
                Button( onClick = {
                    val outtxt: String
                    if (stack is Cons) { outtxt = toSequence(vm.creverse(stack)) }
                    else { outtxt = "(null)" }
                    outtfstate.setTextAndPlaceCursorAtEnd(outtxt)
                }
                ) { Text(".s") }
                // Button( onClick = {  }
                // ) { Text("LOAD") }
            }
            TextField(state=intfstate,     // Outlined...
                readOnly=false,
                modifier = Modifier.fillMaxWidth(),
                lineLimits = TextFieldLineLimits.MultiLine(1,7), //TextFieldLineLimits.Default,
            )
            TextField(state=outtfstate,     // Outlined...
                readOnly=false,
                modifier = Modifier.fillMaxWidth(),
                lineLimits = TextFieldLineLimits.MultiLine(1,7), //TextFieldLineLimits.Default,
            )
            Canvas(modifier = Modifier.fillMaxSize()) { onDraw() }
            /*
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) { Image(painterResource(Res.drawable.compose_multiplatform), null)
            }
            */
        }
    }
}

// --------------------------Main.kt----------------------------
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Joy with Turtle Graphics",
        resizable=true,
    ) { App() }
}
