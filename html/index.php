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
   <h4>2016/09/23</h4>
   <p>There was a problem where portals to the nether sometimes explode when being entered. This is now fixed.</p>
       <p>The reason this was happening has a somewhat long explanation.</p>
   <p>One of the principles I designed these rules around is: 
   <blockquote><b>A world shouldn't be able to be invaded by a powerful player without the regular players of the world doing something that allows it to happen</b></blockquote>
   <p>So, whenever a portal is destroyed in a particular overworld, the corresponding portal in the nether should also be destroyed. Otherwise someone from the nether could
   invade an overworld.</p>
   <p>To implement this, I check for block physics events, which happen whenever a block changes for some reason... hit by a pick ax, started on fire, water falls on it, etc.</p>
   <p>If the portal material is destroyed, it will also trigger a block physics event. So if the code sees one portal of a portal link being destroyed, it would automatically 
   destroy the other one, along with an explosion, basically to look cool.</p>
   <p>When portal is created in a strange place in the nether, with a lava waterfall or something, so that when a player teleports through it, it causes the portal material to be destroyed, then that would
   chain react through the code and cause an explosion</p>
   <p>Anyway, explosions won't occur anymore. Portals may turn to sand, but they won't explode</p>
   
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
