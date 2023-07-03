<?php 
include "header.php";
include "../sqlconnection.php";

/* Seçili Kaydı Siler */
    if(isset($_GET['delete'])){
      $delete_id = $_GET['delete'];
      $delete_cart = $conn->prepare("DELETE FROM `users` WHERE ID = ?");
      $delete_cart->execute([$delete_id]);
      $delete_donet = $conn->prepare("DELETE FROM `request` WHERE USER_ID = ?");
      $delete_donet->execute([$delete_id]);
      header('location:index.php');
   }
 
?>



<br>
<table class="table">
  <thead>
    <tr>
      <th scope="col">TC</th>
      <th scope="col">EMAİL</th>
      <th scope="col">ROLÜ</th>
     
    </tr>
  </thead>
  <tbody>
  <?php
            $select_products = $conn->prepare("SELECT * FROM `users`");
            $select_products->execute();
            if($select_products->rowCount() > 0){
               while($fetch_products = $select_products->fetch(PDO::FETCH_ASSOC)){ 
      
            ?>
    <tr>
      <th scope="row"><?= $fetch_products['TC']; ?></th>
      <td><?= $fetch_products['EMAİL']; ?></td>
     
      <td>
      <select class="form-select form-select-lg mb-3" id='myDropdown_<?= $fetch_products['ID']; ?>' onchange="sendData(<?= $fetch_products['ID']; ?>)">
  <option value="0" <?PHP if ($fetch_products['ROLE'] == "0") { echo "selected='selected'";}?>>ADMİN</option>
  <option value="1" <?PHP if ($fetch_products['ROLE'] == "1") { echo "selected='selected'";}?>>ÖĞRENCİ</option>
  <option value="2" <?PHP if ($fetch_products['ROLE'] == "2") { echo "selected='selected'";}?>>BAĞIŞÇI</option>
</select>
      </td>

      <td><a href="index.php?delete=<?= $fetch_products['ID']; ?>" class="btn btn-danger" onclick="return confirm('Kişi Silinsin mi');">Sil</a></td>
    </tr>
    <?php
         }
      }
   ?>
  </tbody>
</table>
</div>  




<?php include "footer.php"?>