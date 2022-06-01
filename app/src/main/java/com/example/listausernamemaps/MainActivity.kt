package com.example.listausernamemaps

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lista = findViewById<ListView>(R.id.listaMenu)
        val geo : JSONArray = JSONArray()
        val user : ArrayList<String> = ArrayList()
        val dados : ArrayList<String> = ArrayList()
        var adapter: ArrayAdapter<String>? = null
        val url = "https://jsonplaceholder.typicode.com/users/"

        val jsonRequest = JsonArrayRequest( //retorna uma lista
            Request.Method.GET,url,null,
            { response ->

                val tamRetorno = response.length()

                for (i in 0 until tamRetorno){
                    // var obj: JSONObject = response.get(i) as JSONObject

                    val obj = response.getJSONObject(i)
                    val name = obj.getString("name")
                    val email = obj.getString("email")
                    val website = obj.getString("website")
                    val street = obj.getJSONObject("address").getString("street").toString()
                    val city = obj.getJSONObject("address").getString("city").toString()

                    dados.add("\n"+"Username: "+obj["username"].toString()+"\n")
                    geo.put(obj.getJSONObject("address").getJSONObject("geo"))
                    user.add((name + "\n"
                            + email + "\n"
                            + website + "\n"
                            + street + "\n"
                            + city))

                    //var comp:JSONObject = username.getJSONObject("company") as JSONObject
                    // dados.add("Name: "+comp["name"].toString())

                    //print(lon + " -- "+ lat)

                }

                adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    dados
                )

                lista.adapter = adapter
                lista.onItemClickListener = AdapterView.OnItemClickListener{
                        _, _, position, id ->
                    val i = Intent(this, MapsActivity::class.java).apply{
                        putExtra("txtId",(id).toString())
                        putExtra("lat",geo.getJSONObject(position)["lat"].toString())
                        putExtra("lng", geo.getJSONObject(position)["lng"].toString())
                        putExtra("user", user[position])
                    }
                    startActivity(i)
                }
            },
            { error ->
                print(error.toString())
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }
}