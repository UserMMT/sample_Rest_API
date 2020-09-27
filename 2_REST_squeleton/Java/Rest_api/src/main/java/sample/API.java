/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * REST Web Service
 *
 * @author hp
 */
@Path("API")
public class API {
          //Processing to get the data from github
                public static JSONObject download_resp() throws IOException, ParseException{
                          URL url = new URL("https://api.github.com/search/repositories?q=created:<"+set_date()+"&sort=stars&order=desc&page=1&per_page=100");

BufferedReader reader;
    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
   /* for (String line; (line = reader.readLine()) != null;) {
        System.out.println(line);
    }
    */
   Object obj = new JSONParser().parse(reader);
   
    // typecasting obj to JSONObject 
       JSONObject jo = (JSONObject) obj; 
        long total_count;
           total_count = (long) jo.get("total_count");
           
           System.out.println("Voici le total count "+total_count);
           return jo;
                }
      
                
//Processing to parse data and return languages array in json_format
      public static JSONArray processingall(JSONObject jo){
              // getting items 
JSONArray tab; //array to get all langages
List<langage> languages; 
List<String> lang;

        JSONArray ja = (JSONArray) jo.get("items"); 
          
        // iterating items 
        Iterator itr2 = ja.iterator(); 
        Iterator itr1;  
                    
       /* long count;
            Iterable<Object> newIterable = () -> itr2;
    count = StreamSupport.stream(newIterable.spliterator(), false).count();
                    System.out.println("Valeur de l'iterateur "+count);
          */
       int compteur=1;
                    languages=new ArrayList<langage>();
      lang=new ArrayList<String>();
      langage temp;
                    JSONObject tmp; //tmp item selected
                    int v;
                    String L_tmp; //tmp langage name if in array
       langage L;//tmp langage if in array  
              

        while (itr2.hasNext())  
        { 
               tmp=(JSONObject) itr2.next();
             //  System.out.println("item langage "+(String) tmp.get("language")+" compteur "+compteur);
               if((tmp.get("language"))==null){
L_tmp= "null";
                  // System.out.println("I'm null "+L_tmp);
               }
               else{
    L_tmp= (String) tmp.get("language");                   
               }

if(compteur==1){
    //first item
    
    temp=(langage) new langage(L_tmp,"1",tmp);
    languages.add(temp);
    lang.add(L_tmp);
    compteur++;
//    langage abc=languages.get(0);
  //  System.out.println("Voici langage "+abc.name +" nb_count "+abc.nb_count +"Tendance "+abc.ordre_tendance+" content "+abc.content.get(0).toJSONString());
  continue;
}

if (lang.contains(L_tmp)) {
   v=languages.size();
   
    for(int k=0;k<v;k++)
    {
            L=languages.get(k);
          //  System.out.println("Langage name "+L.name);
      if(L_tmp.equals(L.name))
      {
//       L=languages.get(k);
       L.nb_count++;

       L.ordre_tendance=L.ordre_tendance.concat(","+compteur);
      
       L.content.add(tmp);
       languages.set(k, L);
          //System.out.println("Chaine tendance "+L.ordre_tendance);
      break;
      }
    }
    compteur++;
    continue;
  }
  else{
    temp=new langage(L_tmp,Integer.toString(compteur),tmp);
    languages.add(temp);
    lang.add(L_tmp);
    compteur++;
  }
  
        } 
        
        
     /*
        int variable=lang.size();
    for(int k=0;k<variable;k++)
    {
        temp=languages.get(k);
        System.out.println("Language "+temp.name+" Count "+temp.nb_count +" Ordre "+temp.ordre_tendance+" Content 1 name "+temp.content.get(0).get("name"));
    }
        */
     ///
// putting data to JSONObject
       v=languages.size();
       //Free output
       JSONArray JA;
       tab=new JSONArray();
       Map m;
       JSONObject res=new JSONObject();
    for(int k=0;k<v;k++)
    {
        temp=languages.get(k);
   res.put("name", temp.getName());
   res.put("nbcount",temp.nb_count);
   res.put("ordre_tendance",temp.ordre_tendance);
   JA=new JSONArray();
           m = new LinkedHashMap(temp.nb_count); 
           for (int w=0;w<temp.nb_count;w++)
           {
               m.putAll(temp.content.get(w));
                JA.add(m); 
                
            }
           
           res.put("items", JA);
tab.add(res);
       res=new JSONObject();

    }
return tab;
      }
      
      
      

//Function to get the a specific langage items
      //Processing to parse data and return languages array in json_format
      public static JSONObject processinglanguage(JSONObject jo,String Lv ){
              // getting items 
JSONObject res; //array to get output

        JSONArray ja = (JSONArray) jo.get("items"); 
          
        // iterating items 
        Iterator itr2 = ja.iterator(); 
        Iterator itr1;  
                    
       /* long count;
            Iterable<Object> newIterable = () -> itr2;
    count = StreamSupport.stream(newIterable.spliterator(), false).count();
                    System.out.println("Valeur de l'iterateur "+count);
          */
       int compteur=1;
                   langage language=new langage(Lv);
     
                    JSONObject tmp; //tmp item selected
                    int v;
                    String L_tmp; //tmp langage name if in array
              

        while (itr2.hasNext())  
        { 
               tmp=(JSONObject) itr2.next();
             //  System.out.println("item langage "+(String) tmp.get("language")+" compteur "+compteur);
               if((tmp.get("language"))==null){
L_tmp= "null";
                  // System.out.println("I'm null "+L_tmp);
               }
               else{
    L_tmp= (String) tmp.get("language");                   
               }


if (Lv.equals(L_tmp)) {   
        if(language.nb_count==0){
            language.nb_count++;
     language.ordre_tendance=Integer.toString(compteur);
     language.content.add(tmp);
        }
        else
        {
                 language.nb_count++;
     language.ordre_tendance=language.ordre_tendance.concat(","+compteur);
     language.content.add(tmp);
      }
    }
    compteur++;
  }
        
        
     
       
//        System.out.println("Language "+language.name+" Count "+language.nb_count +" Ordre "+language.ordre_tendance+" Content 1 name "+language.content.get(0).get("name"));

  
// putting data to JSONObject
  res=new JSONObject();
      res.put("name", language.getName());
   res.put("nbcount",language.nb_count);
   res.put("ordre_tendance",language.ordre_tendance);
   JSONArray JA=new JSONArray();
          Map m = new LinkedHashMap(language.nb_count); 
           for (int w=0;w<language.nb_count;w++)
           {
               m.putAll(language.content.get(w));
                JA.add(m); 
                
            }
           
           res.put("items", JA);
return res;
      }
      
            
     
      

//Processing to find date-30 in string
         public static String set_date(){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  //Récupérer la date actuelle
  Calendar c = Calendar.getInstance();
  //Afficher la date actuelle 
  System.out.println("La date actuelle: "+sdf.format(c.getTime()));
     
  //Nombre de jours à ajouter
  c.add(Calendar.DAY_OF_MONTH,-30 ); 
  //Date après avoir ajouté les jours à la date actuelle
  String d2 = sdf.format(c.getTime());  
  System.out.println("Date après l'addition: "+d2);
  return d2;
                }
                
                
         
    public API() {
    }

    /**
     * Retrieves representation of an instance of sample.API
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)           //return all languages 
      public static String language(){
             try {
                Instant start = Instant.now();
//your code
JSONArray ja; //Object of response from github
ja=processingall(download_resp());
Instant end = Instant.now();

        Duration timeElapsed = Duration.between(start, end);
//System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
System.out.println("Time taken: "+ timeElapsed.toSeconds() +" seconds");
return (ja.toJSONString());
    } catch (IOException | ParseException ex) {
                 System.out.println("Erreur client");
    }        
             return ("{'error':'Not found'}");
}
 
      @GET
      @Path("language")
      @Produces(MediaType.APPLICATION_JSON)   //return specific language 
 public static String language(@QueryParam("val") String Val) {
                    
 try {
Instant start = Instant.now();
//your code
JSONObject jo; //Object of response from github
jo=processinglanguage(download_resp(),Val);
Instant end = Instant.now();

        Duration timeElapsed = Duration.between(start, end);
//System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
System.out.println("Time taken: "+ timeElapsed.toSeconds() +" seconds");
return jo.toJSONString();
 } catch (IOException | ParseException ex) {
                 System.out.println("Erreur client");                 
    }
     JSONObject jo_error=new JSONObject();
     jo_error.put("error","Ressource not available");
     return jo_error.toJSONString();
 
                
             
}

}
