<?php 
include "header.php";
include "helpcommand.php";
include "sqlconnection.php";
$myObject = new helpcommand();

if(isset($_GET['bagis_kutusu'])){
    $ID = $_GET['bagis_kutusu'];
    $test = $_SESSION['DONOR_ID'];
    $UPDATE = $conn->prepare("UPDATE request SET PAYMENT_STATUS = 1 , DONATE = ? WHERE ID = $ID");
    $UPDATE->execute([$test]);
    header('location:index.php');
  }
?>




    <!-- Causes Start -->
    <div class="container-xxl bg-light my-2 py-2">
        <div class="container py-5">
            <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 500px;">
          
            <?php
            $ROLL = 3;
            if (isset($_SESSION['DONOR_ID'])) {
               echo '  <h1 class="display-6 mb-5">Tüm Bağış İstekleri</h1>';
             }elseif (isset($_SESSION['USER_ID'])) {
                echo ' <div class="display-6 mb-5">
                <a class="btn btn-primary btn-lg py-4 px-5 " href="requestdonation.php">
                    Bağış İsteği Oluştur
                   
                </a>
            </div>';
             }
            ?>
               
            </div>
            <div class="row g-4 justify-content-center">

            <?php
            $select_products = $conn->prepare("SELECT * FROM `request` WHERE APPROVAL_STATUS = 1 and PAYMENT_STATUS = 0");
            $select_products->execute();
            if($select_products->rowCount() > 0){
               while($fetch_products = $select_products->fetch(PDO::FETCH_ASSOC)){ 
      
            ?>
      
                <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.1s">
                    <div class="causes-item d-flex flex-column bg-white border-top border-5 border-primary rounded-top overflow-hidden h-100">
                        <div class="text-center p-4 pt-0">
                            <div class="d-inline-block bg-primary text-white rounded-bottom fs-5 pb-1 px-3 mb-4">
                                <small>Bağış İsteği</small>
                            </div>
                            <h5 class="mb-3"><?= $fetch_products['HEADER']; ?></h5>
                            <p><?= $fetch_products['MESSAGE']; ?></p>
                            <div class="causes-progress bg-light p-3 pt-2">
                                <div class="d-flex h3 justify-content-between">
                                    <label class="text-dark mx-auto"> <?= $fetch_products['DONOR_PAY']; ?> <small class="text-body">TL</small></label>
                                </div>
                            </div>
                        </div>
                        <div class="position-relative mt-auto">
                            <img class="img-fluid" src="img/bag_yap_-1024x489.png" alt="">
                            <?php
                            if (isset($_SESSION['DONOR_ID'])) {
                                echo ' <div class="causes-overlay">
                                <a class="btn btn-outline-primary mx-1" href="index.php?bagis_kutusu='.$fetch_products['ID'].'">
                                    Bağış Yap
                                    <div class="d-inline-flex btn-sm-square bg-primary text-white rounded-circle ms-2">
                                        <i class="fa fa-arrow-right"></i>
                                    </div>
                                </a>
                            </div>';
                              }
                            ?>
                            
                            
                        </div>
                    </div>
                </div>

                <?php
         }
      }
   ?>

            </div>
        </div>
    </div>
 <?php include "footer.php"?>