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
//One client one thread/process nothing to share


function get_Response_Github($element)
{
//$date=date("Y-m-d\TH:i:sO"); // https://www.php.net/manual/fr/class.datetime.php
//date difference now -30 day  we convert date to int after we return with format
//echo($date."<br>");
$date=date("c");
//echo($date."<br>");
$date = strtotime($date);
$date = strtotime("-30 day", $date);
//$date=date("c",$date);
//echo($date."<br>");
$date=date("Y-m-d",$date);
$curl = curl_init();
$str=sprintf("https://api.github.com/search/repositories?q=created:>%s&sort=stars&order=desc&page=1&per_page=%d",$date,$element);
echo $str."<br>";
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
    printf("Total of indexed result %d <br>",$data->total_count);
    return $data->items;
}

}
if (!empty($_GET['element'])) {
    if(!is_numeric($_GET['element']))
{
    echo("invalid Value for element retry with different valuous");
    die;
}
    //Size limit =100 ===> element between 1-100  [<0-->30 elements]
  ob_start("ob_gzhandler");
  echo("processing <br>");
    $data=get_Response_Github($_GET['element']);
    echo count($data);
    echo " Elements <br>";
    //print_r($data);
    $languages=[];
 //   $languages->name="langage 1";
   // $languages->nbcount=0;
   // $languages->items[];
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
print_r($lang);

echo("<br>  <br>");
foreach ($languages as $lp) {
  # code...
  echo ("<br> languages ".$lp['name']."  Nombre count ".$lp['nbcount']." Tendance ".$lp['ordre_tendance']." Nb count ".count($lp['items']));
}



	ob_end_flush();
}
else
{
  

echo ("Functionnement normale  <br>");
}

?>