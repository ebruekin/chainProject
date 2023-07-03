<?php


session_start();    
// Oturumu sonlandır
session_destroy();

// Oturum çerezini sil (isteğe bağlı)
if (ini_get("session.use_cookies")) {
    $params = session_get_cookie_params();
    setcookie(session_name(), '', time() - 42000,
        $params["path"], $params["domain"],
        $params["secure"], $params["httponly"]
    );
}

// Tarayıcıyı oturum çerezlerini silmeye yönlendir (isteğe bağlı)
header('location:login.php');
exit;


?>