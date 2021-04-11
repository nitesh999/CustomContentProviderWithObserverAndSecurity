package com.example.contentreceivercontentobserverexample

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

private const val URL_LOADER = 0
private const val PROVIDER_NAME = "com.example.contentprovidercontentobserverexample"
private const val URL = "content://$PROVIDER_NAME/users"
val CONTENT_URI: Uri = Uri.parse(URL)
const val name = "name"

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private var mCur: Cursor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //showUserList()
        if (savedInstanceState == null) {
            LoaderManager.getInstance(this).initLoader(URL_LOADER, null, this)
        }
    }

    fun onClickAddDetails(view: View) {
        val values = ContentValues()
        values.put(name, (findViewById<View>(R.id.txtName) as EditText).text.toString())
        contentResolver.insert(CONTENT_URI, values)
        Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
    }

    private fun showUserList() {
        val resultView = findViewById<View>(R.id.res) as TextView
        val cursor = contentResolver.query(Uri.parse(URL), null, null, null, null)
        if (cursor!!.moveToFirst()) {
            val strBuild = StringBuilder()
            while (!cursor.isAfterLast) {
                strBuild.append("""${cursor.getString(cursor.getColumnIndex("id"))}-${cursor.getString(cursor.getColumnIndex("name"))}""".trimIndent())
                strBuild.append("\n")
                cursor.moveToNext()
            }
            resultView.text = strBuild
        } else {
            resultView.text = "No Records Found"
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val cursorLoader = CursorLoader(this)
        cursorLoader.uri = CONTENT_URI
        cursorLoader.projection = null
        return cursorLoader
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (mCur != data) {
            mCur = data
            showUserList()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }
}