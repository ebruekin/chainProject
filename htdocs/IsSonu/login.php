<?php
include 'helpcommand.php';
include 'sqlconnection.php';	
$myObject = new helpcommand();


/* KUllanıcı Girişi */
if(isset($_POST['submit'])){
session_start();
   $email = $_POST['email'];
   $email = filter_var($email, FILTER_SANITIZE_STRING);
   $pass = sha1($_POST['pass']);
   $pass = filter_var($pass, FILTER_SANITIZE_STRING);	
   echo $pass;
   $select_user = $conn->prepare("SELECT * FROM `users` WHERE EMAİL = ? AND PASSWORD = ? AND ACTİVE = 1");
   $select_user->execute([$email,$pass]);
   $row = $select_user->fetch(PDO::FETCH_ASSOC);
   if($select_user->rowCount() > 0){
      $ROLE = $myObject ->GetValText("Select * From users where ID = ".$row['ID']." AND ROLE IN(1,2)","ROLE");
      if ($ROLE == "1") {
        session_start();
      $_SESSION['USER_ID'] = $row['ID'];
      }elseif($ROLE == "2"){
        session_start();
        $_SESSION['DONOR_ID'] = $row['ID'];
      }
      echo $row['ID'];
      header('location:index.php');
   }
}

?>
<?php include "header.php" ?>

  
  <div class="content pt-5">
    <div class="container">
      <div class="row">
        <div class="col-md-6">
          <img src="img/undraw_remotely_2j6y.svg" alt="Image" class="img-fluid">
        </div>
        <div class="col-md-6 pt-5 contents">
          <div class="row justify-content-center">
            <div class="col-md-8">
              <div class="mb-4">
              <h3>Giriş Yap</h3> 
            </div>
            <form method="post">
              
              <div class="form-group first">
                <label for="username">Email</label>
                <input type="text" name="email" class="form-control" id="username">
              </div>

              <div class="form-group last mb-4">
                <label for="password">Şifre</label>
                <input type="password" name="pass" class="form-control" id="password">
              </div>
              
              
              

              <input type="submit" name="submit" value="Giriş Yap" class="btn btn-block btn-primary">

            
            </form>
            </div>
          </div>
          
        </div>
        
      </div>
    </div>
  </div>

  
    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/main.js"></script>
  </body>
</html>