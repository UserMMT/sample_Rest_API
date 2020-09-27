<?php

/************************************
*			INITIALIZATION			*
*************************************/
//START SESSION
//session_start();

//EXTERNAL FILES
//require 'C:\xampp\htdocs\api\gemo\vendor\autoload.php';

/****************************************
*			GLOBAL VARIABLES			*
*****************************************/
//$date1 = time();

//$request = $_SERVER['REQUEST_URI'];  //this would be /api/gemo/language/
$uri=explode("/",$_SERVER['REQUEST_URI']);
//print_r($uri);
$cmpt=0;
$lv="";
$valid=false;
foreach ($uri as $r) {
  # code...
  if(strcmp($r,"language")==0){
   // echo(sizeof($uri)."<br>");
 //   echo ($cmpt);
    $valid=true;
    if(sizeof($uri)>$cmpt+1)
    {
      $lv=$uri[$cmpt+1];
  //    echo $lv;
    }
   // else{
      //return all languages
   //   echo("all languages \n");
   // }
  }
  $cmpt++;
}
//die;
function get_Response_Github()
{
//$date=date("Y-m-d\TH:i:sO"); // https://www.php.net/manual/fr/class.datetime.php
//date difference now -30 day  we convert date to int after we return with format
//echo($date."<br>");
$date=date("c"); //iso 8601 format
//echo($date."<br>");
$date = strtotime($date);
$date = strtotime("-30 day", $date);
//$date=date("c",$date);
//echo($date."<br>");
$date=date("Y-m-d",$date);
$curl = curl_init();
$str=sprintf("https://api.github.com/search/repositories?q=created:>%s&sort=stars&order=desc&page=1&per_page=100",$date);
#echo $str."<br>";
//die;
curl_setopt_array($curl, array(
  CURLOPT_URL => $str,
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_USERAGENT=>  "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0",
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "GET",
  CURLOPT_HTTPHEADER => array(
    "accept: application/json",
    "content-type: application/json"
  ),
));

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
  echo "cURL Error #:" . $err;
  die;
} else {
    //echo ($response);
    //print_r(json_decode($response));
    //total_count is usefull
    $data=json_decode($response);
    #printf("Total of indexed result %d <br>",$data->total_count);
    return $data->items;
}

}
if($valid)
{
if (!empty($lv)) {
    //Size limit =100 ===> element between 1-100  [<0-->30 elements]
    if(strcmp($lv,"null")==0)
    {
      $lv=NULL;
    }
  ob_start("ob_gzhandler");
  #echo("processing <br>");
    $data=get_Response_Github();
   # echo count($data);
   # echo " Elements <br>";
    //print_r($data);
    $compteur=1;
    $language=array(	'name' 			=> $lv,
                    'nbcount' 	=> 0);
    foreach ($data as $datap)
{
    if(strcmp($datap->language, $lv) == 0)
    {
        if($language['nbcount']==0){
            $language['nbcount']++;
     $language['ordre_tendance']=(string)$compteur;
     $language['items'][]=$datap;
        }
        else
        {
     $language['nbcount']++;
     $language['ordre_tendance'].=",".$compteur;
     $language['items'][]=$datap;
    }
  

  }
  $compteur++;
}



#echo("<br>  <br>");
  # code...
 # echo ("<br> language ".$language['name']."  Nombre count ".$language['nbcount']." Tendance ".$language['ordre_tendance']." Nb count ".count($language['items']));
  //Output final result
  // required headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
  echo (json_encode($language));
  ob_end_flush();
  //$date2 = time();
 // $sec = ($date2 - $date1);
 // echo $sec;
}


else  // root/language/
{
    ob_start("ob_gzhandler");
    //echo("processing <br>");
      $data=get_Response_Github();
      #echo count($data);
      #echo " Elements <br>";
      //print_r($data);
      $languages=[];
      $lang=[];
      $compteur=1;
      foreach ($data as $datap)
  {
    if(empty($languages))//first item
   { $languages[]=array(	'name' 			=> $datap->language,
    'nbcount' 	=> 1,
    'ordre_tendance' =>'1',
    'items' 	=> array($datap));
    $lang[]=$datap->language;
    $compteur++;
  continue;
  }
  if (in_array($datap->language,$lang)) {
    foreach($languages as &$l)
    {
      if(strcmp($datap->language, $l['name']) == 0)
      {
       $l['nbcount']++;
       $l['ordre_tendance'].=",".$compteur;
       $l['items'][]=$datap;
      break;
      }
    }
    $compteur++;
    continue;
  }
  else{
    $languages[]=array(	'name' 			=> $datap->language,
                      'nbcount' 	=> 1,
                      'ordre_tendance' =>(string)$compteur,
                      'items' 	=> array($datap));
    $lang[]=$datap->language;
    $compteur++;
  }
  
    }
//  print_r($lang);
  
 // echo("<br>  <br>");
 /* foreach ($languages as $lp) {
    # code...
    echo ("<br> languages ".$lp['name']."  Nombre count ".$lp['nbcount']." Tendance ".$lp['ordre_tendance']." Nb count ".count($lp['items']));
  }
  */
 // required headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
  echo (json_encode($languages));
  ob_end_flush();
 // $date2 = time();
//$sec = ($date2 - $date1);
//echo $sec;
//echo ("Functionnement normale  <br>");
}
}
else{
//  http://localhost:5000/language
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
echo (json_encode(array(	'error' 			=>'ressource_not_available' )));
}
?>