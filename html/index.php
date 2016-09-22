<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
   <title>The Quiet Craft</title>
   <link rel="stylesheet" type="text/css" href="style.css">
</head>      
<body>
   <?php
   $active='index';
   include('menu.php');
   ?>
The Quiet Craft is a minecraft mod where death has real in-game consequences.

   <h3>News</h3>
   <h4>2016/09/22</h4>
   <p>Chatting broke with last nights fix. I updated the way config
   files worked, and the ChatManager code was still using the old way
   to read config variables. So it read max chat hearing distance as 0,
   therefore muting everybody</p> 
   <p>This is fixed now. I also reduced the souls that a newbie first receives from 5 to 3</p>
  <p>I also made it that if you are carrying more than 10 souls and die, you will drop all but those 10</p>
   <h4>2016/09/21</h4>
   <p>Now each player will receive soul shards while playing. 9 soul shards can be crafted into a new soul.</p>
</body>
</html>
