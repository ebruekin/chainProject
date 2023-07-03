<?php
include "../sqlconnection.php";
if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $selectedOption = $_POST["selectedOption"];
  $id = $_POST["id"];

  // ID'ye göre işlem yapın
  // ...
    

  
     $insert = $conn->prepare("UPDATE users SET ROLE = ? WHERE ID =".$id);
     $insert->execute([$selectedOption]);
     if($insert){
        header('location:index.php');
     }
     
  // İşlem sonucunu döndürün

}
?>
