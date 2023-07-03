<?php

include 'sqlconnection.php';	
$userID = "0";

session_start();
if (isset($_SESSION['DONOR_ID'])) {
    $userID = isset($_SESSION['DONOR_ID']);
}elseif (isset($_SESSION['USER_ID'])) {
    $userID = isset($_SESSION['USER_ID']);
}
echo $userID;
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Saira:wght@500;600;700&display=swap" rel="stylesheet"> 

    <!-- Icon Font Stylesheet -->
    


    

   

    <!-- Template Stylesheet -->
    <link href="css/style.css" rel="stylesheet">
     <!-- Customized Bootstrap Stylesheet -->
     <link href="css/bootstrap.min.css" rel="stylesheet">
     <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
</head>

<body>
 

    <!-- Navbar Start -->
    <div class="bg-dark container-fluid fixed-top px-0 wow fadeIn" data-wow-delay="0.1s">
    
        <nav class="navbar navbar-expand-lg bg-dark navbar-dark py-lg-0 px-lg-5 wow fadeIn" data-wow-delay="0.1s">
            <a href="index.php" class="navbar-brand ms-4 ms-lg-0">
                <h1 class="fw-bold text-primary m-0"><span class="text-white">SDS</span></h1>
            </a>
            <button type="button" class="navbar-toggler me-4" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <div class="navbar-nav ms-auto p-4 p-lg-0">
                   

                </div>
                <?php
                $UserEklenenSayfa = "";
                if (isset($_SESSION['USER_ID'])) {
                    $UserEklenenSayfa = '<a class="btn btn-primary py-2 mx-2 px-3" href="donors.php">
                    Yapılan İşlemler
                   </a>';
                    }else {
                        $UserEklenenSayfa = '<a class="btn btn-primary py-2 mx-2 px-3" href="donorbagis.php">
                    Yapılan İşlemler
                   </a>';
                    }
                /* Kullanıcıc Girdi İse */
                if (isset($_SESSION['DONOR_ID']) || isset($_SESSION['USER_ID'])) {
                echo '
            
            <div class="d-none d-lg-flex ms-2">
                '.$UserEklenenSayfa.'        
                <a class="btn btn-primary py-2 px-3" href="logout.php">
                    Çıkış Yap
                   
                </a>
            </div>
            ';
                }
                else{
                    echo '   <div class="d-none d-lg-flex ms-2">
                    <a class="btn btn-outline-primary py-2 px-3" href="login.php">
                        Giriş Yap
                        <div class="d-inline-flex btn-sm-square bg-white text-primary rounded-circle ms-2">
                        </div>
                    </a>
                </div>
                <div class="d-none d-lg-flex ms-3">
                    <a class="btn btn-outline-primary py-2 px-3 mx-1" href="register.php">
                        Kayıt Ol
                        <div class="d-inline-flex btn-sm-square bg-white text-primary rounded-circle ms-2">
                        </div>
                    </a>
                </div>';
                }
                ?>
                

             
            </div>
        </nav>
    </div>
    <!-- Navbar End -->

