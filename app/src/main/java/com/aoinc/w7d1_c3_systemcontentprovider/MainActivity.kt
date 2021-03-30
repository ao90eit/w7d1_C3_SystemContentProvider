package com.aoinc.w7d1_c3_systemcontentprovider

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.TextView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: add permissions check - currently just cheating on emulator

    private lateinit var contactsTextView: TextView

    // fields returned in data query
    val contacts_projection: Array<String> = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    // filter on the query results
    private var search = "%%"
    private val columns_reading: Array<String> = arrayOf(search)

    private val selection: String = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactsTextView = findViewById(R.id.contacts_textView)

        val loaderManager: LoaderManager = LoaderManager.getInstance(this)
        loaderManager.initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            contacts_projection,
            selection,
            columns_reading,
            null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val sBuilder = StringBuilder()

        data?.moveToPosition(-1)
        while (data?.moveToNext() == true) {
//            Log.d("TAG_X", data.getString(1))

            for (i in 0 until data.columnCount) {
                sBuilder.append("${data.getString(i)}, ")
            }
            sBuilder.append("\n")
        }

        contactsTextView.text = sBuilder.toString()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // Clear the recycler view of list view if displaying there
    }
}