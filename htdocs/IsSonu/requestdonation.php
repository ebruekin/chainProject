<?php 
include "header.php";
include "sqlconnection.php";
if(isset($_POST['DonorRequest'])){

    $Header = $_POST['Header'];
    $Header = filter_var($Header, FILTER_SANITIZE_STRING);

    $MESSAGE = $_POST['MESSAGE'];
    $MESSAGE = filter_var($MESSAGE, FILTER_SANITIZE_STRING);
   
    $DONER_PAY = $_POST['DONOR_PAY'];
    $DONER_PAY = filter_var($DONER_PAY, FILTER_SANITIZE_STRING);

 
 
       $insert = $conn->prepare("INSERT INTO `request`(HEADER, MESSAGE, DONOR_PAY , USER_ID ,APPROVAL_STATUS) VALUES(?,?,?,?,?)");
       $insert->execute([$Header,$MESSAGE, $DONER_PAY, $_SESSION['USER_ID'],"0"]);
       if($insert){
          header('location:index.php');
       }
       
    }
?>
<div class="container-xxl bg-light my-3 mx-1 px-1 py-5">
<form  method="post">
<div class="form-group">
    <label for="exampleFormControlInput1">Başlık</label>
    <input type="text" name="Header" class="form-control" id="exampleFormControlInput1">
  </div>

  <div class="form-group">
    <label for="exampleFormControlInput1">Bağış Miktarı</label>
    <input type="text" name="DONOR_PAY" class="form-control" id="exampleFormControlInput1">
  </div>

  
  <div class="form-group">
    <label for="exampleFormControlTextarea1">Mesaj</label>
    <textarea name="MESSAGE" class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
  </div>
  <div class="form-group row my-3">
    <div class="col-sm-10">
      <button type="submit" name="DonorRequest" class="btn btn-primary">Kaydet</button>
    </div>
  </div>
</form>
</div>
<?php include "footer.php" ?>