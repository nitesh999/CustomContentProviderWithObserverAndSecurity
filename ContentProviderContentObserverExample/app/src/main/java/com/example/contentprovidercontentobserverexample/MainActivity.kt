package com.example.contentprovidercontentobserverexample

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

private const val URL_LOADER = 0
const val NOTE_ACTION_VIEW = "com.example.contentprovidercontentobserverexample.custom.intent.action.NOTE_VIEW"

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            LoaderManager.getInstance(this).initLoader(URL_LOADER, null, this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        return true
    }

    fun onClickAddDetails(view: View) {
        val values = ContentValues()
        values.put(UsersProvider.name, (findViewById<View>(R.id.txtName) as EditText).text.toString())
        contentResolver.insert(UsersProvider.CONTENT_URI, values)
        Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
    }

    fun onClickTempUriGrant(view: View) {//this feature enables provider app to share data only with specific app's pacakge mentioned in grantUriPermission
        // to check this capability please make exported=false and grantUriPermissions=true
        grantUriPermission("com.example.contentreceivercontentobserverexample", UsersProvider.CONTENT_URI, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        grantUriPermission("com.example.contentreceivercontentobserverexample", UsersProvider.CONTENT_URI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    fun onClickTempUriGrantUsingIntent(view: View) {//this will work only 1 time for receiver app
        // to check this capability please make exported=false and grantUriPermissions=true
        val intent = packageManager.getLaunchIntentForPackage("com.example.contentreceivercontentobserverexample")
        intent?.action = NOTE_ACTION_VIEW // SET CUSTOM INTENT ACTION
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent?.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // GRANT TEMPORARY READ PERMISSION
        intent?.data = UsersProvider.CONTENT_URI
        startActivity(intent)
    }

    fun onClickShowDetails(view: View) {
        // Retrieve employee records
        val cursor = contentResolver.query(Uri.parse(UsersProvider.URL), null, null, null, null)
        if (cursor != null) {
            showUserList(cursor)
        }
    }

    private fun showUserList(cursor:Cursor) {
        val resultView = findViewById<View>(R.id.res) as TextView
        if (cursor.moveToFirst()) {
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
        cursorLoader.uri = UsersProvider.CONTENT_URI
        cursorLoader.projection = null
        return cursorLoader
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        showUserList(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }
}