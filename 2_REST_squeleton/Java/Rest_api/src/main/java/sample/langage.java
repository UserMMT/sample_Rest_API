/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hp
 */
public class langage {
    public   int nb_count; //Nombre d'element
  public  String ordre_tendance; //Ordre d'apparaition des elements en fonction de leurs stars
  public String name; //Nom du langage de programmation
 public   List<JSONObject> content; //Repo du langages

   


   public langage(String name,String ordre,JSONObject val) {
        this.name=name;
        this.nb_count=1;
        this.ordre_tendance=ordre;
        this.content= new ArrayList<JSONObject>();
        this.content.add(val);
    }
    public langage(String name) {
        this.name=name;
        this.nb_count=0;
        this.ordre_tendance="";
    }

    public langage(int nb_count, String ordre_tendance, String name, List<JSONObject> content) {
        this.nb_count = nb_count;
        this.ordre_tendance = ordre_tendance;
        this.name = name;
        this.content = content;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
