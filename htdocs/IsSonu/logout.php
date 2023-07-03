<?php


session_start();    
// Oturumu sonlandır
session_destroy();



// Tarayıcıyı oturum çerezlerini silmeye yönlendir (isteğe bağlı)
header('location:index.php');
exit;


?>