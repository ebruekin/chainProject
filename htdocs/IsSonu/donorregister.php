<?php
include "helpcommand.php";
include "sqlconnection.php";
$myObject = new helpcommand();

if(isset($_POST['add_Donor'])){

    $ID_CARD = $_POST['ID_CARD'];
    $ID_CARD = filter_var($ID_CARD, FILTER_SANITIZE_STRING);

    $NAME = $_POST['NAME'];
    $NAME = filter_var($NAME, FILTER_SANITIZE_STRING);
   
    $PASSWORD = $_POST['PASSWORD'];
    $PASSWORD = filter_var($PASSWORD, FILTER_SANITIZE_STRING);
    $hashedPassword = sha1($PASSWORD);
 
 
       $insert = $conn->prepare("INSERT INTO `users`(EMAİL, PASSWORD, TC , ROLE) VALUES(?,?,?,?)");
       $insert->execute([$NAME,$hashedPassword, $ID_CARD, "2"]);
       if($insert){
          session_start();
	        $_SESSION['DONOR_ID'] = $myObject ->GetValText("SELECT * FROM users where TC = ".$ID_CARD." where ACTİVE = 1","ID");
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
              <h3>BağışÇI Olarak Kayıt Ol</h3> 
            </div>
            <form action="#" method="post">

              <div class="form-group first">
                <label for="username">TC</label>
                <input type="text" class="form-control" name="ID_CARD" id="username" Required>
              </div>

              <div class="form-group first">
                <label for="username">Email</label>
                <input type="email" class="form-control" name="NAME" id="username" Required >
              </div>

              <div class="form-group last mb-2">
                <label for="password">Şifre</label>
                <input type="password" name="PASSWORD" class="form-control" id="password">
              </div>
            
              <input type="submit" name="add_Donor" value="Kayıt Ol" class="btn btn-block btn-primary">

            
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