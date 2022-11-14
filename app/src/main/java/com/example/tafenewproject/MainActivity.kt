package com.example.tafenewproject

import android.app.admin.SystemUpdatePolicy
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast



class MainActivity : AppCompatActivity() {
    //create DBHandler object
    val dbh: DBHandler = DBHandler(this, null)
    //create arrays for contact object and ids
    var personList: MutableList<Person> = arrayListOf()
    var idList: MutableList<Int> = arrayListOf()
    private val MENUADD = Menu.FIRST + 1
    private val MENUEDIT = Menu.FIRST + 2
    private val MENUREMOVE = Menu.FIRST + 3
    private var lstView:ListView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lstView = findViewById(R.id.lstView)
        registerForContextMenu(lstView)
        initAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, MENUADD, Menu.NONE, "ADD")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val goAdd = Intent(this,ADDEDIT::class.java)
        startActivity(goAdd)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(Menu.NONE,MENUEDIT,Menu.NONE,"EDIT")
        menu.add(Menu.NONE,MENUREMOVE,Menu.NONE,"DELETE")
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // adapter context menu into object
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        // code for edit and delete
        when (item.itemId) {
            MENUEDIT -> {
                // create intent and start activity with data pass on
                val addEdit = Intent(this, ADDEDIT::class.java)
                addEdit.putExtra("ID", idList[info.position])
                startActivity(addEdit)
            }
            MENUREMOVE-> {
                // call delete function dbhandler
                dbh.deletePerson(idList[info.position])
                // refresh list view after delete
                initAdapter()
            }

        }
        return super.onContextItemSelected(item)
    }
    private fun initAdapter() {
        try {
            //clear all values
            personList.clear()
            idList.clear()
            //get all contacts from DBHandler and go through loop
            for (person: Person in dbh.getAllPersons()) {
                //read and add to contactsList
                personList.add(person)
                idList.add(person.id)
            }
                //create array adapter
                val adp = CustomAdapter(this, personList as ArrayList<Person>)
                // assign adapter to list view
                lstView?.adapter = adp

        } catch (e: Exception) {
            //show error message toast
            Toast.makeText(this, "Problem ${e.message.toString()}",
                Toast.LENGTH_LONG).show()
        }
    }
}











