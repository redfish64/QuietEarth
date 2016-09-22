<ul class="menu_ul" id="menu">
  <li class="menu_li"><a id="menu_index" href="index.php"">Home</a></li>
  <li class="menu_li"><a id="menu_rules" href="rules.php">Overview</a></li>
  <li class="menu_li"><a id="menu_blog" href="blog.php">Blog</a></li>
  <li class="menu_li"><a id="menu_contact" href="contact.php">Contact</a></li>
</ul> 
<script src="jquery.js"></script>
<script>
$( document ).ready(function() 
{

  console.log("setting height to %d",$('#menu').height());
  document.body.style.paddingTop = $('#menu').height()+"px";
  $('#some-menu').load('some-local-path/menu.html');
 
});

  document.getElementById("menu_<?php echo $active; ?>").className +=' active';

</script>

      <h1>The Quiet Craft</h1>
