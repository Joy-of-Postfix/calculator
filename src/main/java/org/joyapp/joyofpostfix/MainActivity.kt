package org.joyapp.joyofpostfix

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

// ----- constants
const val cterrorcol = "ERROR:   "
const val ctnullpar  = "(null)"
const val ctjoypath  = "joy"

// ----- for monad side-effects
const val ctdot      = "."
const val ctprint    = "print"
const val ctload     = "load"
const val ctsave     = "save"
const val ctloadtext = "loadtext"
const val ctsavetext = "savetext"
const val ctfremove  = "fremove"
const val ctfrename  = "frename"
const val ctviewurl  = "viewurl"

// ----- error messages
const val errinfname =      "  >>>  error in filename"
const val errnofile  =      "  >>>  file not found"
const val edoacterr  = "doAct  >>>  can't react to this number"

var vm = JoyVM()
var itxt = vm.prelude()
var otxt = vm.deflines(vm.splitTo(itxt,"\n"))


class MainActivity : AppCompatActivity() {

    lateinit var et1: EditText
    lateinit var et2: EditText

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.joy_menu, menu)
        return true
    }

    /*
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            //val selectedFile = data?.data // The URI with the location of the file
            var txt: String = ""
            var ln: String = ""
            data?.data?.let {
                contentResolver.openInputStream(it)
            }?.let {
                val r = BufferedReader(InputStreamReader(it))
                while (true) {
                    val line: String? = r.readLine() ?: break
                    txt = txt + ln + line
                    ln = "\n"
                }
            }
            val etxt = vm.toValue(vm.deflines(vm.splitTo(txt,"\n")))

            //val selectedFile = data.getData() // The URI with the location of the file

            //val inputStream = getContentResolver().openInputStream(selectedFile)
            //val allText = inputStream.bufferedReader().use(BufferedReader::readText)
/*
            val fname = selectedFile?.getPath()?.substringAfterLast(":/")

            val rfile = File(fname) // ?.substringBeforeLast("/"),fname?.substringAfterLast("/"))
            if (rfile.exists()) {
                val rtxt = rfile.readText()
                //val etxt = vm.toValue(vm.deflines(vm.splitTo(rtxt,"\n")))
                et1.setText(rtxt)
                //et2.setText(txt)
            } else {
                et1.setText("dont exs")
            }
*/
            et1.setText(txt)
            et2.setText(etxt)
            //doSomeOperations()
        }
    }
    */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            var txt: String = ""
            var ln: String = ""
            //val selectedFile = data?.data // The URI with the location of the file
            data?.data?.let {
                contentResolver.openInputStream(it)
            }?.let {
                val r = BufferedReader(InputStreamReader(it))
                while (true) {
                    val line: String? = r.readLine() ?: break
                    txt = txt + ln + line
                    ln = "\n"
                }  }
            val etxt = vm.deflines(vm.splitTo(txt,"\n"))
            et1.setText(txt)
            et2.setText(etxt)
        }  }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //val path = applicationContext.filesDir
        //val dir = File(path,ctjoypath)

        when (item.itemId) {
            R.id.newstack -> {
                stack = Nil()
                return true  }
            R.id.result  -> {
                //Selection.
                val textToInsert : String = et2.text.toString()
                val start = Math.max(et1.getSelectionStart(), 0)
                val end = Math.max(et1.getSelectionEnd(), 0)
                et1.getText().replace(
                    Math.min(start, end), Math.max(start, end),
                    textToInsert, 0, textToInsert.length  )
                return true  }
            R.id.clear -> {
                vm = JoyVM()
                itxt = vm.prelude()
                otxt = vm.deflines(vm.splitTo(itxt,"\n"))
                et1.setText("")
                return true  }
            R.id.load  -> {
                /*
                val file = File(dir, "Test.txt")
                val contents = file.readText()
                et1.setText(contents)
                //doit
                */
                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
                return true  }
            R.id.copy  -> {
                //et1.selectAll()
                val textToCopy = et1.text
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", textToCopy)
                clipboardManager.setPrimaryClip(clipData)
                /*
                if (!dir.exists()) dir.mkdir()
                val file = File(dir,"Test.txt")
                file.writeText("record goes here")
                //doit
                */
                return true  }
            else -> return super.onOptionsItemSelected(item)
        }  }

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
                    runOnUiThread {  et2.setText(toValue(i))  }
                }
                2.toLong()  -> {  //  print
                    if (stack !is Cons) throw Exception(ctprint+ctact + estacknull)
                    val i = (stack as Cons).addr
                    stack = (stack as Cons).decr
                    runOnUiThread {
                        when (i) {
                            is Cons   -> et2.setText(toSequence(i))
                            is String -> et2.setText(i)
                            is Nil    -> et2.setText(ctnullpar)    // ???
                            else      -> et2.setText(toValue(i))
                        }
                    }  }
                3.toLong()  -> {  // load
                    if (stack !is Cons) throw Exception(ctload+ctact + estacknull)
                    val fnm = (stack as Cons).addr
                    stack = (stack as Cons).decr
                    val rname = when (fnm) {
                        is Ident  -> fnm.pname.substringAfterLast("/")
                        is String -> fnm.substringAfterLast("/")
                        else      -> ""     }
                    if (rname=="") throw Exception(ctload+ctact + errinfname)
                    val rpath = applicationContext.filesDir
                    val rdir = File(rpath,ctjoypath)
                    if (!rdir.exists()) rdir.mkdir()
                    val rfile = File(rdir, rname)
                    if (!rfile.exists()) throw Exception(ctload+ctact + errnofile)
                    val rtxt = rfile.readText()

                    //vm = JoyVM()  // =>vergisst Daten???
                    //itxt = vm.prelude()
                    //otxt = vm.deflines(vm.splitTo(itxt,"\n"))
                    //et1.setText(rtxt)

                    val txt = vm.deflines(vm.splitTo(rtxt,"\n"))
                    runOnUiThread {  et1.setText(rtxt)  }
                }
                4.toLong()  -> {  // save
                    if (stack !is Cons) throw Exception(ctsave+ctact + estacknull)
                    val fnm = (stack as Cons).addr
                    stack = (stack as Cons).decr
                    val wname = when (fnm) {
                        is Ident  -> fnm.pname.substringAfterLast("/")
                        is String -> fnm.substringAfterLast("/")
                        else      -> ""     }
                    if (wname=="") throw Exception(ctsave+ctact + errinfname)
                    val wpath = applicationContext.filesDir
                    val wdir = File(wpath,ctjoypath)
                    if (!wdir.exists()) wdir.mkdir()
                    val wfile = File(wdir,wname)
                    val wtxt = et1.text.toString()
                    wfile.writeText(wtxt)
                }
                5.toLong()  -> {  // loadtext
                    if (stack !is Cons) throw Exception(ctloadtext+ctact + estacknull)
                    val fnm = (stack as Cons).addr
                    stack = (stack as Cons).decr
                    val rname = when (fnm) {
                        is Ident  -> fnm.pname.substringAfterLast("/")
                        is String -> fnm.substringAfterLast("/")
                        else      -> ""     }
                    if (rname=="") throw Exception(ctloadtext+ctact + errinfname)
                    val rpath = applicationContext.filesDir
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
                        is Ident  -> fnm.pname.substringAfterLast("/")
                        is String -> fnm.substringAfterLast("/")
                        else      -> ""      }
                    if (wname=="") throw Exception(ctsavetext+ctact + errinfname)
                    val wpath = applicationContext.filesDir
                    val wdir = File(wpath,ctjoypath)
                    if (!wdir.exists()) wdir.mkdir()
                    val wfile = File(wdir,wname)
                    wfile.writeText(str)
                }
                7.toLong()  -> {  // files
                    val fpath = applicationContext.filesDir
                    val fdir = File(fpath,ctjoypath)
                    if (!fdir.exists()) fdir.mkdir()
                    val flist = fdir.listFiles()
                    var f: Any = Nil()
                    var s: String
                    flist?.forEach{
                        s = it.toString().substringAfterLast("/")
                        f = Cons(s,f)  }
                    stack = Cons(vm.nreverse(f),stack)
                }
                8.toLong()  -> {  // fremove
                    if (stack !is Cons) throw Exception(ctfremove+ctact + estacknull)
                    val fnm = (stack as Cons).addr
                    stack = (stack as Cons).decr
                    val rname = when (fnm) {
                        is Ident  -> fnm.pname.substringAfterLast("/")
                        is String -> fnm.substringAfterLast("/")
                        else      -> ""     }
                    if (rname=="") throw Exception(ctfremove+ctact + errinfname)
                    val rpath = applicationContext.filesDir
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exe: Button = findViewById<View>(R.id.button) as Button
        val dot: Button = findViewById<View>(R.id.button2) as Button
        val brk: Button = findViewById<View>(R.id.button3) as Button
        val stk: Button = findViewById<View>(R.id.button4) as Button
        //val rlt: Button = findViewById<View>(R.id.button5) as Button
        val lft: Button = findViewById<View>(R.id.button6) as Button
        val rgt: Button = findViewById<View>(R.id.button7) as Button
        et1 = findViewById<View>(R.id.editTextInText) as EditText
        et2 = findViewById<View>(R.id.editTextOutText) as EditText

        //et2.setText(otxt)  // ???

        exe.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val txt: String = et1.text.toString()
                val n = Selection.getSelectionStart(et1.getText()) // ???
                val lineN = selectline(txt,n)
                if (runvm) runvm = false
                else {  // hier ist der Thread
                    val thread: Thread = object : Thread(null, null, "joyapp", 50000000) {
                        override fun run() {
                            try {
                                vm.calc(lineN)
                                runOnUiThread {  et2.setText("")  }
                                runvm = true
                                while (isTopIdent(stack,vm.idact)) {  doAct()  }
                                //while (res is Act) { res = doAct(res) }
                                runvm = false
                            } catch(e: Exception) {
                                runvm = false
                                val err: String = cterrorcol + e.message
                                runOnUiThread {  et2.setText(err)  }
                            }
                        }  }
                    thread.start()  }
            } })

        dot.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // .
                if (stack is Cons) {
                    val x = (stack as Cons).addr
                    stack = (stack as Cons).decr
                    et2.setText(toValue(x))
                } else et2.setText(ctnullpar)
            } })

        brk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Selection
                val textToInsert : String = "[]"
                val start = Math.max(et1.getSelectionStart(), 0)
                val end = Math.max(et1.getSelectionEnd(), 0)
                et1.getText().replace(
                    Math.min(start, end), Math.max(start, end),
                    textToInsert, 0, textToInsert.length  )
                val p = et1.getSelectionStart()
                et1.setSelection(p-1)
            } })

        stk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // .s
                if (stack is Cons)
                    et2.setText(toSequence(vm.creverse(stack)))
                else et2.setText(ctnullpar)
            } })

        lft.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Selection
                val p = et1.getSelectionStart()
                if (p>0) et1.setSelection(p-1)
            } })

        rgt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Selection
                val p = et1.getSelectionStart()
                if (p<et1.text.length) et1.setSelection(p+1)
            } })

    }
}