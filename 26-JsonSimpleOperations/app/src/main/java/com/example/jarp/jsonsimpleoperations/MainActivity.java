package com.example.jarp.jsonsimpleoperations;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    String jsonString= "{\"menu\":{\"id\":\"file\",\"value\":\"File\",\"popup\":{\"menuitem\":[{\"value\":\"New\",\"onclick\":\"CreateNewDoc()\"},{\"value\":\"Open\",\"onclick\":\"OpenDoc()\"},{\"value\":\"Close\",\"onclick\":\"CloseDoc()\"}]}}}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Menu menu = null;
        try {
             menu = readMenuFromString(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().create();

        String jsonMenu = gson.toJson(menu);

        Menu createdFromGson = gson.fromJson(jsonString,Menu.class);




    }



    public Menu readMenuFromString(String jsonStringMenu) throws JSONException {


        //This should over an InputStream using a JsonReader

        JSONObject root = new JSONObject(jsonStringMenu);

        JSONObject jsonMenu = root.getJSONObject("menu");

        Menu menu = createFromRoot(jsonMenu);

        return  menu;



    }

    private Menu createFromRoot(JSONObject root) throws JSONException {

        Menu result = new Menu();

        result.id = root.getString("id");
        result.value = root.getString("value");

        Pop pop = createPop(root);

        result.pop = pop;

        return result;
    }

    private Pop createPop(JSONObject root) throws JSONException {
        JSONObject jsonPop = root.getJSONObject("popup");

        Pop pop = new Pop();

        MenuItem[] menuItems = createMenuItems(jsonPop);

        pop.menuItems = menuItems;

        return  pop;


    }

    private MenuItem[] createMenuItems(JSONObject root) throws JSONException {
        JSONArray jsonMenuArray = root.getJSONArray("menuitem");

        MenuItem[] menuItems = new MenuItem[jsonMenuArray.length()];

        for ( int idx = 0 ; idx< jsonMenuArray.length() ;idx++  ){

            JSONObject jsonSingleMenu = jsonMenuArray.getJSONObject(idx);

            MenuItem newMenu = new MenuItem();
            newMenu.value = jsonSingleMenu.getString("value");
            newMenu.onClick = jsonSingleMenu.getString("onclick");

            menuItems[idx]= newMenu;

        }
        return  menuItems;

    }

    public class Menu{

        public String id;
        public String value;
        public Pop pop;

    }

    public class Pop{

        public MenuItem[] menuItems;

    }

    public class MenuItem{
         public String value;
        public String onClick;
    }
}
