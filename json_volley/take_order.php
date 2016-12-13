<?php
$db=mysqli_connect("localhost","root","","databaseandroid") or die("Can not connect right now!");

$request = file_get_contents('php://input');

$output = json_decode($request);
$datausername = $output -> username;
$datapassword = $output -> password;
$response =1;
$sql = "INSERT INTO `savedata` VALUES ('$datausername','$datapassword')";
$result = mysqli_query($db,$sql);

if($result>0){
           $response = 1;
       $errorstr = mysqli_errno($db);
         }    
     else{
           $response = 0;
       $errorstr = mysqli_errno($db);
         }
    
    $arrayjson = array('success'=> $response,
            'errorstr' => $errorstr);


     echo json_encode($arrayjson);
?>
