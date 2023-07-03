<?php 
include "header.php";
include "../sqlconnection.php";
?>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Adı Soyadı</th>
      <th scope="col">Başlık</th>
      <th scope="col">Yapılan Bağış İsteklerini Gör</th>
    </tr>
  </thead>
  <tbody>
  <?php
            $select_products = $conn->prepare("SELECT * FROM `users` WHERE ROLE = 1");
            $select_products->execute();
            if($select_products->rowCount() > 0){
               while($fetch_products = $select_products->fetch(PDO::FETCH_ASSOC)){ 
      
            ?>
    <tr>
      <th scope="row"><?= $fetch_products['TC']; ?></th>
      <td><?= $fetch_products['EMAİL']; ?></td>
      <td>
      <!-- Button trigger modal -->
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal<?= $fetch_products['ID']; ?>">
  Bağış İstekleri
</button>
<!-- Modal -->
<div class="modal fade" id="exampleModal<?= $fetch_products['ID']; ?>" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Mesaj</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
     
<table class="table table-striped">
  <thead>
    <tr>
      <th scope="col">Başlık</th>
      <th scope="col">Mesajı</th>
      <th scope="col">Bağış Miktarı</th>
      <th scope="col">Durumu</th>
    </tr>
  </thead>
  <tbody>
  <?php
            $select_donations = $conn->prepare("SELECT * FROM `request` where USER_ID =".$fetch_products['ID']);
            $select_donations->execute();
            if($select_donations->rowCount() > 0){
               while($fetch_donations = $select_donations->fetch(PDO::FETCH_ASSOC)){ 
            ?>

    <tr>
      <th><?= $fetch_donations['HEADER']; ?></th>
      <td><?= $fetch_donations['MESSAGE']; ?></td>
      <td><?= $fetch_donations['DONOR_PAY']; ?></td>
      <td><?= ($fetch_donations['APPROVAL_STATUS'] == "1") ? 'AKTİF' : 'PASİF' ?></td>
    </tr>
    <?php
         }
      }
   ?>
  </tbody>
</table>
    
   
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Kapat</button>
      </div>
    </div>
  </div>
</div>
     </td>
    </tr>
    

    <?php
         }
      }
   ?>
  </tbody>
</table>

<?php 
include "footer.php";
?>