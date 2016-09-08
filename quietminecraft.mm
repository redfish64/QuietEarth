<map version="1.0.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1467879125437" ID="ID_1423198636" MODIFIED="1467921769582" TEXT="New Mindmap">
<node CREATED="1456401391710" ID="ID_429240407" MODIFIED="1467429279611" POSITION="right" TEXT="TheQuietMineCraft">
<node CREATED="1456401402686" ID="ID_381598210" MODIFIED="1456401409642" TEXT="Based on &quot;The Quiet Earth&quot; movie"/>
<node CREATED="1456401409942" ID="ID_1907732955" MODIFIED="1456401464570" TEXT="Worlds are visited in a certain order every time the player dies. Based on ip/username, the same world will never be visited again">
<node CREATED="1456401471943" ID="ID_694133998" MODIFIED="1456401506442" TEXT="If there is a limit to the number of worlds, an ancient world that no one visits anymore will be deleted to make room for a new one"/>
</node>
<node CREATED="1456401550230" ID="ID_1399011691" MODIFIED="1456401558850" TEXT="A portal can be built to a layer of hell, at random">
<node CREATED="1456401562982" ID="ID_1905863129" MODIFIED="1456401580850" TEXT="If a player offers gold, diamonds, or something, they can choose the layer based on the number of items"/>
</node>
<node CREATED="1456401582758" ID="ID_163144442" MODIFIED="1456401609362" TEXT="If two worlds open to the same layer of hell, players can walk between them"/>
<node CREATED="1456401669598" ID="ID_1334902554" MODIFIED="1456401685730" TEXT="When dying in hell, all souls are released on the ground next to the player"/>
<node CREATED="1456401627646" ID="ID_1291763757" MODIFIED="1456401715050" TEXT="Souls can be retrieved left from a dead player "/>
<node CREATED="1456401727662" ID="ID_1912358908" MODIFIED="1456401735322" TEXT="Portals always form close by to each other."/>
<node CREATED="1456401719238" ID="ID_637666145" MODIFIED="1456401724282" TEXT="Maybe another way to get souls?"/>
<node CREATED="1456401735654" ID="ID_912652450" MODIFIED="1456401759074" TEXT="A persistent portal always remains where it was created"/>
<node CREATED="1456401514606" ID="ID_1113304603" MODIFIED="1456401523314" TEXT="Each player gets 5 souls when starting out"/>
<node CREATED="1456401525750" ID="ID_669140945" MODIFIED="1456401652490" TEXT="When they die, they lose one soul and restart in the same world"/>
<node CREATED="1456401653094" ID="ID_811836942" MODIFIED="1456401660050" TEXT="If they run out of souls, they spawn to a new one"/>
<node CREATED="1467445003118" ID="ID_348172132" MODIFIED="1467921806022" TEXT="database">
<node CREATED="1467445008976" ID="ID_1085108667" MODIFIED="1467445130668" TEXT="Player">
<node CREATED="1467445132629" ID="ID_1843855534" MODIFIED="1467445135377" TEXT="player_id"/>
<node CREATED="1467445029110" ID="ID_1753623832" MODIFIED="1467445035189" TEXT="ip"/>
<node CREATED="1467445037534" ID="ID_1362996263" MODIFIED="1467445039395" TEXT="name"/>
<node CREATED="1467445040682" ID="ID_215217443" MODIFIED="1467445057164" TEXT="souls"/>
<node CREATED="1467445059822" ID="ID_860209672" MODIFIED="1467445064363" TEXT="world_id"/>
</node>
<node CREATED="1467445067167" ID="ID_1016253139" MODIFIED="1467445069059" TEXT="World">
<node CREATED="1467445069949" ID="ID_430708925" MODIFIED="1467445071427" TEXT="world_id"/>
<node CREATED="1467445071729" ID="ID_1567351968" MODIFIED="1467445092105" TEXT="data location or whatever"/>
</node>
<node CREATED="1467445118489" ID="ID_590870163" MODIFIED="1467445126463" TEXT="PlayerLog">
<node CREATED="1467445138537" ID="ID_1001722267" MODIFIED="1467445140325" TEXT="player_id"/>
<node CREATED="1467445144819" ID="ID_1113831247" MODIFIED="1467445154194" TEXT="world_id"/>
<node CREATED="1467445155252" ID="ID_1384876293" MODIFIED="1467445263089" TEXT="datetime"/>
<node CREATED="1467445165533" ID="ID_1158760905" MODIFIED="1467445166971" TEXT="action">
<node CREATED="1467445174836" ID="ID_1229776440" MODIFIED="1467445176498" TEXT="enter"/>
<node CREATED="1467445176966" ID="ID_1530088633" MODIFIED="1467445178740" TEXT="exit"/>
<node CREATED="1467445179026" ID="ID_1210235092" MODIFIED="1467445226230" TEXT="permadeath"/>
</node>
</node>
</node>
<node CREATED="1467877033531" ID="ID_199733925" MODIFIED="1467877077979" TEXT="the same ip/username/etc can&apos;t spawn in the same world twice">
<node CREATED="1467877051820" ID="ID_1400507634" MODIFIED="1467877082591" TEXT="Even if more than one person is logging in from the same ip, it will work">
<node CREATED="1467877084395" ID="ID_1342885622" MODIFIED="1467877108471" TEXT="If two people share the same ip, it just means that each will never see each other&apos;s world "/>
</node>
</node>
</node>
<node CREATED="1467921769573" ID="ID_1041534690" MODIFIED="1467921770582" POSITION="right" TEXT="version">
<node CREATED="1467921759966" ID="ID_1668707427" MODIFIED="1467995686723" TEXT="7/7/16">
<node CREATED="1467921825795" ID="ID_288816071" MODIFIED="1467921828519" TEXT="simple"/>
<node CREATED="1467921773132" ID="ID_1732606033" MODIFIED="1467921781527" TEXT="one world, different spawn points"/>
<node CREATED="1467445003118" ID="ID_1118727661" MODIFIED="1467921806022" TEXT="database">
<node CREATED="1467445008976" ID="ID_26428946" MODIFIED="1467445130668" TEXT="Player">
<node CREATED="1467445132629" ID="ID_1609692610" MODIFIED="1467445135377" TEXT="player_id"/>
<node CREATED="1467445029110" ID="ID_1936413697" MODIFIED="1467445035189" TEXT="ip"/>
<node CREATED="1467445037534" ID="ID_526931428" MODIFIED="1467445039395" TEXT="name"/>
<node CREATED="1467921904498" ID="ID_950295170" MODIFIED="1467921916871" TEXT="last_spawn_point">
<node CREATED="1467921917971" ID="ID_785051782" MODIFIED="1467921924222" TEXT="This is the spawn point since death"/>
</node>
</node>
<node CREATED="1467445067167" ID="ID_582716704" MODIFIED="1467921843525" TEXT="Spawn Point">
<node CREATED="1467445069949" ID="ID_949751123" MODIFIED="1467921848909" TEXT="spawn_id"/>
</node>
<node CREATED="1467445118489" ID="ID_965604997" MODIFIED="1467445126463" TEXT="PlayerLog">
<node CREATED="1467445138537" ID="ID_1924141998" MODIFIED="1467445140325" TEXT="player_id"/>
<node CREATED="1467445144819" ID="ID_1738859554" MODIFIED="1467921862414" TEXT="spawn_point_id"/>
<node CREATED="1467445155252" ID="ID_1390801467" MODIFIED="1467445263089" TEXT="datetime"/>
<node CREATED="1467445165533" ID="ID_1838503533" MODIFIED="1467445166971" TEXT="action">
<node CREATED="1467445174836" ID="ID_558097404" MODIFIED="1467921870493" TEXT="spawn"/>
<node CREATED="1467445176966" ID="ID_25276980" MODIFIED="1467445178740" TEXT="exit"/>
<node CREATED="1467445179026" ID="ID_1908946668" MODIFIED="1467445226230" TEXT="permadeath"/>
</node>
</node>
</node>
</node>
<node CREATED="1467995679929" ID="ID_1767029268" MODIFIED="1467995684013" TEXT="7/8/16">
<node CREATED="1467995688489" ID="ID_1610539266" MODIFIED="1473081713688" TEXT="multi world">
<node CREATED="1473081713676" ID="ID_1980286089" MODIFIED="1473081721397" TEXT="single world with multi spawn points?">
<node CREATED="1467995735168" ID="ID_221398552" MODIFIED="1473081736366" TEXT="coordinates of player can be revealed allowing players to find where they once were"/>
</node>
<node CREATED="1467445003118" ID="ID_1669710513" MODIFIED="1467921806022" TEXT="database">
<node CREATED="1467445008976" ID="ID_1726378833" MODIFIED="1467445130668" TEXT="Player">
<node CREATED="1467445037534" ID="ID_1587864216" MODIFIED="1468003618458" TEXT="uuid"/>
<node CREATED="1467445059822" ID="ID_950797792" MODIFIED="1468054942621" TEXT="world_id"/>
</node>
<node CREATED="1468054943401" ID="ID_1386911060" MODIFIED="1468054945035" TEXT="World">
<node CREATED="1468054946105" ID="ID_1671473401" MODIFIED="1468054952069" TEXT="id"/>
<node CREATED="1468054952433" ID="ID_1289061435" MODIFIED="1468054954029" TEXT="name"/>
</node>
<node CREATED="1467445118489" ID="ID_1944689299" MODIFIED="1467445126463" TEXT="PlayerLog">
<node CREATED="1467445138537" ID="ID_1787179342" MODIFIED="1468003630410" TEXT="player_uuid"/>
<node CREATED="1467445144819" ID="ID_1836162242" MODIFIED="1468054939235" TEXT="world_id"/>
<node CREATED="1467445155252" ID="ID_50553467" MODIFIED="1467445263089" TEXT="datetime"/>
<node CREATED="1467445165533" ID="ID_1060242751" MODIFIED="1467445166971" TEXT="action">
<node CREATED="1467445174836" ID="ID_693366832" MODIFIED="1468005051817" TEXT="login"/>
<node CREATED="1467445176966" ID="ID_646167573" MODIFIED="1468005053993" TEXT="logout"/>
<node CREATED="1468005070125" ID="ID_403353019" MODIFIED="1468005076649" TEXT="new_spawn">
<node CREATED="1468005078365" ID="ID_251825192" MODIFIED="1468005083393" TEXT="first time to spawn in a world"/>
</node>
<node CREATED="1467445179026" ID="ID_577635901" MODIFIED="1467445226230" TEXT="permadeath">
<node CREATED="1468005088581" ID="ID_775344642" MODIFIED="1468005101385" TEXT="player died and is removed from this world forever"/>
</node>
</node>
</node>
</node>
<node CREATED="1468004720758" ID="ID_1245911041" MODIFIED="1468004724361" TEXT="in memory">
<node CREATED="1468004699429" ID="ID_1089857093" MODIFIED="1468005000195" TEXT="active worlds">
<node CREATED="1468004709765" ID="ID_104090092" MODIFIED="1468005011649" TEXT="player_count"/>
</node>
</node>
</node>
<node CREATED="1467996547870" ID="ID_1741852940" MODIFIED="1467996552931" TEXT="startup">
<node CREATED="1467996554087" ID="ID_1545746531" MODIFIED="1467996557883" TEXT="create/open database"/>
<node CREATED="1473081969417" ID="ID_845212550" MODIFIED="1473081976021" TEXT="initialize worlds"/>
</node>
<node CREATED="1467996559071" ID="ID_664342422" MODIFIED="1467996562123" TEXT="on player login">
<node CREATED="1467996564439" ID="ID_1175726893" MODIFIED="1467996567715" TEXT="lookup of player"/>
<node CREATED="1467996573359" ID="ID_1505544654" MODIFIED="1467996578074" TEXT="if player found">
<node CREATED="1467996579499" ID="ID_123843905" MODIFIED="1473081906093" TEXT="player is assumed to be in the right place, so we don&apos;t move them"/>
<node CREATED="1473081908345" ID="ID_1419504634" MODIFIED="1473081916621" TEXT="record to db action"/>
</node>
<node CREATED="1467996601421" ID="ID_601103134" MODIFIED="1467996613070" TEXT="else">
<node CREATED="1467996613983" ID="ID_12034274" MODIFIED="1467996617115" TEXT="create new player"/>
<node CREATED="1467996617431" ID="ID_98625611" MODIFIED="1468004307986" TEXT="choose world">
<node CREATED="1468004311646" ID="ID_1107200345" MODIFIED="1468004315849" TEXT="suitable world">
<node CREATED="1468004324798" ID="ID_1690292589" MODIFIED="1468004348585" TEXT="contains the most active people of all worlds"/>
<node CREATED="1468004349317" ID="ID_1545438265" MODIFIED="1468004363089" TEXT="of equal active people, contains the most people that ever visited"/>
</node>
</node>
</node>
<node CREATED="1467996645991" ID="ID_510693726" MODIFIED="1467996651475" TEXT="update playerlog"/>
</node>
<node CREATED="1468004532414" ID="ID_731819014" MODIFIED="1468004539393" TEXT="options">
<node CREATED="1468004540550" ID="ID_1509246602" MODIFIED="1468004562897" TEXT="Should we make each world a copy of the world the person died in?">
<node CREATED="1468004570446" ID="ID_1723863330" MODIFIED="1468004606721" TEXT="When the player spawns in the new world, its in a slightly different place, incase the spawn point is some sort of trap room"/>
<node CREATED="1468004624709" ID="ID_1512606921" MODIFIED="1468004638353" TEXT="This way all the work the player did wouldn&apos;t be lost, but the people would be gone."/>
<node CREATED="1468004655573" ID="ID_20847600" MODIFIED="1468004670355" TEXT="Would only occur if a new world was built for the player personally"/>
</node>
</node>
<node CREATED="1473080306960" ID="ID_1727806319" MODIFIED="1473099187029" TEXT="spawning">
<node CREATED="1473080310848" ID="ID_722706054" MODIFIED="1473080328740" TEXT="when a player dies, they go to the next world they haven&apos;t visited yet">
<node CREATED="1473080329800" ID="ID_11412421" MODIFIED="1473080334965" TEXT="If the player has visited all the worlds then">
<node CREATED="1473080335880" ID="ID_1533665923" MODIFIED="1473080348765" TEXT="If there are any worlds that haven&apos;t been visited in a month, the player can join those"/>
<node CREATED="1473080349120" ID="ID_133161436" MODIFIED="1473080365052" TEXT="Otherwise the player goes to nether"/>
</node>
</node>
</node>
<node CREATED="1473099187689" ID="ID_1690179985" MODIFIED="1473099211850" TEXT="server">
<node CREATED="1473099211830" ID="ID_744280517" MODIFIED="1473099213036" TEXT="vultr">
<node CREATED="1473099195536" ID="ID_1971885085" MODIFIED="1473099200787" TEXT="$60 per month">
<node CREATED="1473099202033" ID="ID_386042811" MODIFIED="1473099203621" TEXT="8 gig"/>
<node CREATED="1473099204552" ID="ID_1739776195" MODIFIED="1473099205660" TEXT="2 cpu"/>
<node CREATED="1473099206328" ID="ID_15722012" MODIFIED="1473099209084" TEXT="120 GB"/>
<node CREATED="1473099339832" ID="ID_253230457" MODIFIED="1473099343213" TEXT="10 TB bandwidth"/>
</node>
<node CREATED="1473099286913" ID="ID_1878431273" MODIFIED="1473099290772" TEXT="about 100 players"/>
</node>
</node>
<node CREATED="1473099421192" ID="ID_842966636" MODIFIED="1473147407697" TEXT="thoughts">
<node CREATED="1473099425896" ID="ID_1629893461" MODIFIED="1473099480125" TEXT="If we have a maximum of 50 worlds, it seems more like we are just a perma death server with extra lives">
<node CREATED="1473099481585" ID="ID_1603276191" MODIFIED="1473099510045" TEXT="All we need to do is prevent the high death users from joining servers populated by more friendly people."/>
<node CREATED="1473099517672" ID="ID_1960633103" MODIFIED="1473099540868" TEXT="Which means we can reuse as many worlds as we want. "/>
</node>
<node CREATED="1473147408517" ID="ID_1217376109" MODIFIED="1473147436571" TEXT="If the player dies and goes to the nether, then what? Is he nether forever?">
<node CREATED="1473147526518" ID="ID_1953966969" MODIFIED="1473147544482" TEXT="As time goes by and worlds are abandoned, they become revistable"/>
<node CREATED="1473147546509" ID="ID_545265495" MODIFIED="1473147561155" TEXT="People are banned from worlds for a limited period of time???">
<node CREATED="1473147567109" ID="ID_817469872" MODIFIED="1473147578497" TEXT="Banned until they are abandoned for a particular period of time"/>
</node>
<node CREATED="1473147590037" ID="ID_473333231" MODIFIED="1473147605913" TEXT="So people get a set of worlds they are allowed to visit when they die"/>
<node CREATED="1473147608173" ID="ID_574352667" MODIFIED="1473147621378" TEXT="Everytime they die they lose the ability to revisit the world through death">
<node CREATED="1473147622662" ID="ID_138449464" MODIFIED="1473147635986" TEXT="If the world is abandoned long enough, it resets for all players?">
<node CREATED="1473147682101" ID="ID_868913248" MODIFIED="1473147701730" TEXT="Yes, and probably gets a new spawn point, nearby, but not too close to the original spawn point"/>
<node CREATED="1473147723501" ID="ID_1865020102" MODIFIED="1473147739443" TEXT="So a world gets a visit_id">
<node COLOR="#999999" CREATED="1473147740797" ID="ID_996779680" MODIFIED="1473147814165">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      Each player starts with a visit_id of 0
    </p>
  </body>
</html></richcontent>
<font NAME="SansSerif" SIZE="10"/>
</node>
<node CREATED="1473147863533" ID="ID_54278056" MODIFIED="1473147892603" TEXT="Player logs record which visit_ids the player has seen"/>
<node CREATED="1473147893133" ID="ID_1851855622" MODIFIED="1473147913227" TEXT="If a player runs out of visit_ids, they go to nether"/>
</node>
</node>
</node>
</node>
<node CREATED="1473148032502" ID="ID_663027019" MODIFIED="1473148035937" TEXT="monetizing">
<node CREATED="1473148037638" ID="ID_1447795321" MODIFIED="1473148042634" TEXT="beta period then subscription fee">
<node CREATED="1473148049678" ID="ID_1028255873" MODIFIED="1473148058569" TEXT="don&apos;t mention subscription period until beta ends"/>
<node CREATED="1473148071613" ID="ID_1653948769" MODIFIED="1473148107355" TEXT="subscription cheap. $4.99 a month or something?"/>
<node CREATED="1473148108350" ID="ID_1594113529" MODIFIED="1473148115635" TEXT="newcomers get trial period"/>
</node>
</node>
</node>
<node CREATED="1473329224300" ID="ID_999690107" MODIFIED="1473329225537" TEXT="portals">
<node CREATED="1473329227093" ID="ID_702637419" MODIFIED="1473329240841" TEXT="create a portal code snippet (from multiverse netherportals">
<node CREATED="1473329241837" ID="ID_1382927870" MODIFIED="1473329248491">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;} else if (toWorld.getEnvironment() == World.Environment.THE_END &amp;&amp; type == PortalType.END) {
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Location loc = new Location(event.getTo().getWorld(), 100, 50, 0); // This is the vanilla location for obsidian platform.
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;event.setTo(loc);
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Block block = loc.getBlock();
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;for (int x = block.getX() - 2; x &lt;= block.getX() + 2; x++) {
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;for (int z = block.getZ() - 2; z &lt;= block.getZ() + 2; z++) {
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Block platformBlock = loc.getWorld().getBlockAt(x, block.getY() - 1, z);
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;if (platformBlock.getType() != Material.OBSIDIAN) {
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;platformBlock.setType(Material.OBSIDIAN);
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;}
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;for (int yMod = 1; yMod &lt;= 3; yMod++) {
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Block b = platformBlock.getRelative(BlockFace.UP, yMod);
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;if (b.getType() != Material.AIR) {
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;b.setType(Material.AIR);
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;}
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;}
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;}
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;}
    </p>
    <p>
      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;}
    </p>
    <p>
      &#160;
    </p>
  </body>
</html></richcontent>
</node>
</node>
<node CREATED="1473329257437" ID="ID_38201515" MODIFIED="1473329272600" TEXT="portals won&apos;t work automatically, because each world needs its own nether world"/>
<node CREATED="1473332811671" ID="ID_1833829504" MODIFIED="1473344960012" TEXT="can we deactivate a portal?"/>
<node CREATED="1473344962057" ID="ID_224671769" MODIFIED="1473345013644" TEXT="each user gets a special obsedian block when joining. If the leaders of the world wish, they can ask the user gives up this portal block. ">
<node CREATED="1473345015728" ID="ID_532487696" MODIFIED="1473345016796" TEXT="Without it, portals can&apos;t be made."/>
<node CREATED="1473345017233" ID="ID_596003341" MODIFIED="1473345050380" TEXT="Portals made in the nether with this block will only go to the world of the portal block"/>
<node CREATED="1473345054064" ID="ID_1989557766" MODIFIED="1473345054064" TEXT=""/>
</node>
<node CREATED="1473332784744" ID="ID_1272767155" MODIFIED="1473349981877" TEXT="thoughts">
<node CREATED="1473332791007" ID="ID_927903694" MODIFIED="1473332799738" TEXT="there should only be one active portal per world">
<node CREATED="1473332800726" ID="ID_956590528" MODIFIED="1473332810354" TEXT="if user creates another, the previous portal is deactivated"/>
</node>
<node CREATED="1473332822574" ID="ID_1436607596" MODIFIED="1473332839346" TEXT="portals from a world always appear in the same place in the nether?">
<node CREATED="1473332840238" ID="ID_437514496" MODIFIED="1473332853682" TEXT="what if the nether location is flooded with lava, etc?"/>
<node CREATED="1473332885911" ID="ID_762527727" MODIFIED="1473332914202" TEXT="someone may booby trap nether portal for a world, or for all worlds they can">
<node CREATED="1473332917086" ID="ID_1906824224" MODIFIED="1473332921234" TEXT="is this acceptable?">
<node CREATED="1473332952774" ID="ID_1709771872" MODIFIED="1473332978716" TEXT="I think yes but when worlds are revisted, their &quot;nether spawn&quot; point is randomized"/>
<node CREATED="1473333006966" ID="ID_32447400" MODIFIED="1473333018706" TEXT="This provides a lot of interesting permanant world changes">
<node CREATED="1473333019526" ID="ID_708095488" MODIFIED="1473333034074" TEXT="Someone could become &quot;king of hell&quot; by controlling all the world ports"/>
</node>
</node>
</node>
</node>
<node CREATED="1473333072158" ID="ID_914297837" MODIFIED="1473333078970" TEXT="the balance of power is somewhat scary.">
<node CREATED="1473333079934" ID="ID_1171876590" MODIFIED="1473333119348" TEXT="A player who acts nice, could enter a world, create a nether portal in a hidden location, allowing all his evil friends to come in away from the spawn point">
<node CREATED="1473333150838" ID="ID_1141185602" MODIFIED="1473333163651" TEXT="This is too much potential for destruction"/>
</node>
</node>
<node CREATED="1473333201447" ID="ID_867114380" MODIFIED="1473333219354" TEXT="Maybe first user gets a special item that only he can use to create portals">
<node CREATED="1473333221150" ID="ID_830564560" MODIFIED="1473333234986" TEXT="A special flint and steel, that only works for that world and lights a portal">
<node CREATED="1473333670151" ID="ID_1603563414" MODIFIED="1473333678979" TEXT="We will need special fire?"/>
<node CREATED="1473333680887" ID="ID_807915352" MODIFIED="1473333685428" TEXT="This may be difficult to implement"/>
</node>
<node CREATED="1473333286271" ID="ID_1554586392" MODIFIED="1473333322482" TEXT="This way there could still be a &quot;king of hell&quot;, but it&apos;d be a lot harder to get worlds to open up portals until they are ready"/>
<node CREATED="1473333519167" ID="ID_985951557" MODIFIED="1473333532683" TEXT="But then, what if the user logs off and never logs in again?">
<node CREATED="1473333540943" ID="ID_1295995595" MODIFIED="1473333547882" TEXT="Or buries it"/>
<node CREATED="1473333570815" ID="ID_1118879777" MODIFIED="1473333668059" TEXT="We can make the special item drop when they log off, I think"/>
</node>
</node>
<node CREATED="1473333780623" ID="ID_1373267425" MODIFIED="1473333792891" TEXT="Maybe everyone gets a special portal block">
<node CREATED="1473333794015" ID="ID_1269758791" MODIFIED="1473333812451" TEXT="It takes a certain number of special portal blocks to open a portal"/>
</node>
<node CREATED="1473333829559" ID="ID_1534974046" MODIFIED="1473333866275" TEXT="Maybe only the oldest alive member on the server can enter a portal">
<node CREATED="1473333871375" ID="ID_1436227938" MODIFIED="1473333884067" TEXT="This &quot;activates&quot; the portal, and then the rest of the members can"/>
<node CREATED="1473333884759" ID="ID_578651351" MODIFIED="1473333935282" TEXT="However, that would still be bad, if most people are logged off, a rogue player could then still create and enter a portal of their own making"/>
</node>
<node CREATED="1473333988919" ID="ID_811637881" MODIFIED="1473334006835" TEXT="I think a portal key, or a special flint of steel would be best">
<node CREATED="1473334008927" ID="ID_110367794" MODIFIED="1473334036338" TEXT="If we can&apos;t make a special flint of steel work, we just make the first player that enters a portal activate it (and they must have the portal key)"/>
<node CREATED="1473334041319" ID="ID_115578355" MODIFIED="1473334050555" TEXT="It someone buries it, thats the way it goes."/>
</node>
<node CREATED="1473335252704" ID="ID_99279135" MODIFIED="1473335268451" TEXT="a portal key in hell can be used to create a portal to a specific world">
<node CREATED="1473335270023" ID="ID_1895819666" MODIFIED="1473335290715" TEXT="I think it should be a one way portal, and teleports them randomly within the world">
<node CREATED="1473335292127" ID="ID_668939824" MODIFIED="1473335313683" TEXT="This way, &quot;the devil&quot;, or whoever wants to be a king of hell can visit worlds if they get a portal key"/>
<node CREATED="1473335315519" ID="ID_773715383" MODIFIED="1473335384460" TEXT="Since the portal is still open on hell&apos;s side, it will allow others to come, too. They just can&apos;t get back (unless of course they die, which means they automatically go back), or if the owner of the portal key makes another portal on the world side"/>
</node>
</node>
<node CREATED="1473335418744" ID="ID_129545511" MODIFIED="1473335425219" TEXT="Maybe portals should all be one way.">
<node CREATED="1473335426336" ID="ID_1184933579" MODIFIED="1473335447548" TEXT="If the portal key holder wants to go back, they need to carry the items necessary to create a portal back to their world"/>
<node CREATED="1473335450047" ID="ID_508157317" MODIFIED="1473335477683" TEXT="This would complicate getting a friend back to a world they died in">
<node CREATED="1473335478976" ID="ID_1958660370" MODIFIED="1473335506733" TEXT="The PKH would have to open a portal, go find them in hell, and they both come back together"/>
<node CREATED="1473335516104" ID="ID_1226921339" MODIFIED="1473335541260" TEXT="If they died in hell, they&apos;d both be dead and there would be no way to get back, unless someone could recover the key"/>
<node CREATED="1473335561104" ID="ID_740422333" MODIFIED="1473335568348" TEXT="Maybe this would be better than having a static spawn point">
<node CREATED="1473335569656" ID="ID_1910508114" MODIFIED="1473335579980" TEXT="If the devil gets your key, your screwed, in other words">
<node CREATED="1473335609640" ID="ID_249537275" MODIFIED="1473335620188" TEXT="Your whole world would have a random spawn point to it from the nether."/>
</node>
</node>
</node>
<node CREATED="1473335687280" ID="ID_1612136858" MODIFIED="1473335721596" TEXT="What if we remove the &quot;only one portal active&quot; rule and have random but consistent spawn points">
<node CREATED="1473335723136" ID="ID_12520458" MODIFIED="1473335748076" TEXT="Then someone could even set up a portals between overworlds">
<node CREATED="1473335749384" ID="ID_504118464" MODIFIED="1473335761252" TEXT="Create portal to nether with k1"/>
<node CREATED="1473335761656" ID="ID_1306271311" MODIFIED="1473335767100" TEXT="Enter nether"/>
<node CREATED="1473335768552" ID="ID_889749172" MODIFIED="1473335781636" TEXT="Go through portal to world 2"/>
<node CREATED="1473335782088" ID="ID_182330312" MODIFIED="1473335792436" TEXT="Create portal using world one portal key"/>
</node>
</node>
</node>
<node CREATED="1473335834696" ID="ID_1153869783" MODIFIED="1473335862483" TEXT="Portals could still be two way. It just prevents mining obsidian, which I don&apos;t care too much about (since it&apos;s possible in vanilla anyway)">
<node CREATED="1473335863816" ID="ID_553608000" MODIFIED="1473335873476" TEXT="With two way portals and a portal key...">
<node CREATED="1473335918848" ID="ID_401645410" MODIFIED="1473335931812" TEXT="The portal holder wouldn&apos;t have to go into hell (and not first)">
<node CREATED="1473335935344" ID="ID_673447100" MODIFIED="1473335949900" TEXT="This may be better because the portal to hell may be unfair for some reason and cause instant death"/>
</node>
<node CREATED="1473335962592" ID="ID_116922802" MODIFIED="1473335970235" TEXT="Or they could go in, but have to jump right out"/>
</node>
</node>
<node CREATED="1473349983851" ID="ID_98650745" MODIFIED="1473350442952" TEXT="what if a portal key just made a portal appear on the other side as well?">
<node CREATED="1473350001995" ID="ID_22803797" MODIFIED="1473350047879" TEXT="That way we still can have both sides open."/>
<node CREATED="1473350051571" ID="ID_1332274588" MODIFIED="1473350068566" TEXT="We can check if the player has the key in inventory. If not, its a one way portal, bye player!"/>
<node CREATED="1473350069179" ID="ID_1329483500" MODIFIED="1473350079038" TEXT="If they do, then the portal is created on the other side"/>
<node CREATED="1473350084395" ID="ID_1850191999" MODIFIED="1473350102367" TEXT="Other players, entities, can then enter the portal after it has been created"/>
<node CREATED="1473350298467" ID="ID_3828311" MODIFIED="1473350316103" TEXT="It would be confusing though, and if a player made a mistake, they&apos;d be trapped out of their world forever. Kind of sad"/>
</node>
<node CREATED="1473350444043" ID="ID_1977839165" MODIFIED="1473350463543" TEXT="What if we don&apos;t have portal keys. Instead, only one portal can work?">
<node CREATED="1473350464411" ID="ID_1311843530" MODIFIED="1473350495343" TEXT="Then hell entities always have access to world as long as a portal exists"/>
</node>
<node CREATED="1473350514283" ID="ID_546038631" MODIFIED="1473350556599" TEXT="If we have a special flint and steel, and we just find the player around the portal. If they have a special flint and steel, we assume the portal is proper and create it, otherwise don&apos;t/ explode/ whatever">
<node CREATED="1473350576291" ID="ID_756455142" MODIFIED="1473350591319" TEXT="It&apos;s clear, then. The special flint and steel creates portals. Normal ones don&apos;t"/>
</node>
</node>
</node>
</node>
</node>
</node>
</map>
