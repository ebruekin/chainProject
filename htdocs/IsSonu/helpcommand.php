<?php

class helpcommand{

   function GetValText($sqlquery,$column) {
    include "sqlconnection.php";
    $result = $conn->query($sqlquery);

if ($result->rowCount() > 0) {
    $row = $result->fetch(PDO::FETCH_ASSOC);
    $value = $row[$column];
    return $value;
} else {
    echo 'Veri bulunamadı.';
}
   }


}
?>