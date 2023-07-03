<?php
include "helpcommand.php";
include "sqlconnection.php";
$myObject = new helpcommand();
/*Kullanıcı Ekle*/
if(isset($_POST['add_User'])){


    $TC = $_POST['TC'];
    $TC = filter_var($TC, FILTER_SANITIZE_STRING);

    $EMAIL = $_POST['EMAIL'];
    $EMAIL = filter_var($EMAIL, FILTER_SANITIZE_STRING);
   
    $FILE = $_FILES['FILE']['name'];
    $FILE = filter_var($FILE, FILTER_SANITIZE_STRING);
    $FILE = $_FILES['FILE']['size'];
    $FILE_tmp_name_01 = $_FILES['FILE']['tmp_name'];
    $FILE_folder_01 = 'img/'.$FILE;

    $PASSWORD = $_POST['PASSWORD'];
    $PASSWORD = filter_var($PASSWORD, FILTER_SANITIZE_STRING);
    $hashedPassword = sha1($PASSWORD);
    
       $insert = $conn->prepare("INSERT INTO `users`(EMAİL, PASSWORD, TC , ROLE , DOC) VALUES(?,?,?,?,?)");
       $insert->execute([$EMAIL,$hashedPassword, $TC, "1",$FILE]);
       if($insert){
        move_uploaded_file($FILE_tmp_name_01, $FILE_folder_01);
          session_start();
          $GİRENKİSİ = $myObject ->GetValText("SELECT * FROM users where TC = '".$TC."' and PASSWORD = '".$hashedPassword."' AND ACTİVE = 1","ID");
	        $_SESSION['USER_ID'] = $myObject ->GetValText("SELECT * FROM users where TC = '".$TC."' and PASSWORD = '".$hashedPassword."' AND ACTİVE = 1","ID");
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
              <h3>Kayıt Ol</h3> 
            </div>
            <form method="post" enctype="multipart/form-data">

              <div class="form-group first">
                <label for="username">TC</label>
                <input type="text" minlength="11" maxlength="11" name="TC" class="form-control" id="username" Required>
              </div>

              <div class="form-group first">
                <label for="username">Email</label>
                <input type="email" name="EMAIL" class="form-control" id="username" Required >
              </div>    


              <div class="form-group first">
                <label for="username">Öğrenci Belgesi</label>
                <input type="file" name="FILE" class="form-control" id="username" Required >
              </div>

              <div class="form-group last mb-2">
                <label for="password">Şifre</label>
                <input type="password" name="PASSWORD" class="form-control" id="password">
              </div>
              
              <div class="d-flex mb-2 align-items-center">

              <span class="ml-auto text-end"><a href="donorregister.php" class="forgot-pass">Bağışçı Hesabı Oluştur</a></span> 
              </div>
              

              <input type="submit" name="add_User" value="Kayıt Ol" class="btn btn-block btn-primary">

            
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